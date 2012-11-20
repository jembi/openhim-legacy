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
 * <p>Java class for ActBillingArrangementCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActBillingArrangementCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="BLK"/>
 *     &lt;enumeration value="CAP"/>
 *     &lt;enumeration value="CONTF"/>
 *     &lt;enumeration value="FFS"/>
 *     &lt;enumeration value="FINBILL"/>
 *     &lt;enumeration value="ROST"/>
 *     &lt;enumeration value="SESS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActBillingArrangementCode")
@XmlEnum
public enum ActBillingArrangementCode {

    BLK,
    CAP,
    CONTF,
    FFS,
    FINBILL,
    ROST,
    SESS;

    public String value() {
        return name();
    }

    public static ActBillingArrangementCode fromValue(String v) {
        return valueOf(v);
    }

}
