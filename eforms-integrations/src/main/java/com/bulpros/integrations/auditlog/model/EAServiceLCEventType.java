
package com.bulpros.integrations.auditlog.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EAServiceLCEventType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EAServiceLCEventType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://egov.bg/bes/ws/auditlog/v1}EventType">
 *       &lt;sequence>
 *         &lt;element name="AuthnReqID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AuthnRespID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DocumentRegId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EAServiceLCEventType", propOrder = {
    "authnReqID",
    "authnRespID",
    "documentRegId"
})
public class EAServiceLCEventType
    extends EventType
{

    @XmlElement(name = "AuthnReqID")
    protected String authnReqID;
    @XmlElement(name = "AuthnRespID")
    protected String authnRespID;
    @XmlElement(name = "DocumentRegId")
    protected String documentRegId;

    /**
     * Gets the value of the authnReqID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthnReqID() {
        return authnReqID;
    }

    /**
     * Sets the value of the authnReqID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthnReqID(String value) {
        this.authnReqID = value;
    }

    /**
     * Gets the value of the authnRespID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthnRespID() {
        return authnRespID;
    }

    /**
     * Sets the value of the authnRespID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthnRespID(String value) {
        this.authnRespID = value;
    }

    /**
     * Gets the value of the documentRegId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentRegId() {
        return documentRegId;
    }

    /**
     * Sets the value of the documentRegId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentRegId(String value) {
        this.documentRegId = value;
    }

}
