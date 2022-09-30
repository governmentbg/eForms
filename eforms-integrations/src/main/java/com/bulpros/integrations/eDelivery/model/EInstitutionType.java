
package com.bulpros.integrations.eDelivery.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for eInstitutionType.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * &lt;pre&gt;
 * &amp;lt;simpleType name="eInstitutionType"&amp;gt;
 *   &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt;
 *     &amp;lt;enumeration value="StateAdministraation"/&amp;gt;
 *     &amp;lt;enumeration value="SocialOrganisations"/&amp;gt;
 *   &amp;lt;/restriction&amp;gt;
 * &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
 * 
 */
@XmlType(name = "eInstitutionType", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums")
@XmlEnum
public enum EInstitutionType {

    @XmlEnumValue("StateAdministraation")
    STATE_ADMINISTRAATION("StateAdministraation"),
    @XmlEnumValue("SocialOrganisations")
    SOCIAL_ORGANISATIONS("SocialOrganisations");
    private final String value;

    EInstitutionType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EInstitutionType fromValue(String v) {
        for (EInstitutionType c: EInstitutionType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
