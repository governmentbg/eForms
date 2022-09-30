
package com.bulpros.integrations.regix.model.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import java.math.BigDecimal;


/**
 * &lt;p&gt;Java class for ServiceResultData complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="ServiceResultData"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="IsReady" type="{http://www.w3.org/2001/XMLSchema}boolean"/&amp;gt;
 * &amp;lt;element name="Data" type="{http://egov.bg/RegiX/SignedData}DataContainer" minOccurs="0"/&amp;gt;
 * &amp;lt;any minOccurs="0"/&amp;gt;
 * &amp;lt;element name="HasError" type="{http://www.w3.org/2001/XMLSchema}boolean"/&amp;gt;
 * &amp;lt;element name="Error" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="ServiceCallID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&amp;gt;
 * &amp;lt;element name="VerificationCode" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceResultData", propOrder = {
        "isReady",
        "data",
        "any",
        "hasError",
        "error",
        "serviceCallID",
        "verificationCode"
})
public class ServiceResultData {

    @XmlElement(name = "IsReady")
    protected boolean isReady;
    @XmlElement(name = "Data")
    protected DataContainer data;
    @XmlAnyElement(lax = true)
    protected Object any;
    @XmlElement(name = "HasError")
    protected boolean hasError;
    @XmlElement(name = "Error")
    protected String error;
    @XmlElement(name = "ServiceCallID", required = true)
    protected BigDecimal serviceCallID;
    @XmlElement(name = "VerificationCode", required = true)
    protected String verificationCode;

    /**
     * Gets the value of the isReady property.
     */
    public boolean isIsReady() {
        return isReady;
    }

    /**
     * Sets the value of the isReady property.
     */
    public void setIsReady(boolean value) {
        this.isReady = value;
    }

    /**
     * Gets the value of the data property.
     *
     * @return possible object is
     * {@link DataContainer }
     */
    public DataContainer getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     *
     * @param value allowed object is
     *              {@link DataContainer }
     */
    public void setData(DataContainer value) {
        this.data = value;
    }

    /**
     * Gets the value of the any property.
     *
     * @return possible object is
     * {@link Object }
     */
    public Object getAny() {
        return any;
    }

    /**
     * Sets the value of the any property.
     *
     * @param value allowed object is
     *              {@link Object }
     */
    public void setAny(Object value) {
        this.any = value;
    }

    /**
     * Gets the value of the hasError property.
     */
    public boolean isHasError() {
        return hasError;
    }

    /**
     * Sets the value of the hasError property.
     */
    public void setHasError(boolean value) {
        this.hasError = value;
    }

    /**
     * Gets the value of the error property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setError(String value) {
        this.error = value;
    }

    /**
     * Gets the value of the serviceCallID property.
     *
     * @return possible object is
     * {@link BigDecimal }
     */
    public BigDecimal getServiceCallID() {
        return serviceCallID;
    }

    /**
     * Sets the value of the serviceCallID property.
     *
     * @param value allowed object is
     *              {@link BigDecimal }
     */
    public void setServiceCallID(BigDecimal value) {
        this.serviceCallID = value;
    }

    /**
     * Gets the value of the verificationCode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVerificationCode() {
        return verificationCode;
    }

    /**
     * Sets the value of the verificationCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVerificationCode(String value) {
        this.verificationCode = value;
    }

}
