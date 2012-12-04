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
 */
package ca.marc.ihe.xds;

import java.util.Arrays;
import java.util.UUID;

import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;

public class XDSUtil {

	/**
	 * Create registry package
	 */
	public static RegistryPackageType createRegistryPackage()
	{
		RegistryPackageType retVal = new RegistryPackageType();
		retVal.setId(String.format("urn:uuid:%s", UUID.randomUUID().toString()));
		return retVal;
	}
	
	/**
	 * Create Extrinsic object
	 */
	public static ExtrinsicObjectType createExtrinsicObject(String mimeType, String name, XdsGuidType objectType)
	{

		ExtrinsicObjectType retVal = new ExtrinsicObjectType();
		retVal.setId(String.format("urn:uuid:%s", UUID.randomUUID().toString()));
		retVal.setObjectType(objectType.toString());
		retVal.setMimeType(mimeType);
		
		InternationalStringType localName = new InternationalStringType();
		LocalizedStringType stringValue = new LocalizedStringType();
		stringValue.setValue(name);
		localName.getLocalizedString().add(stringValue);
		retVal.setName(localName);
		
		return retVal;
	}
	
    /**
     * Create a query slot
     */
    public static SlotType1 createSlot(String slotName, String... value)
    {
    	SlotType1 retSlot = new SlotType1();
        ValueListType patientValueList = new ValueListType();
        patientValueList.getValue().addAll(Arrays.asList(value));
        retSlot.setName(slotName);
        retSlot.setValueList(patientValueList);
        return retSlot;
    }
    
	/**
	 * Create association type
	 */
	public static AssociationType1 createAssociation(RegistryObjectType source,
			ExtrinsicObjectType target, String status) {
		AssociationType1 retAssoc = new AssociationType1();
		retAssoc.setId(String.format("urn:uuid:%s", UUID.randomUUID().toString()));
		retAssoc.setSourceObject(source.getId());
		retAssoc.setTargetObject(target.getId());
		retAssoc.getSlot().add(createSlot("SubmissionSetStatus", status));
		return retAssoc;
	}

    /**
     * Create classification object
     */
    public static ClassificationType createClassification(RegistryObjectType classifiedObject, XdsGuidType scheme, String nodeRepresentation, String name, SlotType1... slots)
    {

    	ClassificationType retClass = new ClassificationType();
    	retClass.setId(String.format("urn:uuid:%s", UUID.randomUUID().toString()));
    	retClass.setClassificationScheme(scheme.toString());
    	retClass.setClassifiedObject(classifiedObject.getId());
    	if (nodeRepresentation!=null) retClass.setNodeRepresentation(nodeRepresentation);
    	retClass.getSlot().addAll(Arrays.asList(slots));
    	
    	if (name!=null) {
	    	InternationalStringType localName = new InternationalStringType();
			LocalizedStringType stringValue = new LocalizedStringType();
			stringValue.setValue(name);
			localName.getLocalizedString().add(stringValue);
			retClass.setName(localName);
    	}
		
    	return retClass;
    }
    
    /**
	 * Create node classification
	 * @param registryObject
	 * @param classificationNode
	 * @return
	 */
	public static ClassificationType createNodeClassification(RegistryObjectType registryObject, XdsGuidType classificationNode)
	{
		ClassificationType retClass = new ClassificationType();
		retClass.setId(String.format("urn:uuid:%s", UUID.randomUUID()));
		retClass.setClassificationNode(classificationNode.toString());
		retClass.setClassifiedObject(registryObject.getId());
		return retClass;
	}
	
    /**
     * Create external identifier
     */
    public static ExternalIdentifierType createExternalIdentifier(RegistryObjectType registryObject, XdsGuidType scheme, String value)
    {

    	ExternalIdentifierType retId = new ExternalIdentifierType();
    	retId.setId(String.format("urn:uuid:%s", UUID.randomUUID().toString()));
    	
    	retId.setRegistryObject(registryObject.getId());
    	retId.setIdentificationScheme(scheme.toString());
    	retId.setValue(value);
    	
		InternationalStringType localName = new InternationalStringType();
		LocalizedStringType stringValue = new LocalizedStringType();
		stringValue.setValue(scheme.getName());
		localName.getLocalizedString().add(stringValue);
		retId.setName(localName);
		
		return retId;
    }
}
