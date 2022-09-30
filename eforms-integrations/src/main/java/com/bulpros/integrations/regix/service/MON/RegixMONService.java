package com.bulpros.integrations.regix.service.MON;

import com.bulpros.integrations.regix.model.MON.GetChildStudentStatus.ChildStudentStatusRequestType;
import com.bulpros.integrations.regix.model.MON.GetChildStudentStatus.ChildStudentStatusResponse;
import com.bulpros.integrations.regix.model.MON.GetHigherEduStudentByStatus.HigherEduStudentByStatusRequestType;
import com.bulpros.integrations.regix.model.MON.GetHigherEduStudentByStatus.HigherEduStudentByStatusResponse;
import com.bulpros.integrations.regix.model.MON.GetHigherEduStudentByStatus.StudentStatusType;
import com.bulpros.integrations.regix.model.MON.MONOperation;
import com.bulpros.integrations.regix.model.client.CallContext;
import com.bulpros.integrations.regix.model.client.RegixClient;
import com.bulpros.integrations.regix.model.client.ServiceRequestData;
import com.bulpros.integrations.regix.model.client.ServiceResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("regixMONService")
@Slf4j
public class RegixMONService {

    private final RegixClient regixClient;

    public RegixMONService(RegixClient regixClient) {
        this.regixClient = regixClient;
    }

    public ChildStudentStatusResponse getChildStudentStatus(String childIdentifier) {
        ChildStudentStatusRequestType childStudentStatusRequest = new ChildStudentStatusRequestType();
        childStudentStatusRequest.setChildIdentifier(childIdentifier);
        ServiceRequestData requestData = RegixClient.createRequestData(MONOperation.GET_CHILD_STUDENT_STATUS, childStudentStatusRequest);
        CallContext ctx = new CallContext();
        requestData.setCallContext(ctx);
        ServiceResultData serviceResultData = regixClient.execute(requestData);
        Object object = serviceResultData.getData().getResponse().getAny();
        return (ChildStudentStatusResponse) object;
    }

    public HigherEduStudentByStatusResponse getHigherEduStudentByStatus(String studentIdentifier, StudentStatusType studentStatus) {
        HigherEduStudentByStatusRequestType higherEduStudentByStatusRequest = new HigherEduStudentByStatusRequestType();
        higherEduStudentByStatusRequest.setStudentIdentifier(studentIdentifier);
        higherEduStudentByStatusRequest.setStudentStatus(studentStatus);
        ServiceRequestData requestData = RegixClient.createRequestData(MONOperation.GET_HIGHER_EDU_STUDENT_BY_STATUS, higherEduStudentByStatusRequest);
        CallContext ctx = new CallContext();
        requestData.setCallContext(ctx);
        ServiceResultData serviceResultData = regixClient.execute(requestData);
        Object object = serviceResultData.getData().getResponse().getAny();
        return (HigherEduStudentByStatusResponse) object;
    }
}
