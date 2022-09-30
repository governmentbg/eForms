package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.camunda.service.EServiceService;
import com.bulpros.eforms.processengine.camunda.service.ServiceSupplierService;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.egov.model.eservice.*;
import com.bulpros.eforms.processengine.exeptions.SyncFailerException;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.repository.formio.ValueTypeEnum;
import com.bulpros.formio.repository.util.AuthenticationService;
import com.bulpros.formio.service.SubmissionService;
import io.minio.errors.ErrorResponseException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@Getter
@Setter
@Component
@Named("fetchAndMergeEServicesDelegate")
@Scope("prototype")
public class FetchAndMergeEServicesDelegate extends FetchAndMergeDelegate<EServiceDetailsSubmission> {

    private final EServiceService eServiceService;
    private final ServiceSupplierService serviceSupplierService;
    private final RuntimeService runtimeService;
    private final ModelMapper modelMapper;

    private final static String SERVICE_NAME = "serviceName";
    private Map<String, ResourceDto> allCurrentSuppliers;
    private List<ResourceDto> allServicesPerSupplier;
    private Map<String, List<String>> servicesPerSuppliers = new HashMap<>();

    private Long pageContentSize = 100L;

    private enum ServiceStatusEnum {
        DRAFT("draft"),
        DEVELOPED("developed"),
        VALIDATED("validated"),
        APPROVED("approved"),
        PUBLISHED("published"),
        INACTIVE("inactive");

        public String status;

        ServiceStatusEnum(String status) {
            this.status = status;
        }
    }

    public FetchAndMergeEServicesDelegate(EServiceService eServiceService,
                                          ServiceSupplierService serviceSupplierService,
                                          RuntimeService runtimeService,
                                          ModelMapper modelMapper,
                                          SubmissionService submissionService,
                                          ConfigurationProperties configurationProperties) {
        super(submissionService, configurationProperties);
        this.eServiceService = eServiceService;
        this.serviceSupplierService = serviceSupplierService;
        this.modelMapper = modelMapper;
        this.runtimeService = runtimeService;
    }

    @Override
    @Retryable(value = {ErrorResponseException.class, SyncFailerException.class},
            maxAttempts = 3, backoff = @Backoff(delay = 30000))
    public void execute(DelegateExecution delegateExecution, String projectId) throws SyncFailerException {
        try {
            Authentication authentication = AuthenticationService.createServiceAuthentication();
            allCurrentSuppliers = getAllSuppliers(projectId, authentication);

            var supplierEiks = getActiveSuppliersEiks(projectId, authentication);
            supplierEiks.forEach(eik ->
                    servicesPerSuppliers.put(eik, Stream.ofNullable(eServiceService.getServicesBySupplierEIK(eik)
                            .getEServiceList()).flatMap(Collection::stream).map(EService::getArId).collect(Collectors.toList())));

            log.info("Get current services");
            inactivatedEServices = getAllServices(projectId, authentication);
            Set<String> processedServices = new HashSet<>();
            if (!servicesPerSuppliers.isEmpty()) {
                for (String eik : servicesPerSuppliers.keySet()) {
                    List<String> services = servicesPerSuppliers.get(eik);
                    List<String> notProcessedServices = services.stream().filter(s -> !processedServices.contains(s)).collect(Collectors.toList());
                    if (!notProcessedServices.isEmpty()) {
                        int startIndex = 0;
                        int step = 5;
                        int size = notProcessedServices.size();
                        if (size < step) {
                            extractDetailsAndProcessServices(notProcessedServices, authentication, projectId);
                        } else {
                            int endIndex = startIndex + step;
                            int iterations = size % 5 == 0 ? size / 5 : size / 5 + 1;
                            for (int i = 0; i < iterations; i++) {
                                List<String> forProcessing = notProcessedServices.subList(startIndex, endIndex);
                                startIndex = endIndex;
                                endIndex = startIndex + step > size ? size : startIndex + step;
                                extractDetailsAndProcessServices(forProcessing, authentication, projectId);
                            }
                        }
                    }
                    processedServices.addAll(services);
                }
                //Remaining resources are not present in egov.bg thus we set inactive status
                String patchStatus = getStatusUpdateString(ServiceStatusEnum.INACTIVE.status);
                if (configurationProperties.isDeactivateServices()) {
                    inactivatedEServices = inactivatedEServices.entrySet().stream()
                            .filter(this::isNotInactiveStatus)
                            .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
                    inactivatedEServices.keySet().removeAll(skippedEServices);
                    inactivatedEServices.values().forEach(unavailableEService ->
                            this.setServiceStatus(projectId, unavailableEService, authentication, patchStatus)
                    );
                    inactivatedEServices.keySet().forEach(service -> {
                        var serviceWithSupplier = this.getEasWithSupplier(projectId, authentication, service);
                        serviceWithSupplier.forEach(serviceWithSupp ->
                                updateEasWithSuppliersStatus(projectId, authentication, patchStatus, serviceWithSupp));
                    });
                }

                updateEasWithSuppliers(projectId, authentication);
            } else {
                log.error("No services found for update");
            }

        } catch (Exception e) {
            log.error("Error while sync services. Message: " + e.getMessage());
            throw new SyncFailerException("Error while sync services.");
        }
    }


