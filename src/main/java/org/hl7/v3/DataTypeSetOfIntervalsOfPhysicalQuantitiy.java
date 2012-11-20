/* 
 * Copyright 2012 Mohawk College of Applied Arts and Technology
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you 
 * may not use this file except in compliance with the License. You may 
 * obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations under 
 * the License.
 * 
 */

package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataTypeSetOfIntervalsOfPhysicalQuantitiy.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DataTypeSetOfIntervalsOfPhysicalQuantitiy">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SET&lt;UVP&lt;IVL&lt;PQ>>>"/>
 *     &lt;enumeration value="NPPD&lt;IVL&lt;PQ>>"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DataTypeSetOfIntervalsOfPhysicalQuantitiy")
@XmlEnum
public enum DataTypeSetOfIntervalsOfPhysicalQuantitiy {

    @XmlEnumValue("SET<UVP<IVL<PQ>>>")
    SET_UVP_IVL_PQ("SET<UVP<IVL<PQ>>>"),
    @XmlEnumValue("NPPD<IVL<PQ>>")
    NPPD_IVL_PQ("NPPD<IVL<PQ>>");
    private final String value;

    DataTypeSetOfIntervalsOfPhysicalQuantitiy(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DataTypeSetOfIntervalsOfPhysicalQuantitiy fromValue(String v) {
        for (DataTypeSetOfIntervalsOfPhysicalQuantitiy c: DataTypeSetOfIntervalsOfPhysicalQuantitiy.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
