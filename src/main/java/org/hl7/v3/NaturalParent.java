
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NaturalParent.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="NaturalParent">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="NFTH"/>
 *     &lt;enumeration value="NFTHF"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "NaturalParent")
@XmlEnum
public enum NaturalParent {

    NFTH,
    NFTHF;

    public String value() {
        return name();
    }

    public static NaturalParent fromValue(String v) {
        return valueOf(v);
    }

}
