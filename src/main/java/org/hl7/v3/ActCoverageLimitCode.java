
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActCoverageLimitCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActCoverageLimitCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="NETAMT"/>
 *     &lt;enumeration value="UNITPRICE"/>
 *     &lt;enumeration value="UNITQTY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActCoverageLimitCode")
@XmlEnum
public enum ActCoverageLimitCode {

    NETAMT,
    UNITPRICE,
    UNITQTY;

    public String value() {
        return name();
    }

    public static ActCoverageLimitCode fromValue(String v) {
        return valueOf(v);
    }

}