    private boolean isNotInactiveStatus(Map.Entry<String, ResourceDto> entry) {
        return !entry.getValue().getData()
                .get(configurationProperties.getStatusPropertyKey()).equals(ServiceStatusEnum.INACTIVE.status);
    }

    private void extractDetailsAndProcessServices(List<String> services, Authentication authentication, String projectId) {
        EServiceDetails eServiceDetails = eServiceService.getServiceDetailsByNumber(services);
        if (eServiceDetails == null) {
            log.info("Services with ids: " + services + "are not processed!");
            this.skippedEServices.addAll(services);
            return;
        }

        List<String> skippedFromThisRequest = new ArrayList<>(services);
        for (EServiceDetailsInfo eServiceDetailsInfo : eServiceDetails.getEServiceDetail().getEServiceDetailsInfoList()) {
            if (nonNull(eServiceDetailsInfo)) {
                skippedFromThisRequest.remove(eServiceDetailsInfo.getServiceNumber());
            }
        }
        this.skippedEServices.addAll(skippedFromThisRequest);
        try {
            processServices(authentication, projectId, eServiceDetails);
        } catch (Exception e) {
            log.info("Reason: " + e.getMessage());
            log.info("Services with ids: " + services + "are not processed!");
            this.skippedEServices.addAll(services);
        }
    }

    private void processServices(Authentication authentication, String projectId, EServiceDetails eServicesDetails) throws Exception {
        List<EServiceDetailsInfo> newEServices = eServicesDetails.getEServiceDetail().getEServiceDetailsInfoList();
        for (EServiceDetailsInfo newEService : newEServices) {
            Thread.sleep(1000);
            try {
                if (isNull(newEService)) continue;
                String id = newEService.getServiceNumber();
                EServiceDetailsSubmission eServiceDetailsSubmission = modelMapper.map(newEService, EServiceDetailsSubmission.class);

                ResourceDto currentEService = inactivatedEServices.get(id);
                if (currentEService != null) {
                    final String[] removeFields = {AR_ID, STATUS};
                    mergeSubmissions(projectId, configurationProperties.getEServiceResourceName(), currentEService, eServiceDetailsSubmission, removeFields, authentication);
                    inactivatedEServices.remove(id);
                } else {
                    eServiceDetailsSubmission.setStatus(ServiceStatusEnum.DRAFT.status);
                    createSubmission(projectId, configurationProperties.getEServiceResourceName(), eServiceDetailsSubmission, authentication);
                    createdEServices.put(eServiceDetailsSubmission.getArId(), eServiceDetailsSubmission.getServiceName());
                }
            } catch (Exception e) {
                log.info("The process could not create or update service: " + newEService.getServiceNumber());
                log.info("Reason: " + e.getMessage());
                this.skippedEServices.add(newEService.getServiceNumber());
            }
        }
    }

    private void updateEasWithSuppliers(String projectId, Authentication authentication) {
        var services = getAllServices(projectId, authentication);
        AtomicReference<String> supplierCode = new AtomicReference();
        servicesPerSuppliers.entrySet().stream().forEach(entry -> {
                    var supplierCodeOptional = allCurrentSuppliers.entrySet()
                            .stream()
                            .filter(suppEntry -> suppEntry.getValue().getData().get("eik").equals(entry.getKey()))
                            .findFirst();
                    if (supplierCodeOptional.isPresent()) {
                        supplierCode.set((String) supplierCodeOptional.get().getValue().getData().get("code"));
                        allServicesPerSupplier = getAllServicesPerSupplier(projectId, supplierCode.get(), authentication);
                        ResourceDto currentSupplier = allCurrentSuppliers.get(supplierCode.get());
                        var servicesPerSupplier = entry.getValue();
                        for (String service : servicesPerSupplier) {
                            this.mergeWithEasSuppliers(projectId, authentication, service, currentSupplier, services);
                        }
                    }
                }
        );
    }

