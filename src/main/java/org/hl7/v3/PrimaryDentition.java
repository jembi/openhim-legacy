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
 * <p>Java class for PrimaryDentition.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PrimaryDentition">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="TIDA"/>
 *     &lt;enumeration value="TIDB"/>
 *     &lt;enumeration value="TIDC"/>
 *     &lt;enumeration value="TIDD"/>
 *     &lt;enumeration value="TIDE"/>
 *     &lt;enumeration value="TIDF"/>
 *     &lt;enumeration value="TIDG"/>
 *     &lt;enumeration value="TIDH"/>
 *     &lt;enumeration value="TIDI"/>
 *     &lt;enumeration value="TIDJ"/>
 *     &lt;enumeration value="TIDK"/>
 *     &lt;enumeration value="TIDL"/>
 *     &lt;enumeration value="TIDM"/>
 *     &lt;enumeration value="TIDN"/>
 *     &lt;enumeration value="TIDO"/>
 *     &lt;enumeration value="TIDP"/>
 *     &lt;enumeration value="TIDQ"/>
 *     &lt;enumeration value="TIDR"/>
 *     &lt;enumeration value="TIDS"/>
 *     &lt;enumeration value="TIDT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PrimaryDentition")
@XmlEnum
public enum PrimaryDentition {

    TIDA,
    TIDB,
    TIDC,
    TIDD,
    TIDE,
    TIDF,
    TIDG,
    TIDH,
    TIDI,
    TIDJ,
    TIDK,
    TIDL,
    TIDM,
    TIDN,
    TIDO,
    TIDP,
    TIDQ,
    TIDR,
    TIDS,
    TIDT;

    public String value() {
        return name();
    }

    public static PrimaryDentition fromValue(String v) {
        return valueOf(v);
    }

}
