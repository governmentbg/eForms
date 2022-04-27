
package com.bulpros.eforms.processengine.edelivery.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;


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
 *         &amp;lt;element name="SendMessageOnBehalfOfResult" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
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
    "sendMessageOnBehalfOfResult"
})
@XmlRootElement(name = "SendMessageOnBehalfOfResponse")
public class SendMessageOnBehalfOfResponse {

    @XmlElement(name = "SendMessageOnBehalfOfResult")
    @JsonProperty("SendMessageOnBehalfOfResult")
    protected Integer sendMessageOnBehalfOfResult;

    /**
     * Gets the value of the sendMessageOnBehalfOfResult property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSendMessageOnBehalfOfResult() {
        return sendMessageOnBehalfOfResult;
    }

    /**
     * Sets the value of the sendMessageOnBehalfOfResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSendMessageOnBehalfOfResult(Integer value) {
        this.sendMessageOnBehalfOfResult = value;
    }

}
