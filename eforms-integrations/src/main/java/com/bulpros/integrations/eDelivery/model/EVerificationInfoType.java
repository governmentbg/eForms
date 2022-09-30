
package com.bulpros.integrations.eDelivery.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for eVerificationInfoType.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * &lt;pre&gt;
 * &amp;lt;simpleType name="eVerificationInfoType"&amp;gt;
 *   &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt;
 *     &amp;lt;enumeration value="Certificate"/&amp;gt;
 *     &amp;lt;enumeration value="EID"/&amp;gt;
 *     &amp;lt;enumeration value="AdministrativeAct"/&amp;gt;
 *     &amp;lt;enumeration value="NOIToken"/&amp;gt;
 *   &amp;lt;/restriction&amp;gt;
 * &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
 * 
 */
@XmlType(name = "eVerificationInfoType", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums")
@XmlEnum
public enum EVerificationInfoType {

    @XmlEnumValue("Certificate")
    CERTIFICATE("Certificate"),
    EID("EID"),
    @XmlEnumValue("AdministrativeAct")
    ADMINISTRATIVE_ACT("AdministrativeAct"),
    @XmlEnumValue("NOIToken")
    NOI_TOKEN("NOIToken");
    private final String value;

    EVerificationInfoType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EVerificationInfoType fromValue(String v) {
        for (EVerificationInfoType c: EVerificationInfoType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
