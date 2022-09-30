
package com.bulpros.integrations.regix.model.client;

import com.bulpros.integrations.regix.model.AV.TR.GetActualState.ActualStateRequestType;
import com.bulpros.integrations.regix.model.AV.TR.GetActualState.ActualStateResponseType;
import com.bulpros.integrations.regix.model.AZ.GetJobSeekerStatus.JobSeekerStatusData;
import com.bulpros.integrations.regix.model.AZ.GetJobSeekerStatus.JobSeekerStatusRequestType;
import com.bulpros.integrations.regix.model.GRAO.MaritalStatusSearch.MaritalStatusRequestType;
import com.bulpros.integrations.regix.model.GRAO.MaritalStatusSearch.MaritalStatusResponseType;
import com.bulpros.integrations.regix.model.GRAO.MaritalStatusSpouseChildrenSearch.MaritalStatusSpouseChildrenRequestType;
import com.bulpros.integrations.regix.model.GRAO.MaritalStatusSpouseChildrenSearch.MaritalStatusSpouseChildrenResponseType;
import com.bulpros.integrations.regix.model.GRAO.PermanentAddressSearch.PermanentAddressRequestType;
import com.bulpros.integrations.regix.model.GRAO.PermanentAddressSearch.PermanentAddressResponseType;
import com.bulpros.integrations.regix.model.GRAO.PersonDataSearch.PersonDataRequestType;
import com.bulpros.integrations.regix.model.GRAO.PersonDataSearch.PersonDataResponseType;
import com.bulpros.integrations.regix.model.GRAO.RelationsSearch.RelationsRequestType;
import com.bulpros.integrations.regix.model.GRAO.RelationsSearch.RelationsResponseType;
import com.bulpros.integrations.regix.model.GRAO.TemporaryAddressSearch.TemporaryAddressRequestType;
import com.bulpros.integrations.regix.model.GRAO.TemporaryAddressSearch.TemporaryAddressResponseType;
import com.bulpros.integrations.regix.model.MON.GetChildStudentStatus.ChildStudentStatusRequestType;
import com.bulpros.integrations.regix.model.MON.GetChildStudentStatus.ChildStudentStatusResponse;
import com.bulpros.integrations.regix.model.MON.GetHigherEduStudentByStatus.HigherEduStudentByStatusRequestType;
import com.bulpros.integrations.regix.model.MON.GetHigherEduStudentByStatus.HigherEduStudentByStatusResponse;
import com.bulpros.integrations.regix.model.MVR.GetMotorVehicleRegistrationInfo.MotorVehicleRegistrationRequestType;
import com.bulpros.integrations.regix.model.MVR.GetMotorVehicleRegistrationInfo.MotorVehicleRegistrationResponseType;
import com.bulpros.integrations.regix.model.MVR.GetPersonalIdentityV3.PersonalIdentityInfoRequestType;
import com.bulpros.integrations.regix.model.MVR.GetPersonalIdentityV3.PersonalIdentityInfoResponseType;
import com.bulpros.integrations.regix.model.NELK.GetAllExpertDecisionsByIdentifier.ExpertDecisionsResponse;
import com.bulpros.integrations.regix.model.NELK.GetAllExpertDecisionsByIdentifier.GetAllExpertDecisionsByIdentifierRequest;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;


