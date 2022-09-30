
package com.bulpros.integrations.eDelivery.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * &lt;p&gt;Java class for DcMessage complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="DcMessage"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="DateCreated" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="DateReceived" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="DateSent" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="IsDraft" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="ReceiverLogin" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}DcLogin" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="ReceiverProfile" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}DcProfile" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="SenderLogin" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}DcLogin" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="SenderProfile" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}DcProfile" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="Title" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DcMessage", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", propOrder = {
    "dateCreated",
    "dateReceived",
    "dateSent",
    "id",
    "isDraft",
    "receiverLogin",
    "receiverProfile",
    "senderLogin",
    "senderProfile",
    "title"
})
@XmlSeeAlso({
    DcMessageDetails.class
})
public class DcMessage {

    @XmlElement(name = "DateCreated", nillable = true)
    @XmlSchemaType(name = "dateTime")
    @JsonProperty("DateCreated")
    protected XMLGregorianCalendar dateCreated;
    @XmlElement(name = "DateReceived", nillable = true)
    @JsonProperty("DateReceived")
    protected XMLGregorianCalendar dateReceived;
    @XmlElement(name = "DateSent", nillable = true)
    @JsonProperty("DateSent")
    protected XMLGregorianCalendar dateSent;
    @XmlElement(name = "Id")
    @JsonProperty("Id")
    protected Integer id;
    @XmlElement(name = "IsDraft")
    @JsonProperty("IsDraft")
    protected Boolean isDraft;
    @XmlElement(name = "ReceiverLogin")
    @JsonProperty("ReceiverLogin")
    protected DcLogin receiverLogin;
    @XmlElement(name = "ReceiverProfile")
    @JsonProperty("ReceiverProfile")
    protected DcProfile receiverProfile;
    @XmlElement(name = "SenderLogin")
    @JsonProperty("SenderLogin")
    protected DcLogin senderLogin;
    @XmlElement(name = "SenderProfile")
    @JsonProperty("SenderProfile")
    protected DcProfile senderProfile;
    @XmlElement(name = "Title")
    @JsonProperty("Title")
    protected String title;

    /**
     * Gets the value of the dateCreated property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateCreated() {
        return dateCreated;
    }

    /**
     * Sets the value of the dateCreated property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateCreated(XMLGregorianCalendar value) {
        this.dateCreated = value;
    }

    /**
     * Gets the value of the dateReceived property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public XMLGregorianCalendar getDateReceived() {
        return dateReceived;
    }

    /**
     * Sets the value of the dateReceived property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setDateReceived(XMLGregorianCalendar value) {
        this.dateReceived = value;
    }

    /**
     * Gets the value of the dateSent property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public XMLGregorianCalendar getDateSent() {
        return dateSent;
    }

    /**
     * Sets the value of the dateSent property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setDateSent(XMLGregorianCalendar value) {
        this.dateSent = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Gets the value of the isDraft property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsDraft() {
        return isDraft;
    }

    /**
     * Sets the value of the isDraft property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsDraft(Boolean value) {
        this.isDraft = value;
    }

    /**
     * Gets the value of the receiverLogin property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DcLogin }{@code >}
     *     
     */
    public DcLogin getReceiverLogin() {
        return receiverLogin;
    }

    /**
     * Sets the value of the receiverLogin property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DcLogin }{@code >}
     *     
     */
    public void setReceiverLogin(DcLogin value) {
        this.receiverLogin = value;
    }

    /**
     * Gets the value of the receiverProfile property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DcProfile }{@code >}
     *     
     */
    public DcProfile getReceiverProfile() {
        return receiverProfile;
    }

    /**
     * Sets the value of the receiverProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DcProfile }{@code >}
     *     
     */
    public void setReceiverProfile(DcProfile value) {
        this.receiverProfile = value;
    }

    /**
     * Gets the value of the senderLogin property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DcLogin }{@code >}
     *     
     */
    public DcLogin getSenderLogin() {
        return senderLogin;
    }

    /**
     * Sets the value of the senderLogin property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DcLogin }{@code >}
     *     
     */
    public void setSenderLogin(DcLogin value) {
        this.senderLogin = value;
    }

    /**
     * Gets the value of the senderProfile property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DcProfile }{@code >}
     *     
     */
    public DcProfile getSenderProfile() {
        return senderProfile;
    }

    /**
     * Sets the value of the senderProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DcProfile }{@code >}
     *     
     */
    public void setSenderProfile(DcProfile value) {
        this.senderProfile = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

}
