
package com.bulpros.integrations.auditlog.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ServiceRequesterISType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServiceRequesterISType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RequesterIsOID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ISName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AdminOID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AdminName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceRequesterISType", propOrder = {
    "requesterIsOID",
    "isName",
    "adminOID",
    "adminName"
})
public class ServiceRequesterISType {

    @XmlElement(name = "RequesterIsOID", required = true)
    protected String requesterIsOID;
    @XmlElement(name = "ISName", required = true)
    protected String isName;
    @XmlElement(name = "AdminOID", required = true)
    protected String adminOID;
    @XmlElement(name = "AdminName", required = true)
    protected String adminName;

    /**
     * Gets the value of the requesterIsOID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequesterIsOID() {
        return requesterIsOID;
    }

    /**
     * Sets the value of the requesterIsOID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequesterIsOID(String value) {
        this.requesterIsOID = value;
    }

    /**
     * Gets the value of the isName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISName() {
        return isName;
    }

    /**
     * Sets the value of the isName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISName(String value) {
        this.isName = value;
    }

    /**
     * Gets the value of the adminOID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdminOID() {
        return adminOID;
    }

    /**
     * Sets the value of the adminOID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdminOID(String value) {
        this.adminOID = value;
    }

    /**
     * Gets the value of the adminName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdminName() {
        return adminName;
    }

    /**
     * Sets the value of the adminName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdminName(String value) {
        this.adminName = value;
    }

}
