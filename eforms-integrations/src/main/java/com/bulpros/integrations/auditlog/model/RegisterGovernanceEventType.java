
package com.bulpros.integrations.auditlog.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for RegisterGovernanceEventType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegisterGovernanceEventType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://egov.bg/bes/ws/auditlog/v1}GovernanceEvent" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegisterGovernanceEventType", propOrder = {
    "governanceEvent"
})
public class RegisterGovernanceEventType {

    @XmlElement(name = "GovernanceEvent", namespace = "http://egov.bg/bes/ws/auditlog/v1", required = true)
    protected List<GovernanceEventType> governanceEvent;

    /**
     * Gets the value of the governanceEvent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the governanceEvent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGovernanceEvent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GovernanceEventType }
     * 
     * 
     */
    public List<GovernanceEventType> getGovernanceEvent() {
        if (governanceEvent == null) {
            governanceEvent = new ArrayList<GovernanceEventType>();
        }
        return this.governanceEvent;
    }

}
