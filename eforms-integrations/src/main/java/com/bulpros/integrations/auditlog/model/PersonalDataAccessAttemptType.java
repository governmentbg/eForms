
package com.bulpros.integrations.auditlog.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PersonalDataAccessAttemptType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonalDataAccessAttemptType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://egov.bg/bes/ws/auditlog/v1}EventType">
 *       &lt;sequence>
 *         &lt;element name="PersonalDataDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SubjectID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InformationSystem" type="{http://egov.bg/bes/ws/auditlog/v1}ServiceRequesterISType" minOccurs="0"/>
 *         &lt;element name="AccessedData" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Employee" type="{http://egov.bg/bes/ws/auditlog/v1}AdmEmployeeType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonalDataAccessAttemptType", propOrder = {
    "personalDataDescription",
    "subjectID",
    "informationSystem",
    "accessedData",
    "employee"
})
public class PersonalDataAccessAttemptType
    extends EventType
{

    @XmlElement(name = "PersonalDataDescription", required = true)
    protected String personalDataDescription;
    @XmlElement(name = "SubjectID", required = true)
    protected String subjectID;
    @XmlElement(name = "InformationSystem")
    protected ServiceRequesterISType informationSystem;
    @XmlElement(name = "AccessedData", required = true)
    protected String accessedData;
    @XmlElement(name = "Employee")
    protected AdmEmployeeType employee;

    /**
     * Gets the value of the personalDataDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonalDataDescription() {
        return personalDataDescription;
    }

    /**
     * Sets the value of the personalDataDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonalDataDescription(String value) {
        this.personalDataDescription = value;
    }

    /**
     * Gets the value of the subjectID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubjectID() {
        return subjectID;
    }

    /**
     * Sets the value of the subjectID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubjectID(String value) {
        this.subjectID = value;
    }

    /**
     * Gets the value of the informationSystem property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceRequesterISType }
     *     
     */
    public ServiceRequesterISType getInformationSystem() {
        return informationSystem;
    }

    /**
     * Sets the value of the informationSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceRequesterISType }
     *     
     */
    public void setInformationSystem(ServiceRequesterISType value) {
        this.informationSystem = value;
    }

    /**
     * Gets the value of the accessedData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccessedData() {
        return accessedData;
    }

    /**
     * Sets the value of the accessedData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccessedData(String value) {
        this.accessedData = value;
    }

    /**
     * Gets the value of the employee property.
     * 
     * @return
     *     possible object is
     *     {@link AdmEmployeeType }
     *     
     */
    public AdmEmployeeType getEmployee() {
        return employee;
    }

    /**
     * Sets the value of the employee property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdmEmployeeType }
     *     
     */
    public void setEmployee(AdmEmployeeType value) {
        this.employee = value;
    }

}
