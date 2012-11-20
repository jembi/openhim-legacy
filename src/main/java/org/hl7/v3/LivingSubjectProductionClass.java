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
 * <p>Java class for LivingSubjectProductionClass.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LivingSubjectProductionClass">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="BF"/>
 *     &lt;enumeration value="BR"/>
 *     &lt;enumeration value="BL"/>
 *     &lt;enumeration value="CO"/>
 *     &lt;enumeration value="DA"/>
 *     &lt;enumeration value="DR"/>
 *     &lt;enumeration value="DU"/>
 *     &lt;enumeration value="FI"/>
 *     &lt;enumeration value="LY"/>
 *     &lt;enumeration value="MT"/>
 *     &lt;enumeration value="MU"/>
 *     &lt;enumeration value="PL"/>
 *     &lt;enumeration value="RC"/>
 *     &lt;enumeration value="SH"/>
 *     &lt;enumeration value="VL"/>
 *     &lt;enumeration value="WL"/>
 *     &lt;enumeration value="WO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LivingSubjectProductionClass")
@XmlEnum
public enum LivingSubjectProductionClass {

    BF,
    BR,
    BL,
    CO,
    DA,
    DR,
    DU,
    FI,
    LY,
    MT,
    MU,
    PL,
    RC,
    SH,
    VL,
    WL,
    WO;

    public String value() {
        return name();
    }

    public static LivingSubjectProductionClass fromValue(String v) {
        return valueOf(v);
    }

}
