package com.bulpros.integrations.regix.service.AZ;

import com.bulpros.integrations.regix.model.AZ.AzOperation;
import com.bulpros.integrations.regix.model.AZ.GetJobSeekerStatus.JobSeekerStatusData;
import com.bulpros.integrations.regix.model.AZ.GetJobSeekerStatus.JobSeekerStatusRequestType;
import com.bulpros.integrations.regix.model.client.CallContext;
import com.bulpros.integrations.regix.model.client.RegixClient;
import com.bulpros.integrations.regix.model.client.ServiceRequestData;
import com.bulpros.integrations.regix.model.client.ServiceResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("regixAZService")
@Slf4j
public class RegixAZService {

    private final RegixClient regixClient;

    public RegixAZService(RegixClient regixClient) {
        this.regixClient = regixClient;
    }

    public JobSeekerStatusData getJobSeekerStatus(String personalId) {
        JobSeekerStatusRequestType jobSeekerStatusRequest = new JobSeekerStatusRequestType();
        jobSeekerStatusRequest.setPersonalID(personalId);
        ServiceRequestData requestData = RegixClient.createRequestData(AzOperation.GET_JOB_SEEKER_STATUS, jobSeekerStatusRequest);
        CallContext ctx = new CallContext();
        requestData.setCallContext(ctx);
        ServiceResultData serviceResultData = regixClient.execute(requestData);
        Object object = serviceResultData.getData().getResponse().getAny();
        return (JobSeekerStatusData) object;
    }
}
