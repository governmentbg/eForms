//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.05.25 at 04:57:02 PM EEST 
//


package com.bulpros.integrations.regix.model.GRAO.PersonDataSearch;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for PersonDataRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonDataRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="EGN" type="{http://egov.bg/RegiX/GRAO/NBD}EGN"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonDataRequestType", namespace = "http://egov.bg/RegiX/GRAO/NBD/PersonDataRequest", propOrder = {
    "egna"
})
@XmlRootElement(name = "PersonDataRequest", namespace = "http://egov.bg/RegiX/GRAO/NBD/PersonDataRequest")
public class PersonDataRequestType {

    @XmlElement(name = "EGNa", required = true)
    protected String egna;

    /**
     * Gets the value of the egn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEGN() {
        return egna;
    }

    /**
     * Sets the value of the egn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEGN(String value) {
        this.egna = value;
    }

}