package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.camunda.service.EServiceService;
import com.bulpros.eforms.processengine.camunda.service.ServiceSupplierService;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.egov.model.eservice.*;
import com.bulpros.eforms.processengine.egov.model.supplier.Supplier;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.repository.formio.ValueTypeEnum;
import com.bulpros.formio.repository.util.AuthenticationService;
import com.bulpros.formio.service.SubmissionService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@Named("fetchAndMergeEServicesDelegate")
@Slf4j
@Component

public class FetchAndMergeEServicesDelegate extends FetchAndMergeDelegate<EServiceDetailsSubmission> implements JavaDelegate {

    private final EServiceService eServiceService;
    private final ServiceSupplierService serviceSupplierService;
    private final RuntimeService runtimeService;
    private final ModelMapper modelMapper;

    private Expression projectId;

    private final static String SERVICE_NAME = "serviceName";
    private final static String PROCESS = "Process_";
    private final static String SERVICE_URL = "https://{host}/?easId=";
    private final static String INACTIVE_SERVICES = "inactiveServices";
    private final static String CREATED_SERVICES = "createdServices";
    private final static String SERVICES_SYNC_SUCCESS = "ServicesSyncSuccess";
    private final static String SERVICES_SYNC_FAILURE = "ServicesSyncFailure";

    private Map<String, ResourceDto> currentEServicesMap;
    private Map<String, ResourceDto> allCurrentSuppliers;
    private List<ResourceDto> allServicesWithSuppliers;
    private Map<String, String> createdEServices = new HashMap<>();
    private Long pageContentSize = 100L;

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
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String projectId = (String) this.getProjectId().getValue(delegateExecution);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Authentication authentication = AuthenticationService.createServiceAuthentication();
                allCurrentSuppliers = getAllSuppliers(projectId, authentication);

                var supplierEiks = getActiveSuppliersEiks(projectId, authentication);

                Map<String, List<String>> servicesPerSuppliers = new HashMap<>();
                supplierEiks.forEach(eik ->
                        servicesPerSuppliers.put(eik, Stream.ofNullable(eServiceService.getServicesBySupplierEIK(eik)
                                .getEServiceList()).flatMap(Collection::stream).map(EService::getArId).collect(Collectors.toList())));

