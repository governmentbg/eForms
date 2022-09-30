package com.bulpros.integrations.regix.service.GRAO;

import com.bulpros.integrations.regix.model.GRAO.MaritalStatusSearch.MaritalStatusRequestType;
import com.bulpros.integrations.regix.model.GRAO.MaritalStatusSearch.MaritalStatusResponseType;
import com.bulpros.integrations.regix.model.GRAO.MaritalStatusSpouseChildrenSearch.MaritalStatusSpouseChildrenRequestType;
import com.bulpros.integrations.regix.model.GRAO.MaritalStatusSpouseChildrenSearch.MaritalStatusSpouseChildrenResponseType;
import com.bulpros.integrations.regix.model.GRAO.PermanentAddressSearch.PermanentAddressRequestType;
import com.bulpros.integrations.regix.model.GRAO.PermanentAddressSearch.PermanentAddressResponseType;
import com.bulpros.integrations.regix.model.GRAO.PersonDataSearch.GraoOperation;
import com.bulpros.integrations.regix.model.GRAO.PersonDataSearch.PersonDataRequestType;
import com.bulpros.integrations.regix.model.GRAO.PersonDataSearch.PersonDataResponseType;
import com.bulpros.integrations.regix.model.GRAO.RelationsSearch.RelationsRequestType;
import com.bulpros.integrations.regix.model.GRAO.RelationsSearch.RelationsResponseType;
import com.bulpros.integrations.regix.model.GRAO.TemporaryAddressSearch.TemporaryAddressRequestType;
import com.bulpros.integrations.regix.model.GRAO.TemporaryAddressSearch.TemporaryAddressResponseType;
import com.bulpros.integrations.regix.model.client.CallContext;
import com.bulpros.integrations.regix.model.client.RegixClient;
import com.bulpros.integrations.regix.model.client.ServiceRequestData;
import com.bulpros.integrations.regix.model.client.ServiceResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

@Component("regixGraoService")
@Slf4j
public class RegixGraoService {

    private final RegixClient regixClient;

    public RegixGraoService(RegixClient regixClient) {
        this.regixClient = regixClient;
    }

    public PersonDataResponseType personDataSearch(String egn) {
        PersonDataRequestType personDataRequest = new PersonDataRequestType();
        personDataRequest.setEGN(egn);
        ServiceRequestData requestData = RegixClient.createRequestData(GraoOperation.PERSON_DATA_SEARCH, personDataRequest);
        CallContext ctx = new CallContext();
        requestData.setCallContext(ctx);
        ServiceResultData serviceResultData = regixClient.execute(requestData);
        Object object = serviceResultData.getData().getResponse().getAny();
        return (PersonDataResponseType) object;
    }

    public MaritalStatusResponseType maritalStatusSearch(String egn) {
        MaritalStatusRequestType maritalStatusRequest = new MaritalStatusRequestType();
        maritalStatusRequest.setEGN(egn);
        ServiceRequestData requestData = RegixClient.createRequestData(GraoOperation.MARITAL_STATUS_SEARCH, maritalStatusRequest);
        CallContext ctx = new CallContext();
        requestData.setCallContext(ctx);
        ServiceResultData serviceResultData = regixClient.execute(requestData);
        Object object = serviceResultData.getData().getResponse().getAny();
        return (MaritalStatusResponseType) object;
    }

    public MaritalStatusSpouseChildrenResponseType maritalStatusSpouseChildrenSearch(String egn) {
        MaritalStatusSpouseChildrenRequestType maritalStatusSpouseChildrenRequest = new MaritalStatusSpouseChildrenRequestType();
        maritalStatusSpouseChildrenRequest.setEGN(egn);
        ServiceRequestData requestData = RegixClient.createRequestData(GraoOperation.MARITAL_STATUS_SPOUSE_CHILDREN_SEARCH, maritalStatusSpouseChildrenRequest);
        CallContext ctx = new CallContext();
        requestData.setCallContext(ctx);
        ServiceResultData serviceResultData = regixClient.execute(requestData);
        Object object = serviceResultData.getData().getResponse().getAny();
        return (MaritalStatusSpouseChildrenResponseType) object;
    }

    public PermanentAddressResponseType permanentAddressSearch(String egn, String searchDate) throws Exception {
        PermanentAddressRequestType permanentAddressRequest = new PermanentAddressRequestType();
        permanentAddressRequest.setEGN(egn);
        XMLGregorianCalendar searchDateXML = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(searchDate);
        permanentAddressRequest.setSearchDate(searchDateXML);
        ServiceRequestData requestData = RegixClient.createRequestData(GraoOperation.PERMANENT_ADDRESS_SEARCH, permanentAddressRequest);
        CallContext ctx = new CallContext();
        requestData.setCallContext(ctx);
        ServiceResultData serviceResultData = regixClient.execute(requestData);
        Object object = serviceResultData.getData().getResponse().getAny();
        return (PermanentAddressResponseType) object;
    }


    public RelationsResponseType relationsSearch(String egn) {
        RelationsRequestType relationsRequest = new RelationsRequestType();
        relationsRequest.setEGN(egn);
        ServiceRequestData requestData = RegixClient.createRequestData(GraoOperation.RELATIONS_SEARCH, relationsRequest);
        CallContext ctx = new CallContext();
        requestData.setCallContext(ctx);
        ServiceResultData serviceResultData = regixClient.execute(requestData);
        Object object = serviceResultData.getData().getResponse().getAny();
        return (RelationsResponseType) object;
    }

    public TemporaryAddressResponseType temporaryAddressSearch(String egn, String searchDate) throws Exception {
        TemporaryAddressRequestType temporaryAddressRequest = new TemporaryAddressRequestType();
        temporaryAddressRequest.setEGN(egn);
        XMLGregorianCalendar searchDateXML = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(searchDate);
        temporaryAddressRequest.setSearchDate(searchDateXML);
        ServiceRequestData requestData = RegixClient.createRequestData(GraoOperation.TEMPORARY_ADDRESS_SEARCH, temporaryAddressRequest);
        CallContext ctx = new CallContext();
        requestData.setCallContext(ctx);
        ServiceResultData serviceResultData = regixClient.execute(requestData);
        Object object = serviceResultData.getData().getResponse().getAny();
        return (TemporaryAddressResponseType) object;
    }
}
