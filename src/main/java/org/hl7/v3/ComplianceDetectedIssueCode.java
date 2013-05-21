
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ComplianceDetectedIssueCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ComplianceDetectedIssueCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="COMPLY"/>
 *     &lt;enumeration value="DUPTHPY"/>
 *     &lt;enumeration value="PLYDOC"/>
 *     &lt;enumeration value="PLYPHRM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ComplianceDetectedIssueCode")
@XmlEnum
public enum ComplianceDetectedIssueCode {

    COMPLY,
    DUPTHPY,
    PLYDOC,
    PLYPHRM;

    public String value() {
        return name();
    }

    public static ComplianceDetectedIssueCode fromValue(String v) {
        return valueOf(v);
    }

}
