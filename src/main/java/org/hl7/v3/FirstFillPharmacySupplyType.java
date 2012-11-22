
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FirstFillPharmacySupplyType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FirstFillPharmacySupplyType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="FF"/>
 *     &lt;enumeration value="DF"/>
 *     &lt;enumeration value="FFC"/>
 *     &lt;enumeration value="FFP"/>
 *     &lt;enumeration value="TF"/>
 *     &lt;enumeration value="UD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FirstFillPharmacySupplyType")
@XmlEnum
public enum FirstFillPharmacySupplyType {

    FF,
    DF,
    FFC,
    FFP,
    TF,
    UD;

    public String value() {
        return name();
    }

    public static FirstFillPharmacySupplyType fromValue(String v) {
        return valueOf(v);
    }

}
