
package com.bulpros.integrations.eDelivery.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for anonymous complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="message" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}DcMessageDetails" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="senderUniqueIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="receiverUniqueIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="serviceOID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="operatorEGN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "message",
    "senderUniqueIdentifier",
    "receiverUniqueIdentifier",
    "serviceOID",
    "operatorEGN"
})
@XmlRootElement(name = "SendMessageOnBehalfToLegalEntity")
public class SendMessageOnBehalfToLegalEntity {

    @XmlElementRef(name = "message", namespace = "https://edelivery.egov.bg/services/integration", type = JAXBElement.class, required = false)
    protected JAXBElement<DcMessageDetails> message;
    @XmlElementRef(name = "senderUniqueIdentifier", namespace = "https://edelivery.egov.bg/services/integration", type = JAXBElement.class, required = false)
    protected JAXBElement<String> senderUniqueIdentifier;
    @XmlElementRef(name = "receiverUniqueIdentifier", namespace = "https://edelivery.egov.bg/services/integration", type = JAXBElement.class, required = false)
    protected JAXBElement<String> receiverUniqueIdentifier;
    @XmlElementRef(name = "serviceOID", namespace = "https://edelivery.egov.bg/services/integration", type = JAXBElement.class, required = false)
    protected JAXBElement<String> serviceOID;
    @XmlElementRef(name = "operatorEGN", namespace = "https://edelivery.egov.bg/services/integration", type = JAXBElement.class, required = false)
    protected JAXBElement<String> operatorEGN;

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     *     
     */
    public JAXBElement<DcMessageDetails> getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DcMessageDetails }{@code >}
     *     
     */
    public void setMessage(JAXBElement<DcMessageDetails> value) {
        this.message = value;
    }

    /**
     * Gets the value of the senderUniqueIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSenderUniqueIdentifier() {
        return senderUniqueIdentifier;
    }

    /**
     * Sets the value of the senderUniqueIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSenderUniqueIdentifier(JAXBElement<String> value) {
        this.senderUniqueIdentifier = value;
    }

    /**
     * Gets the value of the receiverUniqueIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getReceiverUniqueIdentifier() {
        return receiverUniqueIdentifier;
    }

    /**
     * Sets the value of the receiverUniqueIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setReceiverUniqueIdentifier(JAXBElement<String> value) {
        this.receiverUniqueIdentifier = value;
    }

    /**
     * Gets the value of the serviceOID property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getServiceOID() {
        return serviceOID;
    }

    /**
     * Sets the value of the serviceOID property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setServiceOID(JAXBElement<String> value) {
        this.serviceOID = value;
    }

    /**
     * Gets the value of the operatorEGN property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOperatorEGN() {
        return operatorEGN;
    }

    /**
     * Sets the value of the operatorEGN property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOperatorEGN(JAXBElement<String> value) {
        this.operatorEGN = value;
    }

}
