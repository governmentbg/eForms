package com.bulpros.integrations.regix.service.NELK;

import com.bulpros.integrations.regix.model.NELK.GetAllExpertDecisionsByIdentifier.ExpertDecisionsResponse;
import com.bulpros.integrations.regix.model.NELK.GetAllExpertDecisionsByIdentifier.GetAllExpertDecisionsByIdentifierRequest;
import com.bulpros.integrations.regix.model.NELK.NELKOperation;
import com.bulpros.integrations.regix.model.client.CallContext;
import com.bulpros.integrations.regix.model.client.RegixClient;
import com.bulpros.integrations.regix.model.client.ServiceRequestData;
import com.bulpros.integrations.regix.model.client.ServiceResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("regixNELKService")
@Slf4j
public class RegixNELKService {

    private final RegixClient regixClient;

    public RegixNELKService(RegixClient regixClient) {
        this.regixClient = regixClient;
    }

    public ExpertDecisionsResponse getAllExpertDecisionsByIdentifier(String identifier) {
        GetAllExpertDecisionsByIdentifierRequest getAllExpertDecisionsByIdentifierRequest = new GetAllExpertDecisionsByIdentifierRequest();
        getAllExpertDecisionsByIdentifierRequest.setIdentifier(identifier);
        ServiceRequestData requestData = RegixClient.createRequestData(NELKOperation.GET_ALL_EXPERT_DECISION_BY_IDENTIFIER, getAllExpertDecisionsByIdentifierRequest);
        CallContext ctx = new CallContext();
        requestData.setCallContext(ctx);
        ServiceResultData serviceResultData = regixClient.execute(requestData);
        Object object = serviceResultData.getData().getResponse().getAny();
        return (ExpertDecisionsResponse) object;
    }

}
