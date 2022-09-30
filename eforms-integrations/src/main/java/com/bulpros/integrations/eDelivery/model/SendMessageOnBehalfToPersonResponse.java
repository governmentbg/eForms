
package com.bulpros.integrations.eDelivery.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &amp;lt;element name="SendMessageOnBehalfToPersonResult" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
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
    "sendMessageOnBehalfToPersonResult"
})
@XmlRootElement(name = "SendMessageOnBehalfToPersonResponse")
public class SendMessageOnBehalfToPersonResponse {

    @XmlElement(name = "SendMessageOnBehalfToPersonResult")
    @JsonProperty("SendMessageOnBehalfToPersonResult")
    protected Integer sendMessageOnBehalfToPersonResult;

    /**
     * Gets the value of the sendMessageOnBehalfToPersonResult property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSendMessageOnBehalfToPersonResult() {
        return sendMessageOnBehalfToPersonResult;
    }

    /**
     * Sets the value of the sendMessageOnBehalfToPersonResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSendMessageOnBehalfToPersonResult(Integer value) {
        this.sendMessageOnBehalfToPersonResult = value;
    }

}
