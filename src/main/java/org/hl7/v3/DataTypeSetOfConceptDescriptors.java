
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataTypeSetOfConceptDescriptors.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DataTypeSetOfConceptDescriptors">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SET&lt;UVP&lt;CD>>"/>
 *     &lt;enumeration value="NPPD&lt;CD>"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DataTypeSetOfConceptDescriptors")
@XmlEnum
public enum DataTypeSetOfConceptDescriptors {

    @XmlEnumValue("SET<UVP<CD>>")
    SET_UVP_CD("SET<UVP<CD>>"),
    @XmlEnumValue("NPPD<CD>")
    NPPD_CD("NPPD<CD>");
    private final String value;

    DataTypeSetOfConceptDescriptors(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DataTypeSetOfConceptDescriptors fromValue(String v) {
        for (DataTypeSetOfConceptDescriptors c: DataTypeSetOfConceptDescriptors.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
