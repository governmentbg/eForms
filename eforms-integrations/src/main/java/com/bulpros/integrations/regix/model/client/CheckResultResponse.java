
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
 *         &amp;lt;element name="CheckResultResult" type="{http://egov.bg/RegiX/SignedData}ResultWrapper"/&amp;gt;
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
    "checkResultResult"
})
@XmlRootElement(name = "CheckResultResponse", namespace = "http://egov.bg/RegiX")
public class CheckResultResponse {

    @XmlElement(name = "CheckResultResult", namespace = "http://egov.bg/RegiX", required = true)
    protected ResultWrapper checkResultResult;

    /**
     * Gets the value of the checkResultResult property.
     * 
     * @return
     *     possible object is
     *     {@link ResultWrapper }
     *     
     */
    public ResultWrapper getCheckResultResult() {
        return checkResultResult;
    }

    /**
     * Sets the value of the checkResultResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultWrapper }
     *     
     */
    public void setCheckResultResult(ResultWrapper value) {
        this.checkResultResult = value;
    }

}
