
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Supine.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Supine">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SUP"/>
 *     &lt;enumeration value="RTRD"/>
 *     &lt;enumeration value="TRD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Supine")
@XmlEnum
public enum Supine {

    SUP,
    RTRD,
    TRD;

    public String value() {
        return name();
    }

    public static Supine fromValue(String v) {
        return valueOf(v);
    }

}
