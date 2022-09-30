
package com.bulpros.integrations.eDelivery.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for eSortOrder.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * &lt;pre&gt;
 * &amp;lt;simpleType name="eSortOrder"&amp;gt;
 *   &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt;
 *     &amp;lt;enumeration value="None"/&amp;gt;
 *     &amp;lt;enumeration value="Asc"/&amp;gt;
 *     &amp;lt;enumeration value="Desc"/&amp;gt;
 *   &amp;lt;/restriction&amp;gt;
 * &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
 * 
 */
@XmlType(name = "eSortOrder", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums")
@XmlEnum
public enum ESortOrder {

    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("Asc")
    ASC("Asc"),
    @XmlEnumValue("Desc")
    DESC("Desc");
    private final String value;

    ESortOrder(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ESortOrder fromValue(String v) {
        for (ESortOrder c: ESortOrder.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
