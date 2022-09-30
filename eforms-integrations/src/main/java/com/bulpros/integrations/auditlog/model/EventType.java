
package com.bulpros.integrations.auditlog.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for EventType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="EventTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="EventType" type="{http://egov.bg/bes/ws/auditlog/v1}EventTypeEnum"/>
 *         &lt;element name="EventType2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RequestedService" type="{http://egov.bg/bes/ws/auditlog/v1}RequestedServiceType"/>
 *         &lt;element name="InformationSystemOID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EventDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventType", propOrder = {
    "id",
    "eventTime",
    "eventType",
    "eventType2",
    "requestedService",
    "informationSystemOID",
    "eventDescription"
})
@XmlSeeAlso({
    PersonalDataAccessAttemptType.class,
    EAServiceLCEventType.class
})
public class EventType {

    protected Long id;
    @XmlElement(name = "EventTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar eventTime;
    @XmlElement(name = "EventType", required = true)
    @XmlSchemaType(name = "string")
    protected EventTypeEnum eventType;
    @XmlElement(name = "EventType2")
    protected String eventType2;
    @XmlElement(name = "RequestedService", required = true)
    protected RequestedServiceType requestedService;
    @XmlElement(name = "InformationSystemOID", required = true)
    protected String informationSystemOID;
    @XmlElement(name = "EventDescription", required = true)
    protected String eventDescription;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the eventTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEventTime() {
        return eventTime;
    }

    /**
     * Sets the value of the eventTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEventTime(XMLGregorianCalendar value) {
        this.eventTime = value;
    }

    /**
     * Gets the value of the eventType property.
     * 
     * @return
     *     possible object is
     *     {@link EventTypeEnum }
     *     
     */
    public EventTypeEnum getEventType() {
        return eventType;
    }

    /**
     * Sets the value of the eventType property.
     * 
     * @param value
     *     allowed object is
     *     {@link EventTypeEnum }
     *     
     */
    public void setEventType(EventTypeEnum value) {
        this.eventType = value;
    }

    /**
     * Gets the value of the eventType2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventType2() {
        return eventType2;
    }

    /**
     * Sets the value of the eventType2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventType2(String value) {
        this.eventType2 = value;
    }

    /**
     * Gets the value of the requestedService property.
     * 
     * @return
     *     possible object is
     *     {@link RequestedServiceType }
     *     
     */
    public RequestedServiceType getRequestedService() {
        return requestedService;
    }

    /**
     * Sets the value of the requestedService property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestedServiceType }
     *     
     */
    public void setRequestedService(RequestedServiceType value) {
        this.requestedService = value;
    }

    /**
     * Gets the value of the informationSystemOID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInformationSystemOID() {
        return informationSystemOID;
    }

    /**
     * Sets the value of the informationSystemOID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInformationSystemOID(String value) {
        this.informationSystemOID = value;
    }

    /**
     * Gets the value of the eventDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * Sets the value of the eventDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventDescription(String value) {
        this.eventDescription = value;
    }

}
