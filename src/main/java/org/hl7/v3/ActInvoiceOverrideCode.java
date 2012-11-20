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
 * <p>Java class for ActInvoiceOverrideCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActInvoiceOverrideCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="COVGE"/>
 *     &lt;enumeration value="PYRDELAY"/>
 *     &lt;enumeration value="EFORM"/>
 *     &lt;enumeration value="FAX"/>
 *     &lt;enumeration value="GFTH"/>
 *     &lt;enumeration value="LATE"/>
 *     &lt;enumeration value="MANUAL"/>
 *     &lt;enumeration value="ORTHO"/>
 *     &lt;enumeration value="OOJ"/>
 *     &lt;enumeration value="PAPER"/>
 *     &lt;enumeration value="PIE"/>
 *     &lt;enumeration value="REFNR"/>
 *     &lt;enumeration value="REPSERV"/>
 *     &lt;enumeration value="UNRELAT"/>
 *     &lt;enumeration value="VERBAUTH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActInvoiceOverrideCode")
@XmlEnum
public enum ActInvoiceOverrideCode {

    COVGE,
    PYRDELAY,
    EFORM,
    FAX,
    GFTH,
    LATE,
    MANUAL,
    ORTHO,
    OOJ,
    PAPER,
    PIE,
    REFNR,
    REPSERV,
    UNRELAT,
    VERBAUTH;

    public String value() {
        return name();
    }

    public static ActInvoiceOverrideCode fromValue(String v) {
        return valueOf(v);
    }

}
