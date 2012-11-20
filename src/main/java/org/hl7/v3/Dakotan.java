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
 * <p>Java class for Dakotan.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Dakotan">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-ASB"/>
 *     &lt;enumeration value="x-DHG"/>
 *     &lt;enumeration value="x-LKT"/>
 *     &lt;enumeration value="x-NKT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Dakotan")
@XmlEnum
public enum Dakotan {

    @XmlEnumValue("x-ASB")
    X_ASB("x-ASB"),
    @XmlEnumValue("x-DHG")
    X_DHG("x-DHG"),
    @XmlEnumValue("x-LKT")
    X_LKT("x-LKT"),
    @XmlEnumValue("x-NKT")
    X_NKT("x-NKT");
    private final String value;

    Dakotan(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Dakotan fromValue(String v) {
        for (Dakotan c: Dakotan.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
