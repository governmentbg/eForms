
package com.bulpros.eforms.processengine.edelivery.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for eProfileType.
 *
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * &lt;pre&gt;
 * &amp;lt;simpleType name="eProfileType"&amp;gt;
 *   &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt;
 *     &amp;lt;enumeration value="Person"/&amp;gt;
 *     &amp;lt;enumeration value="LegalPerson"/&amp;gt;
 *     &amp;lt;enumeration value="Institution"/&amp;gt;
 *     &amp;lt;enumeration value="Administrator"/&amp;gt;
 *   &amp;lt;/restriction&amp;gt;
 * &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
 *
 */
@XmlType(name = "eProfileType", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums")
@XmlEnum
public enum EProfileType {

    @XmlEnumValue("Person")
    @JsonProperty("Person")
    PERSON("Person"),
    @XmlEnumValue("LegalPerson")
    @JsonProperty("LegalPerson")
    LEGAL_PERSON("LegalPerson"),
    @XmlEnumValue("Institution")
    @JsonProperty("Institution")
    INSTITUTION("Institution"),
    @XmlEnumValue("Administrator")
    @JsonProperty("Administrator")
    ADMINISTRATOR("Administrator");
    private final String value;

    EProfileType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EProfileType fromValue(String v) {
        for (EProfileType c: EProfileType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
