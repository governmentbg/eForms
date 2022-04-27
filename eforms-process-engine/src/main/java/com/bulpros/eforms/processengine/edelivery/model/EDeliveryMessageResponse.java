
package com.bulpros.eforms.processengine.edelivery.model;

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
 *         &amp;lt;element name="SendMessageResult" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
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
    "sendMessageResult"
})
@XmlRootElement(name = "SendMessageResponse")
public class EDeliveryMessageResponse {

    @XmlElement(name = "SendMessageResult")
    protected Integer sendMessageResult;

    /**
     * Gets the value of the sendMessageResult property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSendMessageResult() {
        return sendMessageResult;
    }

    /**
     * Sets the value of the sendMessageResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSendMessageResult(Integer value) {
        this.sendMessageResult = value;
    }

}
