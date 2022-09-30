
package com.bulpros.integrations.auditlog.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PersonIdTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PersonIdTypeEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ЕГН"/>
 *     &lt;enumeration value="ЛНЧ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PersonIdTypeEnum")
@XmlEnum
public enum PersonIdTypeEnum {

    ЕГН,
    ЛНЧ;

    public String value() {
        return name();
    }

    public static PersonIdTypeEnum fromValue(String v) {
        return valueOf(v);
    }

}
