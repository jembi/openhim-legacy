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
 * <p>Java class for RealmOfUse.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RealmOfUse">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="Canada"/>
 *     &lt;enumeration value="NorthAmerica"/>
 *     &lt;enumeration value="USA"/>
 *     &lt;enumeration value="UV"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RealmOfUse")
@XmlEnum
public enum RealmOfUse {

    @XmlEnumValue("Canada")
    CANADA("Canada"),
    @XmlEnumValue("NorthAmerica")
    NORTH_AMERICA("NorthAmerica"),
    USA("USA"),
    UV("UV");
    private final String value;

    RealmOfUse(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RealmOfUse fromValue(String v) {
        for (RealmOfUse c: RealmOfUse.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
