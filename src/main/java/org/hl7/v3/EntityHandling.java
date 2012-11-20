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
 * <p>Java class for EntityHandling.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EntityHandling">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="AMB"/>
 *     &lt;enumeration value="C37"/>
 *     &lt;enumeration value="CAMB"/>
 *     &lt;enumeration value="CFRZ"/>
 *     &lt;enumeration value="CREF"/>
 *     &lt;enumeration value="DFRZ"/>
 *     &lt;enumeration value="MTLF"/>
 *     &lt;enumeration value="CATM"/>
 *     &lt;enumeration value="PRTL"/>
 *     &lt;enumeration value="REF"/>
 *     &lt;enumeration value="SBU"/>
 *     &lt;enumeration value="UFRZ"/>
 *     &lt;enumeration value="PSA"/>
 *     &lt;enumeration value="DRY"/>
 *     &lt;enumeration value="FRZ"/>
 *     &lt;enumeration value="NTR"/>
 *     &lt;enumeration value="PSO"/>
 *     &lt;enumeration value="UPR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EntityHandling")
@XmlEnum
public enum EntityHandling {

    AMB("AMB"),
    @XmlEnumValue("C37")
    C_37("C37"),
    CAMB("CAMB"),
    CFRZ("CFRZ"),
    CREF("CREF"),
    DFRZ("DFRZ"),
    MTLF("MTLF"),
    CATM("CATM"),
    PRTL("PRTL"),
    REF("REF"),
    SBU("SBU"),
    UFRZ("UFRZ"),
    PSA("PSA"),
    DRY("DRY"),
    FRZ("FRZ"),
    NTR("NTR"),
    PSO("PSO"),
    UPR("UPR");
    private final String value;

    EntityHandling(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EntityHandling fromValue(String v) {
        for (EntityHandling c: EntityHandling.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
