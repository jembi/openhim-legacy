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

package oasis.names.tc.ebxml_regrep.xsd.query._3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExternalIdentifierQueryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ExternalIdentifierQueryType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}RegistryObjectQueryType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}RegistryObjectQuery" minOccurs="0"/>
 *         &lt;element name="IdentificationSchemeQuery" type="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}ClassificationSchemeQueryType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExternalIdentifierQueryType", propOrder = {
    "registryObjectQuery",
    "identificationSchemeQuery"
})
public class ExternalIdentifierQueryType
    extends RegistryObjectQueryType
{

    @XmlElement(name = "RegistryObjectQuery")
    protected RegistryObjectQueryType registryObjectQuery;
    @XmlElement(name = "IdentificationSchemeQuery")
    protected ClassificationSchemeQueryType identificationSchemeQuery;

    /**
     * Gets the value of the registryObjectQuery property.
     * 
     * @return
     *     possible object is
     *     {@link RegistryObjectQueryType }
     *     
     */
    public RegistryObjectQueryType getRegistryObjectQuery() {
        return registryObjectQuery;
    }

    /**
     * Sets the value of the registryObjectQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistryObjectQueryType }
     *     
     */
    public void setRegistryObjectQuery(RegistryObjectQueryType value) {
        this.registryObjectQuery = value;
    }

    /**
     * Gets the value of the identificationSchemeQuery property.
     * 
     * @return
     *     possible object is
     *     {@link ClassificationSchemeQueryType }
     *     
     */
    public ClassificationSchemeQueryType getIdentificationSchemeQuery() {
        return identificationSchemeQuery;
    }

    /**
     * Sets the value of the identificationSchemeQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClassificationSchemeQueryType }
     *     
     */
    public void setIdentificationSchemeQuery(ClassificationSchemeQueryType value) {
        this.identificationSchemeQuery = value;
    }

}
