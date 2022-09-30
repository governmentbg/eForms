
package com.bulpros.integrations.auditlog.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the bg.egov.bes.ws.auditlog.v1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _EAServiceLCEvent_QNAME = new QName("http://egov.bg/bes/ws/auditlog/v1", "EAServiceLCEvent");
    private final static QName _RegisterGovernanceEvent_QNAME = new QName("http://egov.bg/bes/ws/auditlog/v1", "RegisterGovernanceEvent");
    private final static QName _ServiceRequesterISType_QNAME = new QName("http://egov.bg/bes/ws/auditlog/v1", "ServiceRequesterISType");
    private final static QName _RegisterEvent_QNAME = new QName("http://egov.bg/bes/ws/auditlog/v1", "RegisterEvent");
    private final static QName _SubscriberType_QNAME = new QName("http://egov.bg/bes/ws/auditlog/v1", "SubscriberType");
    private final static QName _RegisterPDAccessAttempt_QNAME = new QName("http://egov.bg/bes/ws/auditlog/v1", "RegisterPDAccessAttempt");
    private final static QName _EventSubscription_QNAME = new QName("http://egov.bg/bes/ws/auditlog/v1", "EventSubscription");
    private final static QName _GovernanceEvent_QNAME = new QName("http://egov.bg/bes/ws/auditlog/v1", "GovernanceEvent");
    private final static QName _RequestedServiceType_QNAME = new QName("http://egov.bg/bes/ws/auditlog/v1", "RequestedServiceType");
    private final static QName _PersonalDataAccessAttempt_QNAME = new QName("http://egov.bg/bes/ws/auditlog/v1", "PersonalDataAccessAttempt");
    private final static QName _EventSubscriptionEmailAddress_QNAME = new QName("", "emailAddress");
    private final static QName _EventSubscriptionSubscriber_QNAME = new QName("", "subscriber");
    private final static QName _EventSubscriptionCreationTime_QNAME = new QName("", "creationTime");
    private final static QName _EventSubscriptionEventType_QNAME = new QName("", "eventType");
    private final static QName _EventSubscriptionNotificationChannel_QNAME = new QName("", "notificationChannel");
    private final static QName _EventSubscriptionStatus_QNAME = new QName("", "status");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: bg.egov.bes.ws.auditlog.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PersonalDataAccessAttemptType }
     * 
     */
    public PersonalDataAccessAttemptType createPersonalDataAccessAttemptType() {
        return new PersonalDataAccessAttemptType();
    }

    /**
     * Create an instance of {@link GovernanceEventType }
     * 
     */
    public GovernanceEventType createGovernanceEventType() {
        return new GovernanceEventType();
    }

    /**
     * Create an instance of {@link RequestedServiceType }
     * 
     */
    public RequestedServiceType createRequestedServiceType() {
        return new RequestedServiceType();
    }

    /**
     * Create an instance of {@link EventSubscription }
     * 
     */
    public EventSubscription createEventSubscription() {
        return new EventSubscription();
    }

    /**
     * Create an instance of {@link RegisterPDAccessAttemptType }
     * 
     */
    public RegisterPDAccessAttemptType createRegisterPDAccessAttemptType() {
        return new RegisterPDAccessAttemptType();
    }

    /**
     * Create an instance of {@link SubscriberType }
     * 
     */
    public SubscriberType createSubscriberType() {
        return new SubscriberType();
    }

    /**
     * Create an instance of {@link RegisterEventType }
     * 
     */
    public RegisterEventType createRegisterEventType() {
        return new RegisterEventType();
    }

    /**
     * Create an instance of {@link EAServiceLCEventType }
     * 
     */
    public EAServiceLCEventType createEAServiceLCEventType() {
        return new EAServiceLCEventType();
    }

    /**
     * Create an instance of {@link RegisterGovernanceEventType }
     * 
     */
    public RegisterGovernanceEventType createRegisterGovernanceEventType() {
        return new RegisterGovernanceEventType();
    }

    /**
     * Create an instance of {@link ServiceRequesterISType }
     * 
     */
    public ServiceRequesterISType createServiceRequesterISType() {
        return new ServiceRequesterISType();
    }

    /**
     * Create an instance of {@link EventType }
     * 
     */
    public EventType createEventType() {
        return new EventType();
    }

    /**
     * Create an instance of {@link AdmEmployeeType }
     * 
     */
    public AdmEmployeeType createAdmEmployeeType() {
        return new AdmEmployeeType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EAServiceLCEventType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://egov.bg/bes/ws/auditlog/v1", name = "EAServiceLCEvent")
    public JAXBElement<EAServiceLCEventType> createEAServiceLCEvent(EAServiceLCEventType value) {
        return new JAXBElement<EAServiceLCEventType>(_EAServiceLCEvent_QNAME, EAServiceLCEventType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterGovernanceEventType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://egov.bg/bes/ws/auditlog/v1", name = "RegisterGovernanceEvent")
    public JAXBElement<RegisterGovernanceEventType> createRegisterGovernanceEvent(RegisterGovernanceEventType value) {
        return new JAXBElement<RegisterGovernanceEventType>(_RegisterGovernanceEvent_QNAME, RegisterGovernanceEventType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceRequesterISType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://egov.bg/bes/ws/auditlog/v1", name = "ServiceRequesterISType")
    public JAXBElement<ServiceRequesterISType> createServiceRequesterISType(ServiceRequesterISType value) {
        return new JAXBElement<ServiceRequesterISType>(_ServiceRequesterISType_QNAME, ServiceRequesterISType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterEventType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://egov.bg/bes/ws/auditlog/v1", name = "RegisterEvent")
    public JAXBElement<RegisterEventType> createRegisterEvent(RegisterEventType value) {
        return new JAXBElement<RegisterEventType>(_RegisterEvent_QNAME, RegisterEventType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubscriberType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://egov.bg/bes/ws/auditlog/v1", name = "SubscriberType")
    public JAXBElement<SubscriberType> createSubscriberType(SubscriberType value) {
        return new JAXBElement<SubscriberType>(_SubscriberType_QNAME, SubscriberType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterPDAccessAttemptType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://egov.bg/bes/ws/auditlog/v1", name = "RegisterPDAccessAttempt")
    public JAXBElement<RegisterPDAccessAttemptType> createRegisterPDAccessAttempt(RegisterPDAccessAttemptType value) {
        return new JAXBElement<RegisterPDAccessAttemptType>(_RegisterPDAccessAttempt_QNAME, RegisterPDAccessAttemptType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EventSubscription }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://egov.bg/bes/ws/auditlog/v1", name = "EventSubscription")
    public JAXBElement<EventSubscription> createEventSubscription(EventSubscription value) {
        return new JAXBElement<EventSubscription>(_EventSubscription_QNAME, EventSubscription.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GovernanceEventType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://egov.bg/bes/ws/auditlog/v1", name = "GovernanceEvent")
    public JAXBElement<GovernanceEventType> createGovernanceEvent(GovernanceEventType value) {
        return new JAXBElement<GovernanceEventType>(_GovernanceEvent_QNAME, GovernanceEventType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestedServiceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://egov.bg/bes/ws/auditlog/v1", name = "RequestedServiceType")
    public JAXBElement<RequestedServiceType> createRequestedServiceType(RequestedServiceType value) {
        return new JAXBElement<RequestedServiceType>(_RequestedServiceType_QNAME, RequestedServiceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonalDataAccessAttemptType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://egov.bg/bes/ws/auditlog/v1", name = "PersonalDataAccessAttempt")
    public JAXBElement<PersonalDataAccessAttemptType> createPersonalDataAccessAttempt(PersonalDataAccessAttemptType value) {
        return new JAXBElement<PersonalDataAccessAttemptType>(_PersonalDataAccessAttempt_QNAME, PersonalDataAccessAttemptType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "emailAddress", scope = EventSubscription.class)
    public JAXBElement<String> createEventSubscriptionEmailAddress(String value) {
        return new JAXBElement<String>(_EventSubscriptionEmailAddress_QNAME, String.class, EventSubscription.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubscriberType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "subscriber", scope = EventSubscription.class)
    public JAXBElement<SubscriberType> createEventSubscriptionSubscriber(SubscriberType value) {
        return new JAXBElement<SubscriberType>(_EventSubscriptionSubscriber_QNAME, SubscriberType.class, EventSubscription.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "creationTime", scope = EventSubscription.class)
    public JAXBElement<String> createEventSubscriptionCreationTime(String value) {
        return new JAXBElement<String>(_EventSubscriptionCreationTime_QNAME, String.class, EventSubscription.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EventTypeEnum }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "eventType", scope = EventSubscription.class)
    public JAXBElement<EventTypeEnum> createEventSubscriptionEventType(EventTypeEnum value) {
        return new JAXBElement<EventTypeEnum>(_EventSubscriptionEventType_QNAME, EventTypeEnum.class, EventSubscription.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "notificationChannel", scope = EventSubscription.class)
    public JAXBElement<String> createEventSubscriptionNotificationChannel(String value) {
        return new JAXBElement<String>(_EventSubscriptionNotificationChannel_QNAME, String.class, EventSubscription.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubscriptionStatusEnum }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "status", scope = EventSubscription.class)
    public JAXBElement<SubscriptionStatusEnum> createEventSubscriptionStatus(SubscriptionStatusEnum value) {
        return new JAXBElement<SubscriptionStatusEnum>(_EventSubscriptionStatus_QNAME, SubscriptionStatusEnum.class, EventSubscription.class, value);
    }

}
