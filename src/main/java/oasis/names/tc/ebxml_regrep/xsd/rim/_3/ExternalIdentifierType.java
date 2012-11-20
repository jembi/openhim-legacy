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


/**
 * 
 *         ExternalIdentifier is the mapping of the same named interface in ebRIM.
 *         It extends RegistryObject.
 *       
 * 
 * <p>Java class for ExternalIdentifierType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ExternalIdentifierType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}RegistryObjectType">
 *       &lt;attribute name="registryObject" use="required" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}referenceURI" />
 *       &lt;attribute name="identificationScheme" use="required" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}referenceURI" />
 *       &lt;attribute name="value" use="required" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExternalIdentifierType")
public class ExternalIdentifierType
    extends RegistryObjectType
{

    @XmlAttribute(name = "registryObject", required = true)
    protected String registryObject;
    @XmlAttribute(name = "identificationScheme", required = true)
    protected String identificationScheme;
    @XmlAttribute(name = "value", required = true)
    protected String value;

    /**
     * Gets the value of the registryObject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistryObject() {
        return registryObject;
    }

    /**
     * Sets the value of the registryObject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistryObject(String value) {
        this.registryObject = value;
    }

    /**
     * Gets the value of the identificationScheme property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificationScheme() {
        return identificationScheme;
    }

    /**
     * Sets the value of the identificationScheme property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificationScheme(String value) {
        this.identificationScheme = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

}
