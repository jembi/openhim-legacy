
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FamilyMember.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FamilyMember">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SPS"/>
 *     &lt;enumeration value="HUSB"/>
 *     &lt;enumeration value="WIFE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FamilyMember")
@XmlEnum
public enum FamilyMember {

    SPS,
    HUSB,
    WIFE;

    public String value() {
        return name();
    }

    public static FamilyMember fromValue(String v) {
        return valueOf(v);
    }

}
