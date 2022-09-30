
package com.bulpros.integrations.eDelivery.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;


/**
 * &lt;p&gt;Java class for DcSubjectPublicInfo complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="DcSubjectPublicInfo"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="ElectronicSubjectId" type="{http://schemas.microsoft.com/2003/10/Serialization/}guid" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="ElectronicSubjectName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="Email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="IsActivated" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="PhoneNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="ProfileType" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums}eProfileType" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DcSubjectPublicInfo", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", propOrder = {
        "electronicSubjectId",
        "electronicSubjectName",
        "email",
        "isActivated",
        "phoneNumber",
        "profileType"
})
@XmlSeeAlso({
        DcSubjectInfo.class,
        DcRegisteredSubjectInfo.class
})
public class DcSubjectPublicInfo {

    @XmlElement(name = "ElectronicSubjectId")
    protected String electronicSubjectId;
    @XmlElement(name = "ElectronicSubjectName")
    protected String electronicSubjectName;
    @XmlElement(name = "Email")
    protected String email;
    @XmlElement(name = "IsActivated")
    protected Boolean isActivated;
    @XmlElement(name = "PhoneNumber")
    protected String phoneNumber;
    @XmlElement(name = "ProfileType")
    @XmlSchemaType(name = "string")
    protected EProfileType profileType;

    /**
     * Gets the value of the electronicSubjectId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getElectronicSubjectId() {
        return electronicSubjectId;
    }

    /**
     * Sets the value of the electronicSubjectId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setElectronicSubjectId(String value) {
        this.electronicSubjectId = value;
    }

    /**
     * Gets the value of the electronicSubjectName property.
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public String getElectronicSubjectName() {
        return electronicSubjectName;
    }

    /**
     * Sets the value of the electronicSubjectName property.
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setElectronicSubjectName(String value) {
        this.electronicSubjectName = value;
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
     * Gets the value of the isActivated property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isIsActivated() {
        return isActivated;
    }

    /**
     * Sets the value of the isActivated property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setIsActivated(Boolean value) {
        this.isActivated = value;
    }

    /**
     * Gets the value of the phoneNumber property.
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the value of the phoneNumber property.
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setPhoneNumber(String value) {
        this.phoneNumber = value;
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
