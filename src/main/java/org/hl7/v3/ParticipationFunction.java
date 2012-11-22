
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParticipationFunction.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParticipationFunction">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ADMPHYS"/>
 *     &lt;enumeration value="ANRS"/>
 *     &lt;enumeration value="ANEST"/>
 *     &lt;enumeration value="ATTPHYS"/>
 *     &lt;enumeration value="DISPHYS"/>
 *     &lt;enumeration value="FASST"/>
 *     &lt;enumeration value="MDWF"/>
 *     &lt;enumeration value="NASST"/>
 *     &lt;enumeration value="PCP"/>
 *     &lt;enumeration value="PRISURG"/>
 *     &lt;enumeration value="RNDPHYS"/>
 *     &lt;enumeration value="SNRS"/>
 *     &lt;enumeration value="SASST"/>
 *     &lt;enumeration value="TASST"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ParticipationFunction")
@XmlEnum
public enum ParticipationFunction {

    ADMPHYS,
    ANRS,
    ANEST,
    ATTPHYS,
    DISPHYS,
    FASST,
    MDWF,
    NASST,
    PCP,
    PRISURG,
    RNDPHYS,
    SNRS,
    SASST,
    TASST;

    public String value() {
        return name();
    }

    public static ParticipationFunction fromValue(String v) {
        return valueOf(v);
    }

}
