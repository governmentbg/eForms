package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.exeptions.SyncFailerException;
import lombok.Getter;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

import javax.inject.Named;
import java.util.LinkedHashMap;
import java.util.concurrent.Executors;

@Getter
@Named("fetchAndMergeSuppliersAndEServices")
@Scope("prototype")
public class FetchAndMergeSuppliersAndEServicesDelegate implements JavaDelegate {
    private enum Status {
        STARTED("started"),
        SUCCESS("success"),
        FAILURE("failure");

        Status(String name) {
            this.name = name;
        }

        public String name;
    }

    private final static String INACTIVATED_SUPPLIERS = "inactivatedSuppliers";
    private final static String CREATED_SUPPLIERS = "createdSuppliers";
    private final static String SKIPPED_SUPPLIERS = "skippedSuppliers";
    private final static String SYNC_RESULT = "syncResult";
    private final static String SERVICES_AND_SUPPLIERS_SYNC_COMPLETED = "servicesAndSuppliersSyncCompleted";
    private final static String INACTIVE_SERVICES = "inactiveServices";
    private final static String CREATED_SERVICES = "createdServices";
    private final static String SKIPPED_SERVICES = "skippedServices";
    private final static String CONTEXT = "context";

    private final static String SERVICES_AND_SUPPLIERS_SYNC_SUCCESS = "ServicesAndSuppliersSyncSuccess";
    private final static String SERVICES_AND_SUPPLIERS_SYNC_FAILURE = "ServicesAndSuppliersSyncFailure";

    private final RuntimeService runtimeService;
    private FetchAndMergeSuppliersDelegate fetchAndMergeSuppliersDelegate;
    private FetchAndMergeEServicesDelegate fetchAndMergeEServicesDelegate;
    private Expression projectId;

    public FetchAndMergeSuppliersAndEServicesDelegate(@Qualifier("fetchAndMergeSuppliersDelegate") FetchAndMergeSuppliersDelegate fetchAndMergeSuppliersDelegate,
                                                      @Qualifier("fetchAndMergeEServicesDelegate") FetchAndMergeEServicesDelegate fetchAndMergeEServicesDelegate,
                                                      RuntimeService runtimeService) {
        this.fetchAndMergeSuppliersDelegate = fetchAndMergeSuppliersDelegate;
        this.fetchAndMergeEServicesDelegate = fetchAndMergeEServicesDelegate;
        this.runtimeService = runtimeService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String projectId = (String) this.getProjectId().getValue(execution);
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                runtimeService.setVariable(execution.getProcessInstanceId(), SYNC_RESULT, Status.STARTED.name);
                LinkedHashMap<Object, Object> context = new LinkedHashMap<>();
                context.put("formioBaseProject", projectId);
                runtimeService.setVariable(execution.getProcessInstanceId(), CONTEXT, context);

                fetchAndMergeSuppliersDelegate.execute(execution, projectId);
                runtimeService.setVariable(execution.getProcessInstanceId(), INACTIVATED_SUPPLIERS, fetchAndMergeSuppliersDelegate.getSuppliersToInactivate());
                runtimeService.setVariable(execution.getProcessInstanceId(), CREATED_SUPPLIERS, fetchAndMergeSuppliersDelegate.getCreatedSuppliers());
                runtimeService.setVariable(execution.getProcessInstanceId(), SKIPPED_SUPPLIERS, fetchAndMergeSuppliersDelegate.getSkippedSuppliers());

                fetchAndMergeEServicesDelegate.execute(execution, projectId);
                runtimeService.setVariable(execution.getProcessInstanceId(), SYNC_RESULT, Status.SUCCESS.name);
                runtimeService.createMessageCorrelation(SERVICES_AND_SUPPLIERS_SYNC_COMPLETED)
                        .processInstanceId(execution.getProcessInstanceId())
                        .setVariable(SKIPPED_SERVICES, fetchAndMergeEServicesDelegate.getSkippedEServices())
                        .setVariable(INACTIVE_SERVICES, fetchAndMergeEServicesDelegate.getInactivatedEServices())
                        .setVariable(CREATED_SERVICES, fetchAndMergeEServicesDelegate.getCreatedEServices())
                        .correlateWithResult();
            } catch (SyncFailerException e) {
                runtimeService.setVariable(execution.getProcessInstanceId(), SYNC_RESULT, Status.FAILURE.name);
                runtimeService.createMessageCorrelation(SERVICES_AND_SUPPLIERS_SYNC_COMPLETED)
                        .processInstanceId(execution.getProcessInstanceId())
                        .correlateWithResult();
            }
        });
    }
}
