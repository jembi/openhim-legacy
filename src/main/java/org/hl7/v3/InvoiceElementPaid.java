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
 * <p>Java class for InvoiceElementPaid.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="InvoiceElementPaid">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="PDNPPPELAT"/>
 *     &lt;enumeration value="PDNPPPELCT"/>
 *     &lt;enumeration value="PDNPPPMNAT"/>
 *     &lt;enumeration value="PDNPPPMNCT"/>
 *     &lt;enumeration value="PDNPSPELAT"/>
 *     &lt;enumeration value="PDNPSPELCT"/>
 *     &lt;enumeration value="PDNPSPMNAT"/>
 *     &lt;enumeration value="PDNPSPMNCT"/>
 *     &lt;enumeration value="PDNFPPELAT"/>
 *     &lt;enumeration value="PDNFPPELCT"/>
 *     &lt;enumeration value="PDNFPPMNAT"/>
 *     &lt;enumeration value="PDNFPPMNCT"/>
 *     &lt;enumeration value="PDNFSPELAT"/>
 *     &lt;enumeration value="PDNFSPELCT"/>
 *     &lt;enumeration value="PDNFSPMNAT"/>
 *     &lt;enumeration value="PDNFSPMNCT"/>
 *     &lt;enumeration value="PDPPPPELAT"/>
 *     &lt;enumeration value="PDPPPPELCT"/>
 *     &lt;enumeration value="PDPPPPMNAT"/>
 *     &lt;enumeration value="PDPPPPMNCT"/>
 *     &lt;enumeration value="PDPPSPELAT"/>
 *     &lt;enumeration value="PDPPSPELCT"/>
 *     &lt;enumeration value="PDPPSPMNAT"/>
 *     &lt;enumeration value="PDPPSPMNCT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "InvoiceElementPaid")
@XmlEnum
public enum InvoiceElementPaid {

    PDNPPPELAT,
    PDNPPPELCT,
    PDNPPPMNAT,
    PDNPPPMNCT,
    PDNPSPELAT,
    PDNPSPELCT,
    PDNPSPMNAT,
    PDNPSPMNCT,
    PDNFPPELAT,
    PDNFPPELCT,
    PDNFPPMNAT,
    PDNFPPMNCT,
    PDNFSPELAT,
    PDNFSPELCT,
    PDNFSPMNAT,
    PDNFSPMNCT,
    PDPPPPELAT,
    PDPPPPELCT,
    PDPPPPMNAT,
    PDPPPPMNCT,
    PDPPSPELAT,
    PDPPSPELCT,
    PDPPSPMNAT,
    PDPPSPMNCT;

    public String value() {
        return name();
    }

    public static InvoiceElementPaid fromValue(String v) {
        return valueOf(v);
    }

}
