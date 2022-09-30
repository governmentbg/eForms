
package com.bulpros.integrations.regix.model.client;

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
 *         &amp;lt;element name="argument" type="{http://egov.bg/RegiX/SignedData}ServiceCheckResultWrapper"/&amp;gt;
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
    "argument"
})
@XmlRootElement(name = "AcknowledgeResultReceived", namespace = "http://egov.bg/RegiX")
public class AcknowledgeResultReceived {

    @XmlElement(namespace = "http://egov.bg/RegiX", required = true)
    protected ServiceCheckResultWrapper argument;

    /**
     * Gets the value of the argument property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceCheckResultWrapper }
     *     
     */
    public ServiceCheckResultWrapper getArgument() {
        return argument;
    }

    /**
     * Sets the value of the argument property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceCheckResultWrapper }
     *     
     */
    public void setArgument(ServiceCheckResultWrapper value) {
        this.argument = value;
    }

}
