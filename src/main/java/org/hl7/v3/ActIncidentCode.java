
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActIncidentCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActIncidentCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="MVA"/>
 *     &lt;enumeration value="SCHOOL"/>
 *     &lt;enumeration value="SPT"/>
 *     &lt;enumeration value="WPA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActIncidentCode")
@XmlEnum
public enum ActIncidentCode {

    MVA,
    SCHOOL,
    SPT,
    WPA;

    public String value() {
        return name();
    }

    public static ActIncidentCode fromValue(String v) {
        return valueOf(v);
    }

}
