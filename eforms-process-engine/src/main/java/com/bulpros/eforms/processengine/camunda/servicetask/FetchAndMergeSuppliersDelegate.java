package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.camunda.service.SupplierService;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.egov.model.supplier.SupplierDetailsInfo;
import com.bulpros.eforms.processengine.egov.model.supplier.Suppliers;
import com.bulpros.formio.dto.ResourceDto;
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
import org.springframework.security.core.Authentication;

import javax.inject.Named;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Setter
@Getter
@Named("fetchAndMergeSuppliersDelegate")
@Slf4j
public class FetchAndMergeSuppliersDelegate extends FetchAndMergeDelegate<SupplierDetailsInfo> implements JavaDelegate {

    private final SupplierService supplierService;
    private final RuntimeService runtimeService;

    private final static String INACTIVATED_SUPPLIERS = "inactivatedSuppliers";
    private final static String CREATED_SUPPLIERS = "createdSuppliers";
    private final static String SKIPPED_SUPPLIERS = "skippedSuppliers";
    private final static String SUPPLIERS_SYNC_COMPLETE = "SuppliersSyncComplete";
    private final static String SUPPLIERS_SYNC_RESULT = "syncResult";
    private final static String SUPPLIERS_SYNC_SUCCESS = "suppliersSyncSuccess";
    private final static String SUPPLIERS_SYNC_FAILURE = "suppliersSyncFailure";
    private Long pageContentSize = 100L;
    private Expression projectId;

    public FetchAndMergeSuppliersDelegate(SupplierService supplierService,
                                          RuntimeService runtimeService,
                                          SubmissionService submissionService, ConfigurationProperties configurationProperties) {
        super(submissionService, configurationProperties);
        this.supplierService = supplierService;
        this.runtimeService = runtimeService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String projectId = (String) this.getProjectId().getValue(delegateExecution);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Authentication authentication = AuthenticationService.createServiceAuthentication();

                List<ResourceDto> allSuppliersInSystem = submissionService.getAllSubmissions(projectId,
                        ValueTypeEnum.ID,
                        configurationProperties.getSupplierResourceName(),
                        ValueTypeEnum.PATH,
                        authentication, pageContentSize);
                Suppliers suppliersFromEpdaeu = supplierService.getAllSuppliers();

                List<String> allCodesFromEpdaeu = suppliersFromEpdaeu.getSuppliers().getSupplier().stream().map(s -> s.getCode()).collect(Collectors.toList());

                Map<String, String> suppliersToInactivate = allSuppliersInSystem.stream().filter(s -> !(allCodesFromEpdaeu.contains(s.getData().get(SUPPLIER_CODE))))
                        .collect(Collectors.toMap(s -> s.getData().get(SUPPLIER_CODE).toString(), s -> s.getData().get(TITLE).toString()));

                Map<String, String> createdSuppliers = new HashMap<>();
                Map<String, String> skippedSuppliers = new HashMap<>();
                String patchInactiveStatus = getInactiveStatusUpdateString();
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
                                supplierDetails.setProjectId(projectId);
                                if (!supplierSubmissionOptional.isEmpty()) {
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


                runtimeService.createMessageCorrelation(SUPPLIERS_SYNC_COMPLETE)
                        .processInstanceId(delegateExecution.getProcessInstanceId())
                        .setVariable(SUPPLIERS_SYNC_RESULT, SUPPLIERS_SYNC_SUCCESS)
                        .setVariable(INACTIVATED_SUPPLIERS, suppliersToInactivate)
                        .setVariable(CREATED_SUPPLIERS, createdSuppliers)
                        .setVariable(SKIPPED_SUPPLIERS, skippedSuppliers)
                        .correlateWithResult();

            } catch (Exception e) {
                log.error("Error while sync suppliers!!!", e);
                runtimeService.createMessageCorrelation(SUPPLIERS_SYNC_COMPLETE)
                        .processInstanceId(delegateExecution.getProcessInstanceId())
                        .setVariable(SUPPLIERS_SYNC_RESULT, SUPPLIERS_SYNC_FAILURE)
                        .correlateWithResult();
            }
        });
    }
}
