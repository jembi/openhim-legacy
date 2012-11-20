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
 * <p>Java class for ParticipationFunction.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParticipationFunction">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ADMPHYS"/>
 *     &lt;enumeration value="ANRS"/>
 *     &lt;enumeration value="ANEST"/>
 *     &lt;enumeration value="ATTPHYS"/>
 *     &lt;enumeration value="DISPHYS"/>
 *     &lt;enumeration value="FASST"/>
 *     &lt;enumeration value="MDWF"/>
 *     &lt;enumeration value="NASST"/>
 *     &lt;enumeration value="PCP"/>
 *     &lt;enumeration value="PRISURG"/>
 *     &lt;enumeration value="RNDPHYS"/>
 *     &lt;enumeration value="SNRS"/>
 *     &lt;enumeration value="SASST"/>
 *     &lt;enumeration value="TASST"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ParticipationFunction")
@XmlEnum
public enum ParticipationFunction {

    ADMPHYS,
    ANRS,
    ANEST,
    ATTPHYS,
    DISPHYS,
    FASST,
    MDWF,
    NASST,
    PCP,
    PRISURG,
    RNDPHYS,
    SNRS,
    SASST,
    TASST;

    public String value() {
        return name();
    }

    public static ParticipationFunction fromValue(String v) {
        return valueOf(v);
    }

}
