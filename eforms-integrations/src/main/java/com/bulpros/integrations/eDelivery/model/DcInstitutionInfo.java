
package com.bulpros.integrations.eDelivery.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for DcInstitutionInfo complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="DcInstitutionInfo"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;extension base="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject}DcSubjectInfo"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="HeadInstitution" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject}DcSubjectPublicInfo" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="SubInstitutions" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject}ArrayOfDcSubjectPublicInfo" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/extension&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DcInstitutionInfo", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", propOrder = {
    "headInstitution",
    "name",
    "subInstitutions"
})
@XmlSeeAlso({
    WebInstitutionInfo.class
})
public class DcInstitutionInfo
    extends DcSubjectInfo
{

    @XmlElementRef(name = "HeadInstitution", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", type = JAXBElement.class, required = false)
    protected JAXBElement<DcSubjectPublicInfo> headInstitution;
    @XmlElementRef(name = "Name", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", type = JAXBElement.class, required = false)
    protected JAXBElement<String> name;
    @XmlElementRef(name = "SubInstitutions", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts.ESubject", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfDcSubjectPublicInfo> subInstitutions;

    /**
     * Gets the value of the headInstitution property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DcSubjectPublicInfo }{@code >}
     *     
     */
    public JAXBElement<DcSubjectPublicInfo> getHeadInstitution() {
        return headInstitution;
    }

    /**
     * Sets the value of the headInstitution property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DcSubjectPublicInfo }{@code >}
     *     
     */
    public void setHeadInstitution(JAXBElement<DcSubjectPublicInfo> value) {
        this.headInstitution = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setName(JAXBElement<String> value) {
        this.name = value;
    }

    /**
     * Gets the value of the subInstitutions property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfDcSubjectPublicInfo }{@code >}
     *     
     */
    public JAXBElement<ArrayOfDcSubjectPublicInfo> getSubInstitutions() {
        return subInstitutions;
    }

    /**
     * Sets the value of the subInstitutions property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfDcSubjectPublicInfo }{@code >}
     *     
     */
    public void setSubInstitutions(JAXBElement<ArrayOfDcSubjectPublicInfo> value) {
        this.subInstitutions = value;
    }

}
