
package com.bulpros.integrations.regix.model.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for RequestWrapper complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="RequestWrapper"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="ServiceRequestData" type="{http://egov.bg/RegiX/SignedData}ServiceRequestData"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestWrapper", propOrder = {
    "serviceRequestData"
})
public class RequestWrapper {

    @XmlElement(name = "ServiceRequestData", required = true)
    protected ServiceRequestData serviceRequestData;

    /**
     * Gets the value of the serviceRequestData property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceRequestData }
     *     
     */
    public ServiceRequestData getServiceRequestData() {
        return serviceRequestData;
    }

    /**
     * Sets the value of the serviceRequestData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceRequestData }
     *     
     */
    public void setServiceRequestData(ServiceRequestData value) {
        this.serviceRequestData = value;
    }

}
