
package com.bulpros.integrations.eDelivery.model;

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
 *         &amp;lt;element name="SendElectronicDocumentWithAccessCodeResult" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&amp;gt;
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
    "sendElectronicDocumentWithAccessCodeResult"
})
@XmlRootElement(name = "SendElectronicDocumentWithAccessCodeResponse")
public class SendElectronicDocumentWithAccessCodeResponse {

    @XmlElement(name = "SendElectronicDocumentWithAccessCodeResult")
    protected Integer sendElectronicDocumentWithAccessCodeResult;

    /**
     * Gets the value of the sendElectronicDocumentWithAccessCodeResult property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSendElectronicDocumentWithAccessCodeResult() {
        return sendElectronicDocumentWithAccessCodeResult;
    }

    /**
     * Sets the value of the sendElectronicDocumentWithAccessCodeResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSendElectronicDocumentWithAccessCodeResult(Integer value) {
        this.sendElectronicDocumentWithAccessCodeResult = value;
    }

}
