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
 * <p>Java class for GTSAbbreviationHolidaysUSNational.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="GTSAbbreviationHolidaysUSNational">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="JHNUS"/>
 *     &lt;enumeration value="JHNUSCLM"/>
 *     &lt;enumeration value="JHNUSIND"/>
 *     &lt;enumeration value="JHNUSIND1"/>
 *     &lt;enumeration value="JHNUSIND5"/>
 *     &lt;enumeration value="JHNUSLBR"/>
 *     &lt;enumeration value="JHNUSMEM"/>
 *     &lt;enumeration value="JHNUSMEM5"/>
 *     &lt;enumeration value="JHNUSMEM6"/>
 *     &lt;enumeration value="JHNUSMLK"/>
 *     &lt;enumeration value="JHNUSPRE"/>
 *     &lt;enumeration value="JHNUSTKS"/>
 *     &lt;enumeration value="JHNUSTKS5"/>
 *     &lt;enumeration value="JHNUSVET"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "GTSAbbreviationHolidaysUSNational")
@XmlEnum
public enum GTSAbbreviationHolidaysUSNational {

    JHNUS("JHNUS"),
    JHNUSCLM("JHNUSCLM"),
    JHNUSIND("JHNUSIND"),
    @XmlEnumValue("JHNUSIND1")
    JHNUSIND_1("JHNUSIND1"),
    @XmlEnumValue("JHNUSIND5")
    JHNUSIND_5("JHNUSIND5"),
    JHNUSLBR("JHNUSLBR"),
    JHNUSMEM("JHNUSMEM"),
    @XmlEnumValue("JHNUSMEM5")
    JHNUSMEM_5("JHNUSMEM5"),
    @XmlEnumValue("JHNUSMEM6")
    JHNUSMEM_6("JHNUSMEM6"),
    JHNUSMLK("JHNUSMLK"),
    JHNUSPRE("JHNUSPRE"),
    JHNUSTKS("JHNUSTKS"),
    @XmlEnumValue("JHNUSTKS5")
    JHNUSTKS_5("JHNUSTKS5"),
    JHNUSVET("JHNUSVET");
    private final String value;

    GTSAbbreviationHolidaysUSNational(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GTSAbbreviationHolidaysUSNational fromValue(String v) {
        for (GTSAbbreviationHolidaysUSNational c: GTSAbbreviationHolidaysUSNational.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
