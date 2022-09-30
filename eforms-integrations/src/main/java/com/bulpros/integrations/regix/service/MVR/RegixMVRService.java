package com.bulpros.integrations.regix.service.MVR;

import com.bulpros.integrations.regix.model.MVR.GetMotorVehicleRegistrationInfo.MotorVehicleRegistrationRequestType;
import com.bulpros.integrations.regix.model.MVR.GetMotorVehicleRegistrationInfo.MotorVehicleRegistrationResponseType;
import com.bulpros.integrations.regix.model.MVR.GetPersonalIdentityV3.PersonalIdentityInfoRequestType;
import com.bulpros.integrations.regix.model.MVR.GetPersonalIdentityV3.PersonalIdentityInfoResponseType;
import com.bulpros.integrations.regix.model.MVR.MVROperation;
import com.bulpros.integrations.regix.model.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;

@Component("regixMVRService")
@Slf4j
public class RegixMVRService {

    private final RegixClient regixClient;

    public RegixMVRService(RegixClient regixClient) {
        this.regixClient = regixClient;
    }

    public PersonalIdentityInfoResponseType getPersonalIdentityV3(String egn, String identityDocumentNumber) {
        PersonalIdentityInfoRequestType personalIdentityInfoRequest = new PersonalIdentityInfoRequestType();
        personalIdentityInfoRequest.setEGN(egn);
        personalIdentityInfoRequest.setIdentityDocumentNumber(identityDocumentNumber);
        ServiceRequestData requestData = RegixClient.createRequestData(MVROperation.GЕТ_PERSONAL_IDENTITY_V3, personalIdentityInfoRequest);
        CallContext ctx = new CallContext();
        ObjectFactory factory = new ObjectFactory();
        JAXBElement<String> employeeIdentifier = factory.createCallContextEmployeeIdentifier("9999999999");
        ctx.setEmployeeIdentifier(employeeIdentifier);
        JAXBElement<String> employeeNames = factory.createCallContextEmployeeNames("User");
        ctx.setEmployeeNames(employeeNames);
        ctx.setServiceURI("999");
        ctx.setServiceType("999");
        ctx.setLawReason("LawReason");
        requestData.setCallContext(ctx);
        ServiceResultData serviceResultData = regixClient.execute(requestData);
        Object object = serviceResultData.getData().getResponse().getAny();
        return (PersonalIdentityInfoResponseType) object;
    }

    public MotorVehicleRegistrationResponseType getMotorVehicleRegistrationInfo(String identifier) {
        MotorVehicleRegistrationRequestType motorVehicleRegistrationRequest = new MotorVehicleRegistrationRequestType();
        motorVehicleRegistrationRequest.setIdentifier(identifier);
        ServiceRequestData requestData = RegixClient.createRequestData(MVROperation.GET_MOTOR_VEHICLE_REGISTRATION_INFO, motorVehicleRegistrationRequest);
        CallContext ctx = new CallContext();
        ObjectFactory factory = new ObjectFactory();
        JAXBElement<String> employeeIdentifier = factory.createCallContextEmployeeIdentifier("9999999999");
        ctx.setEmployeeIdentifier(employeeIdentifier);
        JAXBElement<String> employeeNames = factory.createCallContextEmployeeNames("User");
        ctx.setEmployeeNames(employeeNames);
        ctx.setServiceURI("999");
        ctx.setServiceType("999");
        ctx.setLawReason("LawReason");
        requestData.setCallContext(ctx);
        ServiceResultData serviceResultData = regixClient.execute(requestData);
        Object object = serviceResultData.getData().getResponse().getAny();
        return (MotorVehicleRegistrationResponseType) object;
    }

}
