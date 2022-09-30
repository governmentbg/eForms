
package com.bulpros.integrations.eDelivery.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;


/**
 * &lt;p&gt;Java class for DcRegisteredSubjectInfo complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="DcRegisteredSubjectInfo"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;extension base="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject}DcSubjectPublicInfo"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="InstitutionType" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums}eInstitutionType" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/extension&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DcRegisteredSubjectInfo", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", propOrder = {
    "institutionType"
})
public class DcRegisteredSubjectInfo
    extends DcSubjectPublicInfo
{

    @XmlElement(name = "InstitutionType")
    protected EInstitutionType institutionType;

    /**
     * Gets the value of the institutionType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link EInstitutionType }{@code >}
     *     
     */
    public EInstitutionType getInstitutionType() {
        return institutionType;
    }

    /**
     * Sets the value of the institutionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link EInstitutionType }{@code >}
     *     
     */
    public void setInstitutionType(EInstitutionType value) {
        this.institutionType = value;
    }

}
