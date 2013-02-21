
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActBillingArrangementCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActBillingArrangementCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="BLK"/>
 *     &lt;enumeration value="CAP"/>
 *     &lt;enumeration value="CONTF"/>
 *     &lt;enumeration value="FFS"/>
 *     &lt;enumeration value="FINBILL"/>
 *     &lt;enumeration value="ROST"/>
 *     &lt;enumeration value="SESS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActBillingArrangementCode")
@XmlEnum
public enum ActBillingArrangementCode {

    BLK,
    CAP,
    CONTF,
    FFS,
    FINBILL,
    ROST,
    SESS;

    public String value() {
        return name();
    }

    public static ActBillingArrangementCode fromValue(String v) {
        return valueOf(v);
    }

}
