
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RefillPharmacySupplyType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RefillPharmacySupplyType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="RF"/>
 *     &lt;enumeration value="DF"/>
 *     &lt;enumeration value="RFF"/>
 *     &lt;enumeration value="RFC"/>
 *     &lt;enumeration value="RFP"/>
 *     &lt;enumeration value="TB"/>
 *     &lt;enumeration value="UD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RefillPharmacySupplyType")
@XmlEnum
public enum RefillPharmacySupplyType {

    RF,
    DF,
    RFF,
    RFC,
    RFP,
    TB,
    UD;

    public String value() {
        return name();
    }

    public static RefillPharmacySupplyType fromValue(String v) {
        return valueOf(v);
    }

}
