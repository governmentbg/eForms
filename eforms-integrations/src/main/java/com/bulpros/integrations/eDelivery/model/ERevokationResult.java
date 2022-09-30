
package com.bulpros.integrations.eDelivery.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for eRevokationResult.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * &lt;pre&gt;
 * &amp;lt;simpleType name="eRevokationResult"&amp;gt;
 *   &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt;
 *     &amp;lt;enumeration value="OK"/&amp;gt;
 *     &amp;lt;enumeration value="Revoked"/&amp;gt;
 *     &amp;lt;enumeration value="CanNotDetermine"/&amp;gt;
 *   &amp;lt;/restriction&amp;gt;
 * &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
 * 
 */
@XmlType(name = "eRevokationResult", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums")
@XmlEnum
public enum ERevokationResult {

    OK("OK"),
    @XmlEnumValue("Revoked")
    REVOKED("Revoked"),
    @XmlEnumValue("CanNotDetermine")
    CAN_NOT_DETERMINE("CanNotDetermine");
    private final String value;

    ERevokationResult(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ERevokationResult fromValue(String v) {
        for (ERevokationResult c: ERevokationResult.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
