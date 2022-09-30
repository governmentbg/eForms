
package com.bulpros.integrations.auditlog.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RequestedServiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RequestedServiceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ServiceOID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SPOID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SPName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ServiceName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AuthnRequesterOID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AdminiOID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AdminLegalName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestedServiceType", propOrder = {
    "serviceOID",
    "spoid",
    "spName",
    "serviceName",
    "authnRequesterOID",
    "adminiOID",
    "adminLegalName"
})
public class RequestedServiceType {

    @XmlElement(name = "ServiceOID", required = true)
    protected String serviceOID;
    @XmlElement(name = "SPOID", required = true)
    protected String spoid;
    @XmlElement(name = "SPName")
    protected String spName;
    @XmlElement(name = "ServiceName")
    protected String serviceName;
    @XmlElement(name = "AuthnRequesterOID")
    protected String authnRequesterOID;
    @XmlElement(name = "AdminiOID")
    protected String adminiOID;
    @XmlElement(name = "AdminLegalName")
    protected String adminLegalName;

    /**
     * Gets the value of the serviceOID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceOID() {
        return serviceOID;
    }

    /**
     * Sets the value of the serviceOID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceOID(String value) {
        this.serviceOID = value;
    }

    /**
     * Gets the value of the spoid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPOID() {
        return spoid;
    }

    /**
     * Sets the value of the spoid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPOID(String value) {
        this.spoid = value;
    }

    /**
     * Gets the value of the spName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPName() {
        return spName;
    }

    /**
     * Sets the value of the spName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPName(String value) {
        this.spName = value;
    }

    /**
     * Gets the value of the serviceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Sets the value of the serviceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceName(String value) {
        this.serviceName = value;
    }

    /**
     * Gets the value of the authnRequesterOID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthnRequesterOID() {
        return authnRequesterOID;
    }

    /**
     * Sets the value of the authnRequesterOID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthnRequesterOID(String value) {
        this.authnRequesterOID = value;
    }

    /**
     * Gets the value of the adminiOID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdminiOID() {
        return adminiOID;
    }

    /**
     * Sets the value of the adminiOID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdminiOID(String value) {
        this.adminiOID = value;
    }

    /**
     * Gets the value of the adminLegalName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdminLegalName() {
        return adminLegalName;
    }

    /**
     * Sets the value of the adminLegalName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdminLegalName(String value) {
        this.adminLegalName = value;
    }

}
