/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.ResponseOptionType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;

import org.jembi.rhea.Constants;
import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.Util;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import ca.marc.ihe.xds.XdsGuidType;

/**
 * XDS ITI-18 Registry Stored Query
 */
public class XDSRegistryStoredQuery extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
			
		RestfulHttpRequest restRequest = (RestfulHttpRequest) message.getPayload();
		
		String path = restRequest.getPath();
		int beginIndex = path.indexOf("patient/") + 8;
		int endIndex = path.indexOf("/encounters");
		String id_str = path.substring(beginIndex, endIndex);
		
		String[] identifer = Util.splitIdentifer(id_str);
		
		String idType = identifer[0];
		String id = identifer[1];
		
		Map<String, String> requestParams = restRequest.getRequestParams();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String startDate_str = requestParams.get(Constants.QUERY_ENC_START_DATE_PARAM);
		String endDate_str = requestParams.get(Constants.QUERY_ENC_END_DATE_PARAM);
		Date startDate = null;
		Date endDate = null;
		try {
			if (startDate_str != null) {
				startDate = sdf.parse(startDate_str);
			}
			if (endDate_str != null) {
				endDate = sdf.parse(endDate_str);
			}
		} catch (ParseException e) {
			throw new TransformerException(this, e);
		}
		
		String notificationType = requestParams.get(Constants.QUERY_ENC_NOTIFICATION_TYPE_PARAM);
		String ELID = requestParams.get(Constants.QUERY_ENC_ELID_PARAM);
		
		// construct AdhocQueryRequest
		AdhocQueryRequest request = new AdhocQueryRequest();
		
		// Setup response option
        ResponseOptionType responseOption = new ResponseOptionType();
        responseOption.setReturnType("LeafClass");
        responseOption.setReturnComposedObjects(true);
        request.setResponseOption(responseOption);
        
        // Setup the ad-hoc query
        AdhocQueryType adhocQuery = new AdhocQueryType();
        // set query type to find documents, by specific urn
        adhocQuery.setId(String.format("urn:uuid:%s", "14d4debf-8f97-4251-9a74-a90016b0af0d"));
        
        // Slots, first setup slot for patient ID
        // TODO get this from the PIX query
        String idOid = "1.19.6.24.109.42.1.3";
        // TODO HARD CODED REMOVE
        id = "1b48e083395f498";
        String srcPatientId = String.format("'%s^^^&%s&ISO'", id, idOid);
        adhocQuery.getSlot().add(createQuerySlot("$XDSDocumentEntryPatientId", srcPatientId));
        
        // Setup status slot 
        adhocQuery.getSlot().add(createQuerySlot("$XDSDocumentEntryStatus", "('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')"));
        
        // From time
        SimpleDateFormat hl7DateFormat = new SimpleDateFormat("yyyyMMddHHmm"); 
        if(startDate != null)
        	adhocQuery.getSlot().add(createQuerySlot("$XDSDocumentEntryCreationTimeFrom", hl7DateFormat.format(startDate)));
        if(endDate != null)
        	adhocQuery.getSlot().add(createQuerySlot("$XDSDocumentEntryCreationTimeTo", hl7DateFormat.format(endDate)));
        
        // Append the ad-hoc query to the request 
        request.setAdhocQuery(adhocQuery);
		
		//TODO
		// add request to session prop so that we can access it when processing the response
		//message.setSessionProperty("XDS-ITI-18", null);
		//message.setSessionProperty("XDS-ITI-18_uniqueId", null);
		//message.setSessionProperty("XDS-ITI-18_patientId", null);
		
		return request;
	}
	
	//TODO util functions can be moved to a utility class (somewhere in org.jembi.ihe.xds)
	
    /**
     * Create a query slot
     */
    private static SlotType1 createSlot(String slotName, String... value)
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
	private static AssociationType1 createAssociation(RegistryObjectType source,
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
    private static ClassificationType createClassification(RegistryObjectType classifiedObject, XdsGuidType scheme, String nodeRepresentation, String name, SlotType1... slots)
    {

    	ClassificationType retClass = new ClassificationType();
    	retClass.setId(String.format("urn:uuid:%s", UUID.randomUUID().toString()));
    	retClass.setClassificationScheme(scheme.toString());
    	retClass.setClassifiedObject(classifiedObject.getId());
    	retClass.setNodeRepresentation(nodeRepresentation);
    	retClass.getSlot().addAll(Arrays.asList(slots));
    	
    	InternationalStringType localName = new InternationalStringType();
		LocalizedStringType stringValue = new LocalizedStringType();
		stringValue.setValue(name);
		localName.getLocalizedString().add(stringValue);
		retClass.setName(localName);
		
    	return retClass;
    }
    
    /**
	 * Create node classification
	 * @param registryObject
	 * @param classificationNode
	 * @return
	 */
	private static ClassificationType createNodeClassification(RegistryObjectType registryObject, XdsGuidType classificationNode)
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
    private static ExternalIdentifierType createExternalIdentifier(RegistryObjectType registryObject, XdsGuidType scheme, String value)
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
    
    /**
     * Create a query slot
     */
    private SlotType1 createQuerySlot(String slotName, String... value)
    {
    	SlotType1 retSlot = new SlotType1();
        ValueListType patientValueList = new ValueListType();
        patientValueList.getValue().addAll(Arrays.asList(value));
        retSlot.setName(slotName);
        retSlot.setValueList(patientValueList);
        return retSlot;
    }

}