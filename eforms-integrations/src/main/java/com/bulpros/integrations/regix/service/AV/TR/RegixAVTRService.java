package com.bulpros.integrations.regix.service.AV.TR;

import com.bulpros.integrations.regix.model.AV.TR.GetActualState.ActualStateRequestType;
import com.bulpros.integrations.regix.model.AV.TR.GetActualState.ActualStateResponseType;
import com.bulpros.integrations.regix.model.AV.TR.TrOperation;
import com.bulpros.integrations.regix.model.client.CallContext;
import com.bulpros.integrations.regix.model.client.RegixClient;
import com.bulpros.integrations.regix.model.client.ServiceRequestData;
import com.bulpros.integrations.regix.model.client.ServiceResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("regixAVTRService")
@Slf4j
public class RegixAVTRService {

    private final RegixClient regixClient;

    public RegixAVTRService(RegixClient regixClient) {
        this.regixClient = regixClient;
    }

    public ActualStateResponseType getActualState(String uic) {
        ActualStateRequestType actualStateRequest = new ActualStateRequestType();
        actualStateRequest.setUIC(uic);
        ServiceRequestData requestData = RegixClient.createRequestData(TrOperation.GET_ACTUAL_STATE, actualStateRequest);
        CallContext ctx = new CallContext();
        requestData.setCallContext(ctx);
        ServiceResultData serviceResultData = regixClient.execute(requestData);
        Object object = serviceResultData.getData().getResponse().getAny();
        return (ActualStateResponseType) object;
    }
}
