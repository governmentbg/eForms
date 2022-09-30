
package com.bulpros.integrations.auditlog.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EventTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EventTypeEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ROID_OID_CREATED"/>
 *     &lt;enumeration value="ROID_OID_UPDATED"/>
 *     &lt;enumeration value="ROID_OID_DELETED"/>
 *     &lt;enumeration value="RRES_ADMINISTRATION_CREATED"/>
 *     &lt;enumeration value="RRES_ADMINISTRATION_UPDATED"/>
 *     &lt;enumeration value="RRES_ADMINISTRATION_DELETED"/>
 *     &lt;enumeration value="RRES_IS_CREATED"/>
 *     &lt;enumeration value="RRES_IS_UPDATED"/>
 *     &lt;enumeration value="RRES_IS_DELETED"/>
 *     &lt;enumeration value="RRES_SERVICE_CREATED"/>
 *     &lt;enumeration value="RRES_SERVICE_UPDATED"/>
 *     &lt;enumeration value="RRES_SERVICE_DELETED"/>
 *     &lt;enumeration value="PERSONAL_DATA_ACCESS_ATTEMPT"/>
 *     &lt;enumeration value="PORTAL_ESERVICE_REQUESTED"/>
 *     &lt;enumeration value="PORTAL_AUTHN_REQUEST_SENT"/>
 *     &lt;enumeration value="PORTAL_AUTHN_RESPONSE_REDIRECTED"/>
 *     &lt;enumeration value="AUTHN_DENIED"/>
 *     &lt;enumeration value="AUTHN_ATTRBQUERY_SENT"/>
 *     &lt;enumeration value="AUTHN_ATTR_RECEIVED"/>
 *     &lt;enumeration value="AUTHN_RESPONSE_SENT"/>
 *     &lt;enumeration value="AUTHN_REQUEST_RECEIVED"/>
 *     &lt;enumeration value="AUTHN_SUCESSFUL"/>
 *     &lt;enumeration value="AUTHOR_REQUEST_SENT"/>
 *     &lt;enumeration value="AUTHOR_DENIED"/>
 *     &lt;enumeration value="AUTHOR_SUCCEEDED"/>
 *     &lt;enumeration value="AIS_AUTHN_RECEIVED"/>
 *     &lt;enumeration value="AIS_ESERVICE_VALIDATION_FAILED"/>
 *     &lt;enumeration value="AIS_ESERVICE_VALIDATION_SUCCEEDED"/>
 *     &lt;enumeration value="AIS_EPAYMENT_REQUEST_SENT"/>
 *     &lt;enumeration value="AIS_ESERVICE_DENIED"/>
 *     &lt;enumeration value="AIS_ESERVICE_STARTED"/>
 *     &lt;enumeration value="AIS_ESERVICE_RESULT_READY"/>
 *     &lt;enumeration value="AIS_ESERVICE_RESULT_REGISTRATION_SENT"/>
 *     &lt;enumeration value="AIS_ESERVICE_DOCUMENT_REGISTERED"/>
 *     &lt;enumeration value="AIS_ESERVICE_CANCELED_BY_USER"/>
 *     &lt;enumeration value="EPAYMENT_PAYMENT_REQUEST_RECEIVED"/>
 *     &lt;enumeration value="EPAYMENT_PAYMENT_REQUEST_DENIED"/>
 *     &lt;enumeration value="EPAYMENT_PAYMENT_VPOS_AUTHORIZED"/>
 *     &lt;enumeration value="EPAYMENT_PAYMENT_ORDERED"/>
 *     &lt;enumeration value="EPAYMENT_PAYMENT_PAID"/>
 *     &lt;enumeration value="EDELIVERY_DOCUMENT_RECEIVED"/>
 *     &lt;enumeration value="EDELIVERY_DOCUMENT_REGISTERED"/>
 *     &lt;enumeration value="EDELIVERY_DOCUMENT_REGISTRATION_DENIED"/>
 *     &lt;enumeration value="EDELIVERY_NOTIFICATION_SENT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EventTypeEnum")
@XmlEnum
public enum EventTypeEnum {

    PORTAL_ESERVICE_REQUESTED,
    PORTAL_AUTHN_REQUEST_SENT,
    PORTAL_AUTHN_RESPONSE_REDIRECTED,
    AIS_AUTHN_RECEIVED,
    AIS_ESERVICE_VALIDATION_FAILED,
    AIS_ESERVICE_VALIDATION_SUCCEEDED,
    AIS_EPAYMENT_REQUEST_SENT,
    AIS_ESERVICE_DENIED,
    AIS_ESERVICE_STARTED,
    AIS_ESERVICE_RESULT_READY,
    AIS_ESERVICE_RESULT_REGISTRATION_SENT,
    AIS_ESERVICE_DOCUMENT_REGISTERED,
    AIS_ESERVICE_CANCELED_BY_USER;

    public String value() {
        return name();
    }

    public static EventTypeEnum fromValue(String v) {
        return valueOf(v);
    }

}
