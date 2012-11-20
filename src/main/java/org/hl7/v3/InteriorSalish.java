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
 * <p>Java class for InteriorSalish.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="InteriorSalish">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-CRD"/>
 *     &lt;enumeration value="x-COL"/>
 *     &lt;enumeration value="x-FLA"/>
 *     &lt;enumeration value="x-OKA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "InteriorSalish")
@XmlEnum
public enum InteriorSalish {

    @XmlEnumValue("x-CRD")
    X_CRD("x-CRD"),
    @XmlEnumValue("x-COL")
    X_COL("x-COL"),
    @XmlEnumValue("x-FLA")
    X_FLA("x-FLA"),
    @XmlEnumValue("x-OKA")
    X_OKA("x-OKA");
    private final String value;

    InteriorSalish(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static InteriorSalish fromValue(String v) {
        for (InteriorSalish c: InteriorSalish.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