                log.info("Get current services");
                currentEServicesMap = getAllServices(projectId, authentication);
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
                    String patchStatus = getInactiveStatusUpdateString();
                    if (configurationProperties.isDeactivateServices())
                        currentEServicesMap.values().forEach(unavailableEService ->
                                this.setServiceStatus(projectId, unavailableEService, authentication, patchStatus)
                        );
                } else {
                    log.error("No services found for update");
                }
                runtimeService.createMessageCorrelation(SERVICES_SYNC_SUCCESS)
                        .processInstanceId(delegateExecution.getProcessInstanceId())
                        .setVariable(INACTIVE_SERVICES, currentEServicesMap)
                        .setVariable(CREATED_SERVICES, createdEServices)
                        .correlateWithResult();

            } catch (Exception e) {
                log.error("Error while sync services. Message: " + e.getMessage());

                runtimeService.createMessageCorrelation(SERVICES_SYNC_FAILURE)
                        .processInstanceId(delegateExecution.getProcessInstanceId())
                        .correlateWithResult();
            }
        });
    }

    private void extractDetailsAndProcessServices(List<String> services, Authentication authentication, String projectId) {
        EServiceDetails eServiceDetails = eServiceService.getServiceDetailsByNumber(services);
        if (eServiceDetails == null) {
            log.info("Services with ids: " + services + "are not processed!");
        }
        try {
            processServices(authentication, projectId, eServiceDetails);
        } catch (Exception e) {
            log.info("Services with ids: " + services + "are not processed!");
        }
    }

    private void processServices(Authentication authentication, String projectId, EServiceDetails eServicesDetails) throws Exception {
        List<EServiceDetailsInfo> newEServices = eServicesDetails.getEServiceDetail().getEServiceDetailsInfoList();
        for (EServiceDetailsInfo newEService : newEServices) {
            Thread.sleep(1000);
            String id = newEService.getServiceNumber();
            EServiceDetailsSubmission eServiceDetailsSubmission = modelMapper.map(newEService, EServiceDetailsSubmission.class);
            updateEasWithSuppliers(projectId, authentication, newEService);

            eServiceDetailsSubmission.setProcessDefinitionId(PROCESS + eServiceDetailsSubmission.getArId());
            eServiceDetailsSubmission.setStatus(StatusEnum.ACTIVE.status);
            ResourceDto currentEService = currentEServicesMap.get(id);
            if (currentEService != null) {
                final String[] removeFields = {AR_ID};
                mergeSubmissions(projectId, configurationProperties.getEServiceResourceName(), currentEService, eServiceDetailsSubmission, removeFields, authentication);
                currentEServicesMap.remove(id);
                createdEServices.put(currentEService.getData().get(AR_ID).toString(), currentEService.getData().get(SERVICE_NAME).toString());
            } else {
                createSubmission(projectId, configurationProperties.getEServiceResourceName(), eServiceDetailsSubmission, authentication);
            }
        }
    }

    private void updateEasWithSuppliers(String projectId, Authentication authentication,
                                        EServiceDetailsInfo eServiceDetailsInfo) {
        if (eServiceDetailsInfo.getSuppliers() == null)
            return;
        List<Supplier> suppliersPerService = eServiceDetailsInfo.getSuppliers().getSupplier();
        allServicesWithSuppliers = getAllServicesWithSuppliers(projectId, eServiceDetailsInfo.getServiceNumber(), authentication);
        for (Supplier supplier : suppliersPerService) {
            ResourceDto currentSupplier = allCurrentSuppliers.get(supplier.getCode());
            this.mergeWithEasSuppliers(projectId, authentication, eServiceDetailsInfo, currentSupplier);
        }
    }

    private void mergeWithEasSuppliers(String projectId, Authentication authentication, EServiceDetailsInfo eServiceDetailsInfo, ResourceDto supplier) {
        EServiceWithSupplierDetailsSubmission eServiceWithSupplierDetailsSubmission = new EServiceWithSupplierDetailsSubmission();
        eServiceWithSupplierDetailsSubmission.setServiceSupplierTitle(supplier.getData().get(TITLE).toString());
        eServiceWithSupplierDetailsSubmission.setSupplierEAS(supplier.getData().get(SUPPLIER_CODE).toString());
        eServiceWithSupplierDetailsSubmission.setArId(eServiceDetailsInfo.getServiceNumber());
        eServiceWithSupplierDetailsSubmission.setServiceTitle(eServiceDetailsInfo.getServiceName());
        eServiceWithSupplierDetailsSubmission.setStatus(StatusEnum.ACTIVE.status);

        var easWithSupplier = isExistEasWithSupplier(eServiceDetailsInfo.getServiceNumber(), supplier);
        if (easWithSupplier != null) {
            final String[] removeFields = {AR_ID, SUPPLIER_CODE};
            mergeSubmissions(projectId, configurationProperties.getEasSuppliersResourceName(), easWithSupplier,
                    eServiceWithSupplierDetailsSubmission, removeFields, authentication);
        } else {
            createSubmission(projectId, configurationProperties.getEasSuppliersResourceName(),
                    eServiceWithSupplierDetailsSubmission, authentication);
        }
    }

    private ResourceDto isExistEasWithSupplier(String easId, ResourceDto supplier) {
        var easWithSupplier = allServicesWithSuppliers
                .stream()
                .filter(service -> service.getData().get(AR_ID).equals(easId)
                        && service.getData().get("supplierEAS").equals(supplier.getData().get(SUPPLIER_CODE)))
                .findFirst();
        if (easWithSupplier.isPresent()) {
            return easWithSupplier.get();
        }
        return null;
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

    private List<ResourceDto> getAllServicesWithSuppliers(String projectId, String easId, Authentication authentication) {
        return this.submissionService.getAllSubmissionsWithFilter(
                new ResourcePath(projectId, configurationProperties.getEasSuppliersResourceName()),
                authentication, List.of(
                        new SubmissionFilter(SubmissionFilterClauseEnum.NONE,
                                Collections.singletonMap(configurationProperties.getArIdPropertyKey(), easId))),
                pageContentSize);
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
