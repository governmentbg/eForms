package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.camunda.service.SupplierService;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.egov.model.supplier.SupplierDetailsInfo;
import com.bulpros.eforms.processengine.egov.model.supplier.Suppliers;
import com.bulpros.eforms.processengine.exeptions.SyncFailerException;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.repository.formio.ValueTypeEnum;
import com.bulpros.formio.repository.util.AuthenticationService;
import com.bulpros.formio.service.SubmissionService;
import io.minio.errors.ErrorResponseException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.context.annotation.Scope;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Setter
@Getter
@Component
@Named("fetchAndMergeSuppliersDelegate")
@Scope("prototype")
public class FetchAndMergeSuppliersDelegate extends FetchAndMergeDelegate<SupplierDetailsInfo> {

    private final SupplierService supplierService;
    private final RuntimeService runtimeService;

    private Long pageContentSize = 100L;

    private enum StatusEnum {
        ACTIVE("active"),
        INACTIVE("inactive"),
        IN_PROCESSING("inProcessing");

        public String status;

        StatusEnum(String status) {
            this.status = status;
        }
    }

    public FetchAndMergeSuppliersDelegate(SupplierService supplierService,
                                          RuntimeService runtimeService,
                                          SubmissionService submissionService, ConfigurationProperties configurationProperties) {
        super(submissionService, configurationProperties);
        this.supplierService = supplierService;
        this.runtimeService = runtimeService;
    }

    @Override
    @Retryable(value = {ErrorResponseException.class, SyncFailerException.class},
            maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void execute(DelegateExecution delegateExecution, String projectId) throws SyncFailerException {

        try {
            Authentication authentication = AuthenticationService.createServiceAuthentication();

            List<ResourceDto> allSuppliersInSystem = submissionService.getAllSubmissions(projectId,
                    ValueTypeEnum.ID,
                    configurationProperties.getSupplierResourceName(),
                    ValueTypeEnum.PATH,
                    authentication, pageContentSize);
            Suppliers suppliersFromEpdaeu = supplierService.getAllSuppliers();

            List<String> allCodesFromEpdaeu = suppliersFromEpdaeu.getSuppliers().getSupplier().stream().map(s -> s.getCode()).collect(Collectors.toList());

            suppliersToInactivate = allSuppliersInSystem.stream()
                    .filter(s -> !(allCodesFromEpdaeu.contains(s.getData().get(SUPPLIER_CODE))))
                    .filter(this::inNotInactive)
                    .collect(Collectors.toMap(s -> s.getData().get(SUPPLIER_CODE).toString(), s -> s.getData().get(TITLE).toString()));

            String patchInactiveStatus = getStatusUpdateString(StatusEnum.INACTIVE.status);
            if (configurationProperties.isDeactivateServiceSuppliers()) {
                for (String supplier : suppliersToInactivate.keySet()) {
                    submissionService.updateSubmission(
                            projectId,
                            ValueTypeEnum.ID,
                            configurationProperties.getSupplierResourceName(),
                            ValueTypeEnum.PATH,
                            authentication,
                            allSuppliersInSystem.stream().filter(resourceDto -> resourceDto.getData().get(SUPPLIER_CODE).equals(supplier)).findFirst().get().get_id(),
                            patchInactiveStatus);
                    var servicesPerSupplier = getAllServicesPerSupplier(projectId, supplier, authentication);
                    servicesPerSupplier
                            .forEach(serviceWithSupp -> updateEasWithSuppliersStatus(projectId, authentication, patchInactiveStatus, serviceWithSupp)
                            );
                }

            }

            suppliersFromEpdaeu.getSuppliers()
                    .getSupplier()
                    .forEach(supplier -> {
                        var code = supplier.getCode();
                        Optional<ResourceDto> supplierSubmissionOptional = allSuppliersInSystem.stream().filter(resourceDto -> resourceDto.getData().get(SUPPLIER_CODE).equals(code)).findAny();
                        try {
                            var supplierDetails = supplierService.getSupplierDetails(code);
                            supplierDetails.setStatus(StatusEnum.ACTIVE.status);
                            if (supplierSubmissionOptional.isPresent()) {
                                final String[] removeFields = {SUPPLIER_CODE, EIK};
                                mergeSubmissions(projectId, configurationProperties.getSupplierResourceName(),
                                        supplierSubmissionOptional.get(),
                                        supplierDetails, removeFields, authentication);
                            } else {
                                createSubmission(projectId, configurationProperties.getSupplierResourceName(),
                                        supplierDetails, authentication);
                                createdSuppliers.put(supplierDetails.getCode(), supplierDetails.getTitle());
                            }
                        } catch (Exception e) {
                            skippedSuppliers.put(supplier.getCode(), supplier.getTitle());
                        }
                    });

        } catch (Exception e) {
            log.error("Error while sync suppliers!!!", e);
            throw new SyncFailerException("Error while sync suppliers!!!");
        }
    }

    private boolean inNotInactive(ResourceDto resourceDto) {
        return !resourceDto.getData().get(configurationProperties.getStatusPropertyKey()).equals(StatusEnum.INACTIVE.status);
    }
}
