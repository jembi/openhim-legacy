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
 * <p>Java class for EasternMiwok.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EasternMiwok">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-CSM"/>
 *     &lt;enumeration value="x-NSQ"/>
 *     &lt;enumeration value="x-PMW"/>
 *     &lt;enumeration value="x-SKD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EasternMiwok")
@XmlEnum
public enum EasternMiwok {

    @XmlEnumValue("x-CSM")
    X_CSM("x-CSM"),
    @XmlEnumValue("x-NSQ")
    X_NSQ("x-NSQ"),
    @XmlEnumValue("x-PMW")
    X_PMW("x-PMW"),
    @XmlEnumValue("x-SKD")
    X_SKD("x-SKD");
    private final String value;

    EasternMiwok(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EasternMiwok fromValue(String v) {
        for (EasternMiwok c: EasternMiwok.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
