
package com.bulpros.integrations.regix.model.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for CallContext complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="CallContext"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="ServiceURI" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="ServiceType" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="EmployeeIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="EmployeeNames" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="EmployeeAditionalIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="EmployeePosition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="AdministrationOId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="AdministrationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="ResponsiblePersonIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="LawReason" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="Remark" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CallContext", propOrder = {
    "serviceURI",
    "serviceType",
    "employeeIdentifier",
    "employeeNames",
    "employeeAditionalIdentifier",
    "employeePosition",
    "administrationOId",
    "administrationName",
    "responsiblePersonIdentifier",
    "lawReason",
    "remark"
})
public class CallContext {

    @XmlElement(name = "ServiceURI", required = true)
    protected String serviceURI;
    @XmlElement(name = "ServiceType", required = true)
    protected String serviceType;
    @XmlElementRef(name = "EmployeeIdentifier", namespace = "http://egov.bg/RegiX/SignedData", type = JAXBElement.class, required = false)
    protected JAXBElement<String> employeeIdentifier;
    @XmlElementRef(name = "EmployeeNames", namespace = "http://egov.bg/RegiX/SignedData", type = JAXBElement.class, required = false)
    protected JAXBElement<String> employeeNames;
    @XmlElementRef(name = "EmployeeAditionalIdentifier", namespace = "http://egov.bg/RegiX/SignedData", type = JAXBElement.class, required = false)
    protected JAXBElement<String> employeeAditionalIdentifier;
    @XmlElementRef(name = "EmployeePosition", namespace = "http://egov.bg/RegiX/SignedData", type = JAXBElement.class, required = false)
    protected JAXBElement<String> employeePosition;
    @XmlElementRef(name = "AdministrationOId", namespace = "http://egov.bg/RegiX/SignedData", type = JAXBElement.class, required = false)
    protected JAXBElement<String> administrationOId;
    @XmlElementRef(name = "AdministrationName", namespace = "http://egov.bg/RegiX/SignedData", type = JAXBElement.class, required = false)
    protected JAXBElement<String> administrationName;
    @XmlElementRef(name = "ResponsiblePersonIdentifier", namespace = "http://egov.bg/RegiX/SignedData", type = JAXBElement.class, required = false)
    protected JAXBElement<String> responsiblePersonIdentifier;
    @XmlElement(name = "LawReason", required = true)
    protected String lawReason;
    @XmlElementRef(name = "Remark", namespace = "http://egov.bg/RegiX/SignedData", type = JAXBElement.class, required = false)
    protected JAXBElement<String> remark;

    /**
     * Gets the value of the serviceURI property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceURI() {
        return serviceURI;
    }

    /**
     * Sets the value of the serviceURI property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceURI(String value) {
        this.serviceURI = value;
    }

    /**
     * Gets the value of the serviceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Sets the value of the serviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceType(String value) {
        this.serviceType = value;
    }

    /**
     * Gets the value of the employeeIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEmployeeIdentifier() {
        return employeeIdentifier;
    }

    /**
     * Sets the value of the employeeIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEmployeeIdentifier(JAXBElement<String> value) {
        this.employeeIdentifier = value;
    }

    /**
     * Gets the value of the employeeNames property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEmployeeNames() {
        return employeeNames;
    }

    /**
     * Sets the value of the employeeNames property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEmployeeNames(JAXBElement<String> value) {
        this.employeeNames = value;
    }

    /**
     * Gets the value of the employeeAditionalIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEmployeeAditionalIdentifier() {
        return employeeAditionalIdentifier;
    }

    /**
     * Sets the value of the employeeAditionalIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEmployeeAditionalIdentifier(JAXBElement<String> value) {
        this.employeeAditionalIdentifier = value;
    }

    /**
     * Gets the value of the employeePosition property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEmployeePosition() {
        return employeePosition;
    }

    /**
     * Sets the value of the employeePosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEmployeePosition(JAXBElement<String> value) {
        this.employeePosition = value;
    }

    /**
     * Gets the value of the administrationOId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAdministrationOId() {
        return administrationOId;
    }

    /**
     * Sets the value of the administrationOId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAdministrationOId(JAXBElement<String> value) {
        this.administrationOId = value;
    }

    /**
     * Gets the value of the administrationName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAdministrationName() {
        return administrationName;
    }

    /**
     * Sets the value of the administrationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAdministrationName(JAXBElement<String> value) {
        this.administrationName = value;
    }

    /**
     * Gets the value of the responsiblePersonIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getResponsiblePersonIdentifier() {
        return responsiblePersonIdentifier;
    }

    /**
     * Sets the value of the responsiblePersonIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setResponsiblePersonIdentifier(JAXBElement<String> value) {
        this.responsiblePersonIdentifier = value;
    }

    /**
     * Gets the value of the lawReason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLawReason() {
        return lawReason;
    }

    /**
     * Sets the value of the lawReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLawReason(String value) {
        this.lawReason = value;
    }

    /**
     * Gets the value of the remark property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRemark() {
        return remark;
    }

    /**
     * Sets the value of the remark property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRemark(JAXBElement<String> value) {
        this.remark = value;
    }

}
