
package com.bulpros.integrations.auditlog.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EventSubscription complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventSubscription">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="creationTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="status" type="{http://egov.bg/bes/ws/auditlog/v1}SubscriptionStatusEnum"/>
 *           &lt;element name="notificationChannel" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="emailAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="subscriber" type="{http://egov.bg/bes/ws/auditlog/v1}SubscriberType"/>
 *           &lt;element name="eventType" type="{http://egov.bg/bes/ws/auditlog/v1}EventTypeEnum"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventSubscription", propOrder = {
    "creationTimeOrStatusOrNotificationChannel"
})
public class EventSubscription {

    @XmlElementRefs({
        @XmlElementRef(name = "creationTime", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "emailAddress", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "status", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "notificationChannel", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "eventType", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "subscriber", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> creationTimeOrStatusOrNotificationChannel;

    /**
     * Gets the value of the creationTimeOrStatusOrNotificationChannel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the creationTimeOrStatusOrNotificationChannel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCreationTimeOrStatusOrNotificationChannel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link SubscriptionStatusEnum }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link EventTypeEnum }{@code >}
     * {@link JAXBElement }{@code <}{@link SubscriberType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getCreationTimeOrStatusOrNotificationChannel() {
        if (creationTimeOrStatusOrNotificationChannel == null) {
            creationTimeOrStatusOrNotificationChannel = new ArrayList<JAXBElement<?>>();
        }
        return this.creationTimeOrStatusOrNotificationChannel;
    }

}
