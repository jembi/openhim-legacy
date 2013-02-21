
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FDALabelData.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FDALabelData">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="FDACOATING"/>
 *     &lt;enumeration value="FDACOLOR"/>
 *     &lt;enumeration value="FDAIMPRINTCD"/>
 *     &lt;enumeration value="FDALOGO"/>
 *     &lt;enumeration value="FDASCORING"/>
 *     &lt;enumeration value="FDASHAPE"/>
 *     &lt;enumeration value="FDASIZE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FDALabelData")
@XmlEnum
public enum FDALabelData {

    FDACOATING,
    FDACOLOR,
    FDAIMPRINTCD,
    FDALOGO,
    FDASCORING,
    FDASHAPE,
    FDASIZE;

    public String value() {
        return name();
    }

    public static FDALabelData fromValue(String v) {
        return valueOf(v);
    }

}
