
package com.bulpros.integrations.eDelivery.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for DcPersonRegistrationInfo complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="DcPersonRegistrationInfo"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="AccessibleProfiles" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}ArrayOfDcSubjectShortInfo" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="HasRegistration" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="PersonIdentificator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DcPersonRegistrationInfo", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", propOrder = {
    "accessibleProfiles",
    "hasRegistration",
    "name",
    "personIdentificator"
})
public class DcPersonRegistrationInfo {

    @XmlElement(name = "AccessibleProfiles")
    protected ArrayOfDcSubjectShortInfo accessibleProfiles;
    @XmlElement(name = "HasRegistration")
    protected Boolean hasRegistration;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "PersonIdentificator")
    protected String personIdentificator;

    /**
     * Gets the value of the accessibleProfiles property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfDcSubjectShortInfo }{@code >}
     *     
     */
    public ArrayOfDcSubjectShortInfo getAccessibleProfiles() {
        return accessibleProfiles;
    }

    /**
     * Sets the value of the accessibleProfiles property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfDcSubjectShortInfo }{@code >}
     *     
     */
    public void setAccessibleProfiles(ArrayOfDcSubjectShortInfo value) {
        this.accessibleProfiles = value;
    }

    /**
     * Gets the value of the hasRegistration property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHasRegistration() {
        return hasRegistration;
    }

    /**
     * Sets the value of the hasRegistration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHasRegistration(Boolean value) {
        this.hasRegistration = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the personIdentificator property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public String getPersonIdentificator() {
        return personIdentificator;
    }

    /**
     * Sets the value of the personIdentificator property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPersonIdentificator(String value) {
        this.personIdentificator = value;
    }

}
