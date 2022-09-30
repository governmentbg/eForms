
package com.bulpros.integrations.eDelivery.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for eSortColumn.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * &lt;pre&gt;
 * &amp;lt;simpleType name="eSortColumn"&amp;gt;
 *   &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt;
 *     &amp;lt;enumeration value="None"/&amp;gt;
 *     &amp;lt;enumeration value="Status"/&amp;gt;
 *     &amp;lt;enumeration value="ReceiverName"/&amp;gt;
 *     &amp;lt;enumeration value="Title"/&amp;gt;
 *     &amp;lt;enumeration value="SenderName"/&amp;gt;
 *     &amp;lt;enumeration value="DateSent"/&amp;gt;
 *     &amp;lt;enumeration value="DateReceived"/&amp;gt;
 *     &amp;lt;enumeration value="RegIndex"/&amp;gt;
 *     &amp;lt;enumeration value="DocKind"/&amp;gt;
 *   &amp;lt;/restriction&amp;gt;
 * &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
 * 
 */
@XmlType(name = "eSortColumn", namespace = "http://schemas.datacontract.org/2004/07/EDelivery.Common.Enums")
@XmlEnum
public enum ESortColumn {

    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("Status")
    STATUS("Status"),
    @XmlEnumValue("ReceiverName")
    RECEIVER_NAME("ReceiverName"),
    @XmlEnumValue("Title")
    TITLE("Title"),
    @XmlEnumValue("SenderName")
    SENDER_NAME("SenderName"),
    @XmlEnumValue("DateSent")
    DATE_SENT("DateSent"),
    @XmlEnumValue("DateReceived")
    DATE_RECEIVED("DateReceived"),
    @XmlEnumValue("RegIndex")
    REG_INDEX("RegIndex"),
    @XmlEnumValue("DocKind")
    DOC_KIND("DocKind");
    private final String value;

    ESortColumn(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ESortColumn fromValue(String v) {
        for (ESortColumn c: ESortColumn.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
