package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.camunda.service.CreateAndMergeSubmissionImpl;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.security.core.Authentication;

import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.repository.formio.ValueTypeEnum;
import com.bulpros.formio.repository.util.DataUtil;
import com.bulpros.formio.service.SubmissionService;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

@Slf4j
public abstract class FetchAndMergeDelegate<T> extends CreateAndMergeSubmissionImpl implements JavaDelegate {
    protected final SubmissionService submissionService;
    protected final ConfigurationProperties configurationProperties;

    protected final String SUPPLIER_CODE;
    protected final String EIK;
    protected final String TITLE;
    protected final String AR_ID;

    protected FetchAndMergeDelegate(SubmissionService submissionService, ConfigurationProperties configurationProperties) {
        super(submissionService);
        this.submissionService = submissionService;
        this.configurationProperties = configurationProperties;
        SUPPLIER_CODE = configurationProperties.getCodePropertyKey();
        EIK = configurationProperties.getEikPropertyKey();
        TITLE = configurationProperties.getTitlePropertyKey();
        AR_ID = configurationProperties.getArIdPropertyKey();
    }

    protected enum StatusEnum{
        ACTIVE ("active"),
        INACTIVE ("inactive"),
        IN_PROCESSING ("inProcessing");

        public String status;
        StatusEnum(String status) {
            this.status = status;
        }
    };
    
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        
    }
    
    protected String getInactiveStatusUpdateString() {
        var jsonPatchArray = new JSONArray();
        var updateStatus =  DataUtil.getJsonObjectForPatch(
                "replace",
                "/data/" + "status",
                StatusEnum.INACTIVE.status
        );
        jsonPatchArray.add(updateStatus);
        return jsonPatchArray.toJSONString();
    }
}
