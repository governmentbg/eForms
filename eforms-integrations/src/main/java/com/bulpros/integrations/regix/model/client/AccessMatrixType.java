
package com.bulpros.integrations.regix.model.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for AccessMatrixType complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="AccessMatrixType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="HasAccess" type="{http://www.w3.org/2001/XMLSchema}boolean"/&amp;gt;
 *         &amp;lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="Properties" type="{http://egov.bg/RegiX/SignedData}ArrayOfAMPropertyType"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccessMatrixType", propOrder = {
    "hasAccess",
    "name",
    "properties"
})
@XmlSeeAlso({
    com.bulpros.integrations.regix.model.client.DataContainer.Matrix.class
})
public class AccessMatrixType {

    @XmlElement(name = "HasAccess")
    protected boolean hasAccess;
    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "Properties", required = true)
    protected ArrayOfAMPropertyType properties;

    /**
     * Gets the value of the hasAccess property.
     * 
     */
    public boolean isHasAccess() {
        return hasAccess;
    }

    /**
     * Sets the value of the hasAccess property.
     * 
     */
    public void setHasAccess(boolean value) {
        this.hasAccess = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the properties property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAMPropertyType }
     *     
     */
    public ArrayOfAMPropertyType getProperties() {
        return properties;
    }

    /**
     * Sets the value of the properties property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAMPropertyType }
     *     
     */
    public void setProperties(ArrayOfAMPropertyType value) {
        this.properties = value;
    }

}
