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

package oasis.names.tc.ebxml_regrep.xsd.rs._3;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the oasis.names.tc.ebxml_regrep.xsd.rs._3 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RegistryRequest_QNAME = new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0", "RegistryRequest");
    private final static QName _RegistryResponse_QNAME = new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0", "RegistryResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: oasis.names.tc.ebxml_regrep.xsd.rs._3
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RegistryResponseType }
     * 
     */
    public RegistryResponseType createRegistryResponseType() {
        return new RegistryResponseType();
    }

    /**
     * Create an instance of {@link RegistryErrorList }
     * 
     */
    public RegistryErrorList createRegistryErrorList() {
        return new RegistryErrorList();
    }

    /**
     * Create an instance of {@link RegistryError }
     * 
     */
    public RegistryError createRegistryError() {
        return new RegistryError();
    }

    /**
     * Create an instance of {@link RegistryRequestType }
     * 
     */
    public RegistryRequestType createRegistryRequestType() {
        return new RegistryRequestType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegistryRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0", name = "RegistryRequest")
    public JAXBElement<RegistryRequestType> createRegistryRequest(RegistryRequestType value) {
        return new JAXBElement<RegistryRequestType>(_RegistryRequest_QNAME, RegistryRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegistryResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0", name = "RegistryResponse")
    public JAXBElement<RegistryResponseType> createRegistryResponse(RegistryResponseType value) {
        return new JAXBElement<RegistryResponseType>(_RegistryResponse_QNAME, RegistryResponseType.class, null, value);
    }

}
