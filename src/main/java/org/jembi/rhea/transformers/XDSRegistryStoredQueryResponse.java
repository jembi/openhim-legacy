/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import ihe.iti.atna.AuditMessage;
import ihe.iti.atna.EventIdentificationType;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;

import org.jembi.ihe.atna.ATNAUtil;
import org.jembi.rhea.RestfulHttpRequest;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.client.MuleClient;
import org.mule.transformer.AbstractMessageTransformer;

/**
 * Handle XDS ITI-18 Registry Stored Query response
 */
public class XDSRegistryStoredQueryResponse extends AbstractMessageTransformer {
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		try {
			// process response					
			boolean outcome = false;

			AdhocQueryResponse response = (AdhocQueryResponse) message.getPayload();
			
		    // get a list of doc unique id separated by "~"
			String docUniqueId = getDocUniqueId(response);
			String repositoryUniqueId = getRepositoryUniqueId(response);
		
			//generate audit message
			String request = (String)message.getSessionProperty("XDS-ITI-18");
			String uniqueId = (String)message.getSessionProperty("XDS-ITI-18_uniqueId");
			String patientId = (String)message.getSessionProperty("XDS-ITI-18_patientId");
			
			String at = generateATNAMessage(request, patientId, uniqueId, outcome); //??need to log the response message instead of the request??
			MuleClient client = new MuleClient(muleContext);
			at = ATNAUtil.build_TCP_Msg_header() + at;
			client.dispatch("vm://atna_auditing", at.length() + " " + at, null);
			
			// return the list of document unique id's found in RestfulHttpRequest format
			RestfulHttpRequest payload = new RestfulHttpRequest();
			payload.setPath("ws/rest/v1/retrieve_document_set/?patient_id= "+ patientId + "&document_unique_id=" + docUniqueId + "&repository_unique_id=" + repositoryUniqueId);			
			
			return payload;
		} catch (JAXBException e) {
			throw new TransformerException(this, e);
		} catch (MuleException e) {
			throw new TransformerException(this, e);
		}
	}

    private String getDocUniqueId(AdhocQueryResponse aqResponse)  {
        String uniqueDocId = null;
        
        if (aqResponse.getRegistryObjectList() != null) {
            RegistryObjectListType rol = aqResponse.getRegistryObjectList();

            List<JAXBElement<? extends IdentifiableType>> identifiableObjectList = rol.getIdentifiable();

            for (int i = 0; i < identifiableObjectList.size(); i++) {
                ExtrinsicObjectType eot = null;
                Object tempObj = identifiableObjectList.get(i).getValue();   //the getValue method will return the non-JAXBElement<? extends...> object

                if (tempObj instanceof ExtrinsicObjectType) {
                    eot = (ExtrinsicObjectType) tempObj;

                   if (eot != null) {
                        //get the externalIdentifiers so that we can get the docId  
                        List<ExternalIdentifierType> externalIdentifiers = eot.getExternalIdentifier();

                        //extract the docId
                        uniqueDocId = extractMetadataFromExternalIdentifiers(externalIdentifiers, "XDSDocumentEntry.uniqueId");
                    }
                }
            }
        } 
        
        return uniqueDocId;
    }
    
    private String getRepositoryUniqueId(AdhocQueryResponse aqResponse)  {
        String uniqueRepoId = null;
        
        if (aqResponse.getRegistryObjectList() != null) {
            RegistryObjectListType rol = aqResponse.getRegistryObjectList();

            List<JAXBElement<? extends IdentifiableType>> identifiableObjectList = rol.getIdentifiable();

            for (int i = 0; i < identifiableObjectList.size(); i++) {
                ExtrinsicObjectType eot = null;
                Object tempObj = identifiableObjectList.get(i).getValue();   //the getValue method will return the non-JAXBElement<? extends...> object

                if (tempObj instanceof ExtrinsicObjectType) {
                    eot = (ExtrinsicObjectType) tempObj;

                   if (eot != null) {
                       if (eot.getSlot() != null && eot.getSlot().size() > 0) {
                           List<SlotType1> documentSlots = eot.getSlot();

                           // extract repository unique id
                           uniqueRepoId = extractMetadataFromSlots(documentSlots, "repositoryUniqueId", 0);
                       }                	   
                    }
                }
            }
        } 
        
        return uniqueRepoId;
    }    
	
    // return a list of doc unique id separated by "~"
    private String extractMetadataFromExternalIdentifiers(
    		List<oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType> externalIdentifiers,
    		String metadataItemName) {
		String metadataItemValue = null;
		
		//loop through the externalIdentifiers looking for the for the desired name
		for (oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType
		        externalIdentifier : externalIdentifiers) {
		    String externalIdentifierName = externalIdentifier.getName().getLocalizedString().get(0).getValue();
		    if (metadataItemName.equalsIgnoreCase(externalIdentifierName)) {
		    	if(metadataItemValue == null) {
		    		metadataItemValue = externalIdentifier.getValue();
		    	} else {
		    		metadataItemValue = metadataItemValue + "~" + externalIdentifier.getValue(); 
		    	}
		        
		    }
		}
		
		return metadataItemValue;
    }    
	
    /**
     * Extracts the valueIndex value from an XDS request slot for a given metadata name.
     *
     * @param documentSlots A list of XDS metadata slots
     * @param slotName      The name of the slot containing the desired metadata item
     * @param valueIndex    For slot multivalued possibilities, the index value desired.
     *                      If the value is < 0 then all values in the value list are
     *                      returned in a '~' delimited list.
     * @return Returns the value of the first metadata value with the given metadata
     *         name. Null if not present.
     */
    @SuppressWarnings("unchecked")
    private String extractMetadataFromSlots(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1>
            documentSlots, String slotName, int valueIndex) {
        String slotValue = null;
        StringBuffer slotValues = null;
        boolean returnAllValues = false;
        if (valueIndex < 0) {
            returnAllValues = true;
            slotValues = new StringBuffer();
        }
        for (oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1 slot : documentSlots) {
            if (slotName.equals(slot.getName())) {
                if (returnAllValues) {
                    int listSize = slot.getValueList().getValue().size();
                    int counter = 0;
                    Iterator iter = slot.getValueList().getValue().iterator();
                    while (iter.hasNext()) {
                        String value = (String) iter.next();
                        slotValues.append(value);
                        counter++;
                        if (counter < listSize) {
                            slotValues.append("~");
                        }
                    }

                } else {
                    if (slot.getValueList() != null
                            && slot.getValueList().getValue() != null
                            && slot.getValueList().getValue().size() > 0) {
                        slotValue = slot.getValueList().getValue().get(valueIndex);
                    } else {
                        slotValue = "";
                    }
                }
                break; //found desired slot, have values, exit loop
            } //if (slotName.equals(slot.getName()))
        } //for (oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1 slot : documentSlots)
        if (returnAllValues) {
            slotValue = slotValues.toString();
        }

        return slotValue;
    }
    
	
    /* Auditing */
	
	protected String generateATNAMessage(String request, String patientId, String uniqueId, boolean outcome) throws JAXBException {
		AuditMessage res = new AuditMessage();
		
		EventIdentificationType eid = new EventIdentificationType();
		eid.setEventID( ATNAUtil.buildCodedValueType("DCM", "110112", "Query") );
		eid.setEventActionCode("E");
		eid.setEventDateTime( ATNAUtil.newXMLGregorianCalendar() );
		eid.getEventTypeCode().add( ATNAUtil.buildCodedValueType("IHE Transactions", "ITI-18", "Registry Stored Query") );
		eid.setEventOutcomeIndicator(outcome ? BigInteger.ONE : BigInteger.ZERO);
		res.setEventIdentification(eid);
		
		//TODO userId should be content of <wsa:ReplyTo/>
		res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant("userId", ATNAUtil.getProcessID(), true, ATNAUtil.getHostIP(), (short)2, "DCM", "110153", "Source"));
		//TODO reference the SHR from the configuration
		res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant("localhost", false, "localhost", (short)1, "DCM", "110152", "Destination"));
		
		res.getAuditSourceIdentification().add(ATNAUtil.buildAuditSource());
		
		res.getParticipantObjectIdentification().add(
			ATNAUtil.buildParticipantObjectIdentificationType(patientId +  "^^^&ECID&ISO", (short)1, (short)1, "RFC-3881", "2", "PatientNumber", null, null, null)
		);
		
		//TODO homeCommunityId: if known, then add it to participantObjectName and as an additional participantObjectDetail
		res.getParticipantObjectIdentification().add(
			ATNAUtil.buildParticipantObjectIdentificationType(
				uniqueId, (short)2, (short)24, "IHE Transactions", "ITI-18", "Registry Stored Query", request, "QueryEncoding", "UTF-8".getBytes()
			)
		);
		
		return ATNAUtil.marshall(res);
	}
	
    /* */
}
