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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UnitOfMeasurePrefixInsens.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="UnitOfMeasurePrefixInsens">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="A"/>
 *     &lt;enumeration value="C"/>
 *     &lt;enumeration value="D"/>
 *     &lt;enumeration value="DA"/>
 *     &lt;enumeration value="EX"/>
 *     &lt;enumeration value="F"/>
 *     &lt;enumeration value="GIB"/>
 *     &lt;enumeration value="GA"/>
 *     &lt;enumeration value="H"/>
 *     &lt;enumeration value="KIB"/>
 *     &lt;enumeration value="K"/>
 *     &lt;enumeration value="MIB"/>
 *     &lt;enumeration value="MA"/>
 *     &lt;enumeration value="U"/>
 *     &lt;enumeration value="M"/>
 *     &lt;enumeration value="N"/>
 *     &lt;enumeration value="PT"/>
 *     &lt;enumeration value="P"/>
 *     &lt;enumeration value="TIB"/>
 *     &lt;enumeration value="TR"/>
 *     &lt;enumeration value="YO"/>
 *     &lt;enumeration value="YA"/>
 *     &lt;enumeration value="ZO"/>
 *     &lt;enumeration value="ZA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "UnitOfMeasurePrefixInsens")
@XmlEnum
public enum UnitOfMeasurePrefixInsens {

    A,
    C,
    D,
    DA,
    EX,
    F,
    GIB,
    GA,
    H,
    KIB,
    K,
    MIB,
    MA,
    U,
    M,
    N,
    PT,
    P,
    TIB,
    TR,
    YO,
    YA,
    ZO,
    ZA;

    public String value() {
        return name();
    }

    public static UnitOfMeasurePrefixInsens fromValue(String v) {
        return valueOf(v);
    }

}
