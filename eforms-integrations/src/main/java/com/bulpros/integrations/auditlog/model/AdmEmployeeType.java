
package com.bulpros.integrations.auditlog.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AdmEmployeeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AdmEmployeeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="personalID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="personalIDType" type="{http://egov.bg/bes/ws/auditlog/v1}PersonIdTypeEnum"/>
 *         &lt;element name="personNames" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="currentPosition" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="parentAdministrationOID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="parentAdministrationLegalName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdmEmployeeType", propOrder = {
    "personalID",
    "personalIDType",
    "personNames",
    "currentPosition",
    "parentAdministrationOID",
    "parentAdministrationLegalName"
})
public class AdmEmployeeType {

    @XmlElement(required = true)
    protected String personalID;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected PersonIdTypeEnum personalIDType;
    @XmlElement(required = true)
    protected String personNames;
    @XmlElement(required = true)
    protected String currentPosition;
    @XmlElement(required = true)
    protected String parentAdministrationOID;
    @XmlElement(required = true)
    protected String parentAdministrationLegalName;

    /**
     * Gets the value of the personalID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonalID() {
        return personalID;
    }

    /**
     * Sets the value of the personalID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonalID(String value) {
        this.personalID = value;
    }

    /**
     * Gets the value of the personalIDType property.
     * 
     * @return
     *     possible object is
     *     {@link PersonIdTypeEnum }
     *     
     */
    public PersonIdTypeEnum getPersonalIDType() {
        return personalIDType;
    }

    /**
     * Sets the value of the personalIDType property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonIdTypeEnum }
     *     
     */
    public void setPersonalIDType(PersonIdTypeEnum value) {
        this.personalIDType = value;
    }

    /**
     * Gets the value of the personNames property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonNames() {
        return personNames;
    }

    /**
     * Sets the value of the personNames property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonNames(String value) {
        this.personNames = value;
    }

    /**
     * Gets the value of the currentPosition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Sets the value of the currentPosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentPosition(String value) {
        this.currentPosition = value;
    }

    /**
     * Gets the value of the parentAdministrationOID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentAdministrationOID() {
        return parentAdministrationOID;
    }

    /**
     * Sets the value of the parentAdministrationOID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentAdministrationOID(String value) {
        this.parentAdministrationOID = value;
    }

    /**
     * Gets the value of the parentAdministrationLegalName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentAdministrationLegalName() {
        return parentAdministrationLegalName;
    }

    /**
     * Sets the value of the parentAdministrationLegalName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentAdministrationLegalName(String value) {
        this.parentAdministrationLegalName = value;
    }

}
