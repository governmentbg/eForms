
package com.bulpros.integrations.eDelivery.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;


/**
 * &lt;p&gt;Java class for DcSubjectRegistrationInfo complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="DcSubjectRegistrationInfo"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="HasRegistration" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="Identificator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="SubjectInfo" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}DcRegisteredSubjectInfo" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DcSubjectRegistrationInfo", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", propOrder = {
        "hasRegistration",
        "identificator",
        "subjectInfo"
})
@XmlRootElement(name = "DcSubjectRegistrationInfo")
public class DcSubjectRegistrationInfo {

    @XmlElement(name = "HasRegistration")
    protected Boolean hasRegistration;
    @XmlElement(name = "Identificator")
    protected String identificator;
    @XmlElement(name = "SubjectInfo")
    protected DcRegisteredSubjectInfo subjectInfo;

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
     * Gets the value of the identificator property.
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public String getIdentificator() {
        return identificator;
    }

    /**
     * Sets the value of the identificator property.
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setIdentificator(String value) {
        this.identificator = value;
    }

    /**
     * Gets the value of the subjectInfo property.
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link DcRegisteredSubjectInfo }{@code >}
     */
    public DcRegisteredSubjectInfo getSubjectInfo() {
        return subjectInfo;
    }

    /**
     * Sets the value of the subjectInfo property.
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link DcRegisteredSubjectInfo }{@code >}
     */
    public void setSubjectInfo(DcRegisteredSubjectInfo value) {
        this.subjectInfo = value;
    }

}
