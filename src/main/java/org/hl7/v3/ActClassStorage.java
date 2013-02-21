
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActClassStorage.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActClassStorage">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="STORE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActClassStorage")
@XmlEnum
public enum ActClassStorage {

    STORE;

    public String value() {
        return name();
    }

    public static ActClassStorage fromValue(String v) {
        return valueOf(v);
    }

}
