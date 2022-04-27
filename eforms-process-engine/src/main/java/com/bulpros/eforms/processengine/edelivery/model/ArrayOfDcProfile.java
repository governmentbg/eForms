
package com.bulpros.eforms.processengine.edelivery.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * &lt;p&gt;Java class for ArrayOfDcProfile complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="ArrayOfDcProfile"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="DcProfile" type="{http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts}DcProfile" maxOccurs="unbounded" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfDcProfile", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.DataContracts", propOrder = {
    "dcProfile"
})
public class ArrayOfDcProfile {

    @XmlElement(name = "DcProfile", nillable = true)
    protected List<DcProfile> dcProfile;

    /**
     * Gets the value of the dcProfile property.
     * 
     * &lt;p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the dcProfile property.
     * 
     * &lt;p&gt;
     * For example, to add a new item, do as follows:
     * &lt;pre&gt;
     *    getDcProfile().add(newItem);
     * &lt;/pre&gt;
     * 
     * 
     * &lt;p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link DcProfile }
     * 
     * 
     */
    public List<DcProfile> getDcProfile() {
        if (dcProfile == null) {
            dcProfile = new ArrayList<DcProfile>();
        }
        return this.dcProfile;
    }

}
