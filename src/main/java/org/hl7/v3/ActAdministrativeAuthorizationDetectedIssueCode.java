
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActAdministrativeAuthorizationDetectedIssueCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActAdministrativeAuthorizationDetectedIssueCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="NAT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActAdministrativeAuthorizationDetectedIssueCode")
@XmlEnum
public enum ActAdministrativeAuthorizationDetectedIssueCode {

    NAT;

    public String value() {
        return name();
    }

    public static ActAdministrativeAuthorizationDetectedIssueCode fromValue(String v) {
        return valueOf(v);
    }

}
