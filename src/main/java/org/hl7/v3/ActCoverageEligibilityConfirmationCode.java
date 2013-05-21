
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActCoverageEligibilityConfirmationCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActCoverageEligibilityConfirmationCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ELG"/>
 *     &lt;enumeration value="NELG"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActCoverageEligibilityConfirmationCode")
@XmlEnum
public enum ActCoverageEligibilityConfirmationCode {

    ELG,
    NELG;

    public String value() {
        return name();
    }

    public static ActCoverageEligibilityConfirmationCode fromValue(String v) {
        return valueOf(v);
    }

}
