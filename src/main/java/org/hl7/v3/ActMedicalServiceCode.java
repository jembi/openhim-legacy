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
 * <p>Java class for ActMedicalServiceCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActMedicalServiceCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ALC"/>
 *     &lt;enumeration value="CARD"/>
 *     &lt;enumeration value="CHR"/>
 *     &lt;enumeration value="DNTL"/>
 *     &lt;enumeration value="DRGRHB"/>
 *     &lt;enumeration value="GENRL"/>
 *     &lt;enumeration value="MED"/>
 *     &lt;enumeration value="OBS"/>
 *     &lt;enumeration value="ONC"/>
 *     &lt;enumeration value="PALL"/>
 *     &lt;enumeration value="PED"/>
 *     &lt;enumeration value="PHAR"/>
 *     &lt;enumeration value="PHYRHB"/>
 *     &lt;enumeration value="PSYCH"/>
 *     &lt;enumeration value="SURG"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActMedicalServiceCode")
@XmlEnum
public enum ActMedicalServiceCode {

    ALC,
    CARD,
    CHR,
    DNTL,
    DRGRHB,
    GENRL,
    MED,
    OBS,
    ONC,
    PALL,
    PED,
    PHAR,
    PHYRHB,
    PSYCH,
    SURG;

    public String value() {
        return name();
    }

    public static ActMedicalServiceCode fromValue(String v) {
        return valueOf(v);
    }

}
