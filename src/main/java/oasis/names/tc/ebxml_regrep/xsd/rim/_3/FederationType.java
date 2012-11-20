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

package oasis.names.tc.ebxml_regrep.xsd.rim._3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.Duration;


/**
 * Mapping of the same named interface in ebRIM.
 * 
 * <p>Java class for FederationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FederationType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}RegistryObjectType">
 *       &lt;attribute name="replicationSyncLatency" type="{http://www.w3.org/2001/XMLSchema}duration" default="P1D" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FederationType")
public class FederationType
    extends RegistryObjectType
{

    @XmlAttribute(name = "replicationSyncLatency")
    protected Duration replicationSyncLatency;

    /**
     * Gets the value of the replicationSyncLatency property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getReplicationSyncLatency() {
        return replicationSyncLatency;
    }

    /**
     * Sets the value of the replicationSyncLatency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setReplicationSyncLatency(Duration value) {
        this.replicationSyncLatency = value;
    }

}
