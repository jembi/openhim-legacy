
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActEncounterAccommodationCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActEncounterAccommodationCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="I"/>
 *     &lt;enumeration value="P"/>
 *     &lt;enumeration value="SP"/>
 *     &lt;enumeration value="S"/>
 *     &lt;enumeration value="W"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActEncounterAccommodationCode")
@XmlEnum
public enum ActEncounterAccommodationCode {

    I,
    P,
    SP,
    S,
    W;

    public String value() {
        return name();
    }

    public static ActEncounterAccommodationCode fromValue(String v) {
        return valueOf(v);
    }

}
