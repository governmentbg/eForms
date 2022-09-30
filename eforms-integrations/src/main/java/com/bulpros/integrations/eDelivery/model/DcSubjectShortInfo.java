
package com.bulpros.integrations.eDelivery.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;


/**
 * &lt;p&gt;Java class for DcSubjectShortInfo complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="DcSubjectShortInfo"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="EGN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="EIK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="ProfileType" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums}eProfileType" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DcSubjectShortInfo", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", propOrder = {
        "egn",
        "eik",
        "name",
        "profileType"
})
public class DcSubjectShortInfo {

    @XmlElement(name = "EGN")
    protected String egn;
    @XmlElement(name = "EIK")
    protected String eik;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "ProfileType")
    @XmlSchemaType(name = "string")
    protected EProfileType profileType;

    /**
     * Gets the value of the egn property.
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public String getEGN() {
        return egn;
    }

    /**
     * Sets the value of the egn property.
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setEGN(String value) {
        this.egn = value;
    }

    /**
     * Gets the value of the eik property.
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public String getEIK() {
        return eik;
    }

    /**
     * Sets the value of the eik property.
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setEIK(String value) {
        this.eik = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the profileType property.
     *
     * @return possible object is
     * {@link EProfileType }
     */
    public EProfileType getProfileType() {
        return profileType;
    }

    /**
     * Sets the value of the profileType property.
     *
     * @param value allowed object is
     *              {@link EProfileType }
     */
    public void setProfileType(EProfileType value) {
        this.profileType = value;
    }

}
