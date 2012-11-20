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
 * <p>Java class for FirstFillPharmacySupplyType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FirstFillPharmacySupplyType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="FF"/>
 *     &lt;enumeration value="DF"/>
 *     &lt;enumeration value="FFC"/>
 *     &lt;enumeration value="FFP"/>
 *     &lt;enumeration value="TF"/>
 *     &lt;enumeration value="UD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FirstFillPharmacySupplyType")
@XmlEnum
public enum FirstFillPharmacySupplyType {

    FF,
    DF,
    FFC,
    FFP,
    TF,
    UD;

    public String value() {
        return name();
    }

    public static FirstFillPharmacySupplyType fromValue(String v) {
        return valueOf(v);
    }

}