    private void mergeWithEasSuppliers(String projectId, Authentication authentication, String serviceNumber,
                                       ResourceDto supplier, Map<String, ResourceDto> services) {
        if (!services.containsKey(serviceNumber)) return;

        EServiceWithSupplierDetailsSubmission eServiceWithSupplierDetailsSubmission = new EServiceWithSupplierDetailsSubmission();
        eServiceWithSupplierDetailsSubmission.setServiceSupplierTitle(supplier.getData().get(TITLE).toString());
        eServiceWithSupplierDetailsSubmission.setSupplierEAS(supplier.getData().get(SUPPLIER_CODE).toString());
        eServiceWithSupplierDetailsSubmission.setArId(serviceNumber);
        eServiceWithSupplierDetailsSubmission.setServiceTitle((String) services.get(serviceNumber).getData().get("serviceName"));
        eServiceWithSupplierDetailsSubmission.setStatus(ServiceStatusEnum.DRAFT.status);

        var easWithSupplier = isExistEasWithSupplier(serviceNumber, supplier);
        if (easWithSupplier != null) {
            final String[] removeFields = {AR_ID, SUPPLIER_CODE, STATUS};
            mergeSubmissions(projectId, configurationProperties.getEasSuppliersResourceName(), easWithSupplier,
                    eServiceWithSupplierDetailsSubmission, removeFields, authentication);
        } else {
            createSubmission(projectId, configurationProperties.getEasSuppliersResourceName(),
                    eServiceWithSupplierDetailsSubmission, authentication);
        }
    }

    private ResourceDto isExistEasWithSupplier(String easId, ResourceDto supplier) {
        var easWithSupplier = allServicesPerSupplier
                .stream()
                .filter(service -> service.getData().get(AR_ID).equals(easId)
                        && service.getData().get("supplierEAS").equals(supplier.getData().get(SUPPLIER_CODE)))
                .findFirst();
        if (easWithSupplier.isPresent()) {
            return easWithSupplier.get();
        }
        return null;
    }

    private List<ResourceDto> getEasWithSupplier(String projectId, Authentication authentication, String easId) {
        return this.submissionService.getAllSubmissionsWithFilter(
                new ResourcePath(projectId, configurationProperties.getEasSuppliersResourceName()),
                authentication,
                List.of(
                        new SubmissionFilter(SubmissionFilterClauseEnum.NONE,
                                Collections.singletonMap(configurationProperties.getArIdPropertyKey(), easId))),
                pageContentSize);
    }


    private void setServiceStatus(String projectId, ResourceDto service, Authentication authentication, String status) {
        this.submissionService.updateSubmission(
                projectId,
                ValueTypeEnum.ID,
                configurationProperties.getEServiceResourceName(),
                ValueTypeEnum.PATH,
                authentication,
                service.get_id(),
                status);
    }

    private List<String> getActiveSuppliersEiks(String projectId, Authentication authentication) {
        var suppliersList = serviceSupplierService.getActiveSuppliers(projectId, authentication);
        return suppliersList.stream()
                .map(supplier -> supplier.getData().get(EIK).toString())
                .collect(Collectors.toList());
    }

    private Map<String, ResourceDto> getAllServices(String projectId, Authentication authentication) {
        var currentEServices = this.submissionService.getAllSubmissions(projectId,
                ValueTypeEnum.ID,
                configurationProperties.getEServiceResourceName(),
                ValueTypeEnum.PATH,
                authentication, pageContentSize);
        return currentEServices.stream()
                .filter(s -> s.getData().get(AR_ID) != null)
                .collect(Collectors.toMap(s -> (String) s.getData().get(AR_ID), Function.identity()));
    }

    private Map<String, ResourceDto> getAllSuppliers(String projectId, Authentication authentication) {
        List<ResourceDto> allSuppliersInSystem = this.submissionService.getAllSubmissions(projectId,
                ValueTypeEnum.ID,
                configurationProperties.getSupplierResourceName(),
                ValueTypeEnum.PATH,
                authentication, pageContentSize);
        return allSuppliersInSystem.stream().collect(Collectors.toMap(s -> (String) s.getData().get(SUPPLIER_CODE), Function.identity()));
    }
}
