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
 * <p>Java class for TableCellScope.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TableCellScope">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="col"/>
 *     &lt;enumeration value="colgroup"/>
 *     &lt;enumeration value="row"/>
 *     &lt;enumeration value="rowgroup"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TableCellScope")
@XmlEnum
public enum TableCellScope {

    @XmlEnumValue("col")
    COL("col"),
    @XmlEnumValue("colgroup")
    COLGROUP("colgroup"),
    @XmlEnumValue("row")
    ROW("row"),
    @XmlEnumValue("rowgroup")
    ROWGROUP("rowgroup");
    private final String value;

    TableCellScope(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TableCellScope fromValue(String v) {
        for (TableCellScope c: TableCellScope.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
