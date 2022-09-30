
package com.bulpros.integrations.eDelivery.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for DcStatisticsGeneral complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="DcStatisticsGeneral"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="NumberOfLogins" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="NumberOfRegisteredAdministrations" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="NumberOfRegisteredLegalPerson" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="NumberOfRegisteredSocialOrganisations" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="NumberOfSentMessage" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="NumberOfSentMessage10days" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="NumberOfSentMessage30days" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="NumberOfSentMessageToday" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DcStatisticsGeneral", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", propOrder = {
    "numberOfLogins",
    "numberOfRegisteredAdministrations",
    "numberOfRegisteredLegalPerson",
    "numberOfRegisteredSocialOrganisations",
    "numberOfSentMessage",
    "numberOfSentMessage10Days",
    "numberOfSentMessage30Days",
    "numberOfSentMessageToday"
})
public class DcStatisticsGeneral {

    @XmlElement(name = "NumberOfLogins")
    protected Integer numberOfLogins;
    @XmlElement(name = "NumberOfRegisteredAdministrations")
    protected Integer numberOfRegisteredAdministrations;
    @XmlElement(name = "NumberOfRegisteredLegalPerson")
    protected Integer numberOfRegisteredLegalPerson;
    @XmlElement(name = "NumberOfRegisteredSocialOrganisations")
    protected Integer numberOfRegisteredSocialOrganisations;
    @XmlElement(name = "NumberOfSentMessage")
    protected Integer numberOfSentMessage;
    @XmlElement(name = "NumberOfSentMessage10days")
    protected Integer numberOfSentMessage10Days;
    @XmlElement(name = "NumberOfSentMessage30days")
    protected Integer numberOfSentMessage30Days;
    @XmlElement(name = "NumberOfSentMessageToday")
    protected Integer numberOfSentMessageToday;

    /**
     * Gets the value of the numberOfLogins property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfLogins() {
        return numberOfLogins;
    }

    /**
     * Sets the value of the numberOfLogins property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfLogins(Integer value) {
        this.numberOfLogins = value;
    }

    /**
     * Gets the value of the numberOfRegisteredAdministrations property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfRegisteredAdministrations() {
        return numberOfRegisteredAdministrations;
    }

    /**
     * Sets the value of the numberOfRegisteredAdministrations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfRegisteredAdministrations(Integer value) {
        this.numberOfRegisteredAdministrations = value;
    }

    /**
     * Gets the value of the numberOfRegisteredLegalPerson property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfRegisteredLegalPerson() {
        return numberOfRegisteredLegalPerson;
    }

    /**
     * Sets the value of the numberOfRegisteredLegalPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfRegisteredLegalPerson(Integer value) {
        this.numberOfRegisteredLegalPerson = value;
    }

    /**
     * Gets the value of the numberOfRegisteredSocialOrganisations property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfRegisteredSocialOrganisations() {
        return numberOfRegisteredSocialOrganisations;
    }

    /**
     * Sets the value of the numberOfRegisteredSocialOrganisations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfRegisteredSocialOrganisations(Integer value) {
        this.numberOfRegisteredSocialOrganisations = value;
    }

    /**
     * Gets the value of the numberOfSentMessage property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfSentMessage() {
        return numberOfSentMessage;
    }

    /**
     * Sets the value of the numberOfSentMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfSentMessage(Integer value) {
        this.numberOfSentMessage = value;
    }

    /**
     * Gets the value of the numberOfSentMessage10Days property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfSentMessage10Days() {
        return numberOfSentMessage10Days;
    }

    /**
     * Sets the value of the numberOfSentMessage10Days property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfSentMessage10Days(Integer value) {
        this.numberOfSentMessage10Days = value;
    }

    /**
     * Gets the value of the numberOfSentMessage30Days property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfSentMessage30Days() {
        return numberOfSentMessage30Days;
    }

    /**
     * Sets the value of the numberOfSentMessage30Days property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfSentMessage30Days(Integer value) {
        this.numberOfSentMessage30Days = value;
    }

    /**
     * Gets the value of the numberOfSentMessageToday property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfSentMessageToday() {
        return numberOfSentMessageToday;
    }

    /**
     * Sets the value of the numberOfSentMessageToday property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfSentMessageToday(Integer value) {
        this.numberOfSentMessageToday = value;
    }

}
