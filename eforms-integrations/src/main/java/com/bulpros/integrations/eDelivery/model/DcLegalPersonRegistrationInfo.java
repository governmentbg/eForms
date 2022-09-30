
package com.bulpros.integrations.eDelivery.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for DcLegalPersonRegistrationInfo complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="DcLegalPersonRegistrationInfo"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="EIK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="Email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="HasRegistration" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="Phone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="ProfilesWithAccess" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}ArrayOfDcSubjectShortInfo" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DcLegalPersonRegistrationInfo", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", propOrder = {
        "eik",
        "email",
        "hasRegistration",
        "name",
        "phone",
        "profilesWithAccess"
})
public class DcLegalPersonRegistrationInfo {

    @XmlElement(name = "EIK")
    protected String eik;
    @XmlElement(name = "Email")
    protected String email;
    @XmlElement(name = "HasRegistration")
    protected Boolean hasRegistration;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "Phone")
    protected String phone;
    @XmlElement(name = "ProfilesWithAccess")
    protected ArrayOfDcSubjectShortInfo profilesWithAccess;

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
     * Gets the value of the email property.
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the hasRegistration property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isHasRegistration() {
        return hasRegistration;
    }

    /**
     * Sets the value of the hasRegistration property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setHasRegistration(Boolean value) {
        this.hasRegistration = value;
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
     * Gets the value of the phone property.
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the value of the phone property.
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setPhone(String value) {
        this.phone = value;
    }

    /**
     * Gets the value of the profilesWithAccess property.
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link ArrayOfDcSubjectShortInfo }{@code >}
     */
    public ArrayOfDcSubjectShortInfo getProfilesWithAccess() {
        return profilesWithAccess;
    }

    /**
     * Sets the value of the profilesWithAccess property.
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link ArrayOfDcSubjectShortInfo }{@code >}
     */
    public void setProfilesWithAccess(ArrayOfDcSubjectShortInfo value) {
        this.profilesWithAccess = value;
    }

}
