
package com.bulpros.integrations.auditlog.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for RegisterEventType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegisterEventType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://egov.bg/bes/ws/auditlog/v1}EAServiceLCEvent" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegisterEventType", propOrder = {
    "eaServiceLCEvent"
})
public class RegisterEventType {

    @XmlElement(name = "EAServiceLCEvent", namespace = "http://egov.bg/bes/ws/auditlog/v1", required = true)
    protected List<EAServiceLCEventType> eaServiceLCEvent;

    /**
     * Gets the value of the eaServiceLCEvent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the eaServiceLCEvent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEAServiceLCEvent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EAServiceLCEventType }
     * 
     * 
     */
    public List<EAServiceLCEventType> getEAServiceLCEvent() {
        if (eaServiceLCEvent == null) {
            eaServiceLCEvent = new ArrayList<EAServiceLCEventType>();
        }
        return this.eaServiceLCEvent;
    }

}
