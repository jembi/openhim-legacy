
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActClassSpecimenCollection.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActClassSpecimenCollection">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SPECCOLLECT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActClassSpecimenCollection")
@XmlEnum
public enum ActClassSpecimenCollection {

    SPECCOLLECT;

    public String value() {
        return name();
    }

    public static ActClassSpecimenCollection fromValue(String v) {
        return valueOf(v);
    }

}
