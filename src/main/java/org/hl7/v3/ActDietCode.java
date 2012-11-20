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
 * <p>Java class for ActDietCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActDietCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="BR"/>
 *     &lt;enumeration value="DM"/>
 *     &lt;enumeration value="FAST"/>
 *     &lt;enumeration value="GF"/>
 *     &lt;enumeration value="LQ"/>
 *     &lt;enumeration value="LF"/>
 *     &lt;enumeration value="LP"/>
 *     &lt;enumeration value="LS"/>
 *     &lt;enumeration value="VLI"/>
 *     &lt;enumeration value="NF"/>
 *     &lt;enumeration value="N"/>
 *     &lt;enumeration value="PAR"/>
 *     &lt;enumeration value="PAF"/>
 *     &lt;enumeration value="RD"/>
 *     &lt;enumeration value="SCH"/>
 *     &lt;enumeration value="T"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActDietCode")
@XmlEnum
public enum ActDietCode {

    BR,
    DM,
    FAST,
    GF,
    LQ,
    LF,
    LP,
    LS,
    VLI,
    NF,
    N,
    PAR,
    PAF,
    RD,
    SCH,
    T;

    public String value() {
        return name();
    }

    public static ActDietCode fromValue(String v) {
        return valueOf(v);
    }

}
