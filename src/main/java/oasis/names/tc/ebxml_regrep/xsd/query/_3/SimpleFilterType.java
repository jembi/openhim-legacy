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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for SimpleFilterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SimpleFilterType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}FilterType">
 *       &lt;attribute name="domainAttribute" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="comparator" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NCName">
 *             &lt;enumeration value="LE"/>
 *             &lt;enumeration value="LT"/>
 *             &lt;enumeration value="GE"/>
 *             &lt;enumeration value="GT"/>
 *             &lt;enumeration value="EQ"/>
 *             &lt;enumeration value="NE"/>
 *             &lt;enumeration value="Like"/>
 *             &lt;enumeration value="NotLike"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SimpleFilterType")
@XmlSeeAlso({
    DateTimeFilterType.class,
    BooleanFilterType.class,
    IntegerFilterType.class,
    StringFilterType.class,
    FloatFilterType.class
})
public abstract class SimpleFilterType
    extends FilterType
{

    @XmlAttribute(name = "domainAttribute", required = true)
    protected String domainAttribute;
    @XmlAttribute(name = "comparator", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String comparator;

    /**
     * Gets the value of the domainAttribute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainAttribute() {
        return domainAttribute;
    }

    /**
     * Sets the value of the domainAttribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainAttribute(String value) {
        this.domainAttribute = value;
    }

    /**
     * Gets the value of the comparator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComparator() {
        return comparator;
    }

    /**
     * Sets the value of the comparator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComparator(String value) {
        this.comparator = value;
    }

}
