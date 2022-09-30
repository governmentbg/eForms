
package com.bulpros.integrations.eDelivery.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * &lt;p&gt;Java class for WebInstitutionInfo complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="WebInstitutionInfo"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;extension base="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject}DcInstitutionInfo"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="AdditionalDcouments" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}ArrayOfDcDocumentAdditional" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="DateDeleted" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="IsReadOnly" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="IsSendMessageWithCodeEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="RegistrationDocument" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}DcDocument" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/extension&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WebInstitutionInfo", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", propOrder = {
    "additionalDcouments",
    "dateDeleted",
    "isReadOnly",
    "isSendMessageWithCodeEnabled",
    "registrationDocument",
    "type"
})
public class WebInstitutionInfo
    extends DcInstitutionInfo
{

    @XmlElementRef(name = "AdditionalDcouments", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfDcDocumentAdditional> additionalDcouments;
    @XmlElementRef(name = "DateDeleted", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> dateDeleted;
    @XmlElement(name = "IsReadOnly")
    protected Boolean isReadOnly;
    @XmlElement(name = "IsSendMessageWithCodeEnabled")
    protected Boolean isSendMessageWithCodeEnabled;
    @XmlElementRef(name = "RegistrationDocument", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", type = JAXBElement.class, required = false)
    protected JAXBElement<DcDocument> registrationDocument;
    @XmlElement(name = "Type")
    protected Integer type;

    /**
     * Gets the value of the additionalDcouments property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfDcDocumentAdditional }{@code >}
     *     
     */
    public JAXBElement<ArrayOfDcDocumentAdditional> getAdditionalDcouments() {
        return additionalDcouments;
    }

    /**
     * Sets the value of the additionalDcouments property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfDcDocumentAdditional }{@code >}
     *     
     */
    public void setAdditionalDcouments(JAXBElement<ArrayOfDcDocumentAdditional> value) {
        this.additionalDcouments = value;
    }

    /**
     * Gets the value of the dateDeleted property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getDateDeleted() {
        return dateDeleted;
    }

    /**
     * Sets the value of the dateDeleted property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setDateDeleted(JAXBElement<XMLGregorianCalendar> value) {
        this.dateDeleted = value;
    }

    /**
     * Gets the value of the isReadOnly property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsReadOnly() {
        return isReadOnly;
    }

    /**
     * Sets the value of the isReadOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsReadOnly(Boolean value) {
        this.isReadOnly = value;
    }

    /**
     * Gets the value of the isSendMessageWithCodeEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsSendMessageWithCodeEnabled() {
        return isSendMessageWithCodeEnabled;
    }

    /**
     * Sets the value of the isSendMessageWithCodeEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsSendMessageWithCodeEnabled(Boolean value) {
        this.isSendMessageWithCodeEnabled = value;
    }

    /**
     * Gets the value of the registrationDocument property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DcDocument }{@code >}
     *     
     */
    public JAXBElement<DcDocument> getRegistrationDocument() {
        return registrationDocument;
    }

    /**
     * Sets the value of the registrationDocument property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DcDocument }{@code >}
     *     
     */
    public void setRegistrationDocument(JAXBElement<DcDocument> value) {
        this.registrationDocument = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setType(Integer value) {
        this.type = value;
    }

}
