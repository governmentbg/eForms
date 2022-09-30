
package com.bulpros.integrations.eDelivery.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for DcTokenVerificationInfo complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="DcTokenVerificationInfo"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="IsValid" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="Token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="VerificationInfoType" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums}eVerificationInfoType" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DcTokenVerificationInfo", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", propOrder = {
    "isValid",
    "token",
    "verificationInfoType"
})
public class DcTokenVerificationInfo {

    @XmlElement(name = "IsValid")
    protected Boolean isValid;
    @XmlElementRef(name = "Token", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", type = JAXBElement.class, required = false)
    protected JAXBElement<String> token;
    @XmlElement(name = "VerificationInfoType")
    @XmlSchemaType(name = "string")
    protected EVerificationInfoType verificationInfoType;

    /**
     * Gets the value of the isValid property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsValid() {
        return isValid;
    }

    /**
     * Sets the value of the isValid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsValid(Boolean value) {
        this.isValid = value;
    }

    /**
     * Gets the value of the token property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setToken(JAXBElement<String> value) {
        this.token = value;
    }

    /**
     * Gets the value of the verificationInfoType property.
     * 
     * @return
     *     possible object is
     *     {@link EVerificationInfoType }
     *     
     */
    public EVerificationInfoType getVerificationInfoType() {
        return verificationInfoType;
    }

    /**
     * Sets the value of the verificationInfoType property.
     * 
     * @param value
     *     allowed object is
     *     {@link EVerificationInfoType }
     *     
     */
    public void setVerificationInfoType(EVerificationInfoType value) {
        this.verificationInfoType = value;
    }

}
