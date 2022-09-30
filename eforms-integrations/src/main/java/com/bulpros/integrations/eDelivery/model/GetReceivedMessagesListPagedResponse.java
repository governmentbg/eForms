
package com.bulpros.integrations.eDelivery.model;

import javax.xml.bind.JAXBElement;
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
 *         &amp;lt;element name="GetReceivedMessagesListPagedResult" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}DcPartialListOfDcMessageHR29gRRX" minOccurs="0"/&amp;gt;
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
    "getReceivedMessagesListPagedResult"
})
@XmlRootElement(name = "GetReceivedMessagesListPagedResponse")
public class GetReceivedMessagesListPagedResponse {

    public GetReceivedMessagesListPagedResponse() {
    }

    @XmlElement(name = "GetReceivedMessagesListPagedResult")
    protected DcPartialListOfDcMessageHR29GRRX getReceivedMessagesListPagedResult;

    /**
     * Gets the value of the getReceivedMessagesListPagedResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DcPartialListOfDcMessageHR29GRRX }{@code >}
     *     
     */
    public DcPartialListOfDcMessageHR29GRRX getGetReceivedMessagesListPagedResult() {
        return getReceivedMessagesListPagedResult;
    }

    /**
     * Sets the value of the getReceivedMessagesListPagedResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DcPartialListOfDcMessageHR29GRRX }{@code >}
     *     
     */
    public void setGetReceivedMessagesListPagedResult(DcPartialListOfDcMessageHR29GRRX value) {
        this.getReceivedMessagesListPagedResult = value;
    }

}
