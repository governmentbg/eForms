package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.camunda.service.CreateAndMergeSubmissionImpl;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.repository.formio.ValueTypeEnum;
import com.bulpros.formio.repository.util.DataUtil;
import com.bulpros.formio.service.SubmissionService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.security.core.Authentication;

import java.util.*;

@Slf4j
@Getter
public abstract class FetchAndMergeDelegate<T> extends CreateAndMergeSubmissionImpl {
    protected final SubmissionService submissionService;
    protected final ConfigurationProperties configurationProperties;

    protected final String SUPPLIER_CODE;
    protected final String EIK;
    protected final String TITLE;
    protected final String AR_ID;
    protected final String STATUS;

    protected Map<String, String> suppliersToInactivate;
    protected Map<String, String> createdSuppliers = new HashMap<>();
    protected Map<String, String> skippedSuppliers = new HashMap<>();
    protected Map<String, ResourceDto> inactivatedEServices;
    protected Map<String, String> createdEServices = new HashMap<>();
    protected List<String> skippedEServices = new ArrayList<>();

    private Long pageContentSize = 100L;

    protected FetchAndMergeDelegate(SubmissionService submissionService, ConfigurationProperties configurationProperties) {
        super(submissionService);
        this.submissionService = submissionService;
        this.configurationProperties = configurationProperties;
        SUPPLIER_CODE = configurationProperties.getCodePropertyKey();
        EIK = configurationProperties.getEikPropertyKey();
        TITLE = configurationProperties.getTitlePropertyKey();
        AR_ID = configurationProperties.getArIdPropertyKey();
        STATUS = configurationProperties.getStatusPropertyKey();
    }

    protected abstract void execute(DelegateExecution delegateExecution, String projectId) throws Exception;

    protected String getStatusUpdateString(String status) {
        var jsonPatchArray = new JSONArray();
        var updateStatus = DataUtil.getJsonObjectForPatch(
                "add",
                "/data/" + "status",
                status
        );
        jsonPatchArray.add(updateStatus);
        return jsonPatchArray.toJSONString();
    }

    protected List<ResourceDto> getAllServicesPerSupplier(String projectId, String supplierEik, Authentication authentication) {
        return this.submissionService.getAllSubmissionsWithFilter(
                new ResourcePath(projectId, configurationProperties.getEasSuppliersResourceName()),
                authentication, List.of(
                        new SubmissionFilter(SubmissionFilterClauseEnum.NONE,
                                Collections.singletonMap(configurationProperties.getServiceSupplierPropertyKey(), supplierEik))),
                pageContentSize);
    }


    protected ResourceDto updateEasWithSuppliersStatus(String projectId, Authentication authentication,
                                                       String patchStatus, ResourceDto serviceWithSupp) {
        return submissionService.updateSubmission(
                projectId,
                ValueTypeEnum.ID,
                configurationProperties.getEasSuppliersResourceName(),
                ValueTypeEnum.PATH,
                authentication,
                serviceWithSupp.get_id(),
                patchStatus);
    }

}
