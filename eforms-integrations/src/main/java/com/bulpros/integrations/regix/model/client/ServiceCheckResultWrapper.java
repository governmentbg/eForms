
package com.bulpros.integrations.regix.model.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for ServiceCheckResultWrapper complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="ServiceCheckResultWrapper"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="ServiceCheckResultArgument" type="{http://egov.bg/RegiX/SignedData}ServiceCheckResultArgument"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceCheckResultWrapper", propOrder = {
    "serviceCheckResultArgument"
})
public class ServiceCheckResultWrapper {

    @XmlElement(name = "ServiceCheckResultArgument", required = true)
    protected ServiceCheckResultArgument serviceCheckResultArgument;

    /**
     * Gets the value of the serviceCheckResultArgument property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceCheckResultArgument }
     *     
     */
    public ServiceCheckResultArgument getServiceCheckResultArgument() {
        return serviceCheckResultArgument;
    }

    /**
     * Sets the value of the serviceCheckResultArgument property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceCheckResultArgument }
     *     
     */
    public void setServiceCheckResultArgument(ServiceCheckResultArgument value) {
        this.serviceCheckResultArgument = value;
    }

}
