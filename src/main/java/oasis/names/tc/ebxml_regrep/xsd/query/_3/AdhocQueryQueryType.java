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
 * <p>Java class for AdhocQueryQueryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AdhocQueryQueryType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}RegistryObjectQueryType">
 *       &lt;sequence>
 *         &lt;element name="QueryExpressionBranch" type="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}QueryExpressionBranchType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdhocQueryQueryType", propOrder = {
    "queryExpressionBranch"
})
public class AdhocQueryQueryType
    extends RegistryObjectQueryType
{

    @XmlElement(name = "QueryExpressionBranch")
    protected QueryExpressionBranchType queryExpressionBranch;

    /**
     * Gets the value of the queryExpressionBranch property.
     * 
     * @return
     *     possible object is
     *     {@link QueryExpressionBranchType }
     *     
     */
    public QueryExpressionBranchType getQueryExpressionBranch() {
        return queryExpressionBranch;
    }

    /**
     * Sets the value of the queryExpressionBranch property.
     * 
     * @param value
     *     allowed object is
     *     {@link QueryExpressionBranchType }
     *     
     */
    public void setQueryExpressionBranch(QueryExpressionBranchType value) {
        this.queryExpressionBranch = value;
    }

}