/**
 * &lt;p&gt;Java class for ServiceRequestData complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="ServiceRequestData"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="RequestProcessing" type="{http://egov.bg/RegiX/SignedData}RequestProcessing"/&amp;gt;
 *         &amp;lt;element name="ResponseProcessing" type="{http://egov.bg/RegiX/SignedData}ResponseProcessing"/&amp;gt;
 *         &amp;lt;element name="Operation" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="Argument"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;any/&amp;gt;
 *                 &amp;lt;/sequence&amp;gt;
 *               &amp;lt;/restriction&amp;gt;
 *             &amp;lt;/complexContent&amp;gt;
 *           &amp;lt;/complexType&amp;gt;
 *         &amp;lt;/element&amp;gt;
 *         &amp;lt;element name="EIDToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="CallContext" type="{http://egov.bg/RegiX/SignedData}CallContext"/&amp;gt;
 *         &amp;lt;element name="CallbackURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="EmployeeEGN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="CitizenEGN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="SignResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/&amp;gt;
 *         &amp;lt;element name="ReturnAccessMatrix" type="{http://www.w3.org/2001/XMLSchema}boolean"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceRequestData", propOrder = {
    "requestProcessing",
    "responseProcessing",
    "operation",
    "argument",
    "eidToken",
    "callContext",
    "callbackURL",
    "employeeEGN",
    "citizenEGN",
    "signResult",
    "returnAccessMatrix"
})
@XmlSeeAlso({
        PersonDataRequestType.class,
        PersonDataResponseType.class,
        ActualStateRequestType.class,
        ActualStateResponseType.class,
        JobSeekerStatusRequestType.class,
        JobSeekerStatusData.class,
        MaritalStatusRequestType.class,
        MaritalStatusResponseType.class,
        MaritalStatusSpouseChildrenRequestType.class,
        MaritalStatusSpouseChildrenResponseType.class,
        PermanentAddressRequestType.class,
        PermanentAddressResponseType.class,
        RelationsRequestType.class,
        RelationsResponseType.class,
        TemporaryAddressRequestType.class,
        TemporaryAddressResponseType.class,
        ChildStudentStatusRequestType.class,
        ChildStudentStatusResponse.class,
        HigherEduStudentByStatusRequestType.class,
        HigherEduStudentByStatusResponse.class,
        MotorVehicleRegistrationRequestType.class,
        MotorVehicleRegistrationResponseType.class,
        GetAllExpertDecisionsByIdentifierRequest.class,
        ExpertDecisionsResponse.class,
        PersonalIdentityInfoRequestType.class,
        PersonalIdentityInfoResponseType.class
})
public class ServiceRequestData {

    @XmlList
    @XmlElement(name = "RequestProcessing", required = true)
    protected List<String> requestProcessing;
    @XmlList
    @XmlElement(name = "ResponseProcessing", required = true)
    protected List<String> responseProcessing;
    @XmlElement(name = "Operation", required = true)
    protected String operation;
    @XmlElement(name = "Argument", required = true)
    protected ServiceRequestData.Argument argument;
    @XmlElementRef(name = "EIDToken", namespace = "http://egov.bg/RegiX/SignedData", type = JAXBElement.class, required = false)
    protected JAXBElement<String> eidToken;
    @XmlElement(name = "CallContext", required = true)
    protected CallContext callContext;
    @XmlElementRef(name = "CallbackURL", namespace = "http://egov.bg/RegiX/SignedData", type = JAXBElement.class, required = false)
    protected JAXBElement<String> callbackURL;
    @XmlElementRef(name = "EmployeeEGN", namespace = "http://egov.bg/RegiX/SignedData", type = JAXBElement.class, required = false)
    protected JAXBElement<String> employeeEGN;
    @XmlElementRef(name = "CitizenEGN", namespace = "http://egov.bg/RegiX/SignedData", type = JAXBElement.class, required = false)
    protected JAXBElement<String> citizenEGN;
    @XmlElement(name = "SignResult")
    protected boolean signResult;
    @XmlElement(name = "ReturnAccessMatrix")
    protected boolean returnAccessMatrix;

    /**
     * Gets the value of the requestProcessing property.
     * 
     * &lt;p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the requestProcessing property.
     * 
     * &lt;p&gt;
     * For example, to add a new item, do as follows:
     * &lt;pre&gt;
     *    getRequestProcessing().add(newItem);
     * &lt;/pre&gt;
     * 
     * 
     * &lt;p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getRequestProcessing() {
        if (requestProcessing == null) {
            requestProcessing = new ArrayList<String>();
        }
        return this.requestProcessing;
    }

    /**
     * Gets the value of the responseProcessing property.
     * 
     * &lt;p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the responseProcessing property.
     * 
     * &lt;p&gt;
     * For example, to add a new item, do as follows:
     * &lt;pre&gt;
     *    getResponseProcessing().add(newItem);
     * &lt;/pre&gt;
     * 
     * 
     * &lt;p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getResponseProcessing() {
        if (responseProcessing == null) {
            responseProcessing = new ArrayList<String>();
        }
        return this.responseProcessing;
    }

    /**
     * Gets the value of the operation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Sets the value of the operation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperation(String value) {
        this.operation = value;
    }

    /**
     * Gets the value of the argument property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceRequestData.Argument }
     *     
     */
    public ServiceRequestData.Argument getArgument() {
        return argument;
    }

    /**
     * Sets the value of the argument property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceRequestData.Argument }
     *     
     */
    public void setArgument(ServiceRequestData.Argument value) {
        this.argument = value;
    }

    /**
     * Gets the value of the eidToken property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEIDToken() {
        return eidToken;
    }

    /**
     * Sets the value of the eidToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEIDToken(JAXBElement<String> value) {
        this.eidToken = value;
    }

    /**
     * Gets the value of the callContext property.
     * 
     * @return
     *     possible object is
     *     {@link CallContext }
     *     
     */
    public CallContext getCallContext() {
        return callContext;
    }

    /**
     * Sets the value of the callContext property.
     * 
     * @param value
     *     allowed object is
     *     {@link CallContext }
     *     
     */
    public void setCallContext(CallContext value) {
        this.callContext = value;
    }

    /**
     * Gets the value of the callbackURL property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCallbackURL() {
        return callbackURL;
    }

    /**
     * Sets the value of the callbackURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCallbackURL(JAXBElement<String> value) {
        this.callbackURL = value;
    }

    /**
     * Gets the value of the employeeEGN property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEmployeeEGN() {
        return employeeEGN;
    }

    /**
     * Sets the value of the employeeEGN property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEmployeeEGN(JAXBElement<String> value) {
        this.employeeEGN = value;
    }

    /**
     * Gets the value of the citizenEGN property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCitizenEGN() {
        return citizenEGN;
    }

    /**
     * Sets the value of the citizenEGN property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCitizenEGN(JAXBElement<String> value) {
        this.citizenEGN = value;
    }

    /**
     * Gets the value of the signResult property.
     * 
     */
    public boolean isSignResult() {
        return signResult;
    }

    /**
     * Sets the value of the signResult property.
     * 
     */
    public void setSignResult(boolean value) {
        this.signResult = value;
    }

    /**
     * Gets the value of the returnAccessMatrix property.
     * 
     */
    public boolean isReturnAccessMatrix() {
        return returnAccessMatrix;
    }

    /**
     * Sets the value of the returnAccessMatrix property.
     * 
     */
    public void setReturnAccessMatrix(boolean value) {
        this.returnAccessMatrix = value;
    }


    /**
     * &lt;p&gt;Java class for anonymous complex type.
     * 
     * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
     * 
     * &lt;pre&gt;
     * &amp;lt;complexType&amp;gt;
     *   &amp;lt;complexContent&amp;gt;
     *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
     *       &amp;lt;sequence&amp;gt;
     *         &amp;lt;any/&amp;gt;
     *       &amp;lt;/sequence&amp;gt;
     *     &amp;lt;/restriction&amp;gt;
     *   &amp;lt;/complexContent&amp;gt;
     * &amp;lt;/complexType&amp;gt;
     * &lt;/pre&gt;
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "any"
    })
    public static class Argument {

        @XmlAnyElement(lax = true)
        protected Object any;

        /**
         * Gets the value of the any property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getAny() {
            return any;
        }

        /**
         * Sets the value of the any property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setAny(Object value) {
            this.any = value;
        }

    }

}
