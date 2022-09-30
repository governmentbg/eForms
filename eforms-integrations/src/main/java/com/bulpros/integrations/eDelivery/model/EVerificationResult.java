
package com.bulpros.integrations.eDelivery.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for eVerificationResult.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * &lt;pre&gt;
 * &amp;lt;simpleType name="eVerificationResult"&amp;gt;
 *   &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt;
 *     &amp;lt;enumeration value="Success"/&amp;gt;
 *     &amp;lt;enumeration value="InvalidFile"/&amp;gt;
 *     &amp;lt;enumeration value="NoSignatureFound"/&amp;gt;
 *     &amp;lt;enumeration value="DetachedSignature"/&amp;gt;
 *   &amp;lt;/restriction&amp;gt;
 * &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
 * 
 */
@XmlType(name = "eVerificationResult", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums")
@XmlEnum
public enum EVerificationResult {

    @XmlEnumValue("Success")
    SUCCESS("Success"),
    @XmlEnumValue("InvalidFile")
    INVALID_FILE("InvalidFile"),
    @XmlEnumValue("NoSignatureFound")
    NO_SIGNATURE_FOUND("NoSignatureFound"),
    @XmlEnumValue("DetachedSignature")
    DETACHED_SIGNATURE("DetachedSignature");
    private final String value;

    EVerificationResult(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EVerificationResult fromValue(String v) {
        for (EVerificationResult c: EVerificationResult.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
