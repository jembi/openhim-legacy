
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActInsurancePolicyCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActInsurancePolicyCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="AUTOPOL"/>
 *     &lt;enumeration value="EHCPOL"/>
 *     &lt;enumeration value="HSAPOL"/>
 *     &lt;enumeration value="PUBLICPOL"/>
 *     &lt;enumeration value="WCBPOL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActInsurancePolicyCode")
@XmlEnum
public enum ActInsurancePolicyCode {

    AUTOPOL,
    EHCPOL,
    HSAPOL,
    PUBLICPOL,
    WCBPOL;

    public String value() {
        return name();
    }

    public static ActInsurancePolicyCode fromValue(String v) {
        return valueOf(v);
    }

}
