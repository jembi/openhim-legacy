/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import ihe.iti.atna.AuditMessage;
import ihe.iti.atna.EventIdentificationType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jembi.ihe.atna.ATNAUtil;
import org.jembi.ihe.atna.ATNAUtil.ParticipantObjectDetail;
import org.jembi.rhea.Constants;
import org.jembi.rhea.xds.DocumentMetaData;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;

/**
 * Handle XDS ITI-18 Registry Stored Query response
 */
public class XDSRegistryStoredQueryResponse extends AbstractMessageTransformer {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private String xdsRegistryHost = "";
	private String xdsRegistryPath = "";
	private String xdsRegistryPort = "";
	private String xdsRegistrySecurePort = "";
	private String iheSecure = "";
	private String requestedAssigningAuthority = "";
	private String homeCommunityId;
	
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		boolean outcome = false;
		Map<String, List<DocumentMetaData>> repoDocumentsMap = null;
			
		try {
			AdhocQueryResponse response = null;
			if (message.getPayload() instanceof AdhocQueryResponse) {
				response = (AdhocQueryResponse) message.getPayload();
			} else {
				log.error(String.format("Unknown response type received (%s)", message.getPayload().getClass()));
				return null;
			}
			
			// get a map of repository id's pointing to a list of document id's for that repository
			repoDocumentsMap = getRepositoryDocuments(response);
			
		} finally {
			try {
				//generate audit message
				String request = (String)message.getProperty(Constants.XDS_ITI_18_PROPERTY, PropertyScope.SESSION);
				String uniqueId = (String)message.getProperty(Constants.XDS_ITI_18_UNIQUEID_PROPERTY, PropertyScope.SESSION);
				String patientId = (String)message.getProperty(Constants.XDS_ITI_18_PATIENTID_PROPERTY, PropertyScope.SESSION);
				ATNAUtil.dispatchAuditMessage(muleContext, generateATNAMessage(request, patientId, uniqueId, outcome));
				log.info("Dispatched ATNA message");
			} catch (Exception e) {
				//If the auditing breaks, it shouldn't break the flow, so catch and log
				log.error("Failed to dispatch ATNA message", e);
			}
		}
		
		return repoDocumentsMap;
	}

	private Map<String, List<DocumentMetaData>> getRepositoryDocuments(
			AdhocQueryResponse aqResponse) {
		Map<String, List<DocumentMetaData>> repoDocumentsMap = new HashMap<String, List<DocumentMetaData>>();

		if (aqResponse.getRegistryObjectList() != null) {
			RegistryObjectListType rol = aqResponse.getRegistryObjectList();

			List<JAXBElement<? extends IdentifiableType>> identifiableObjectList = rol
					.getIdentifiable();

			for (int i = 0; i < identifiableObjectList.size(); i++) {
				ExtrinsicObjectType eot = null;
				// the getValue method will return the non-JAXBElement<? extends...> object
				Object tempObj = identifiableObjectList.get(i).getValue();

				if (tempObj instanceof ExtrinsicObjectType) {
					eot = (ExtrinsicObjectType) tempObj;

					if (eot != null) {
						String uniqueRepoId = null;
						if (eot.getSlot() != null && eot.getSlot().size() > 0) {
							List<SlotType1> documentSlots = eot.getSlot();

							// extract repository unique id
							uniqueRepoId = extractMetadataFromSlots(documentSlots, "repositoryUniqueId", 0);
						}
						// get the externalIdentifiers so that we can get the
						// docId
						List<ExternalIdentifierType> externalIdentifiers = eot.getExternalIdentifier();

						// extract the docId
						String uniqueDocId = extractMetadataFromExternalIdentifiers(externalIdentifiers, "XDSDocumentEntry.uniqueId");
						
						// store uniqueRepoId and docId
						List<DocumentMetaData> docList = repoDocumentsMap.get(uniqueRepoId);
						if (docList == null) {
							docList = new ArrayList<DocumentMetaData>();
							repoDocumentsMap.put(uniqueRepoId, docList);
						}
						
						// get homeCommunityId
						homeCommunityId = eot.getHome();
						
						docList.add(new DocumentMetaData(uniqueDocId, homeCommunityId));
					}
				}
			}
		}

		return repoDocumentsMap;
	}  
	
    // return id from an external identifier
    private String extractMetadataFromExternalIdentifiers(
    		List<oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType> externalIdentifiers,
    		String metadataItemName) {
		String metadataItemValue = null;
		
		//loop through the externalIdentifiers looking for the for the desired name
		for (oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType
		        externalIdentifier : externalIdentifiers) {
		    String externalIdentifierName = externalIdentifier.getName().getLocalizedString().get(0).getValue();
		    if (metadataItemName.equalsIgnoreCase(externalIdentifierName)) {
		    	return externalIdentifier.getValue();
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
                    Iterator<String> iter = slot.getValueList().getValue().iterator();
                    while (iter.hasNext()) {
                        String value = iter.next();
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
		
		res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant(ATNAUtil.WSA_REPLYTO_ANON, ATNAUtil.getProcessID(), true, ATNAUtil.getHostIP(), (short)2, "DCM", "110153", "Source"));
		res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant(buildRegistryPath(), xdsRegistryHost, false, xdsRegistryHost, (short)1, "DCM", "110152", "Destination"));
		
		res.getAuditSourceIdentification().add(ATNAUtil.buildAuditSource("openhie-registry"));
		
		res.getParticipantObjectIdentification().add(
			ATNAUtil.buildParticipantObjectIdentificationType(String.format("%s^^^&%s&ISO", patientId, requestedAssigningAuthority), (short)1, (short)1, "RFC-3881", "2", "PatientNumber", null)
		);
		
		List<ParticipantObjectDetail> pod = new ArrayList<ParticipantObjectDetail>();
		pod.add(new ParticipantObjectDetail("QueryEncoding", "UTF-8".getBytes()));
		if (homeCommunityId!=null) pod.add(new ParticipantObjectDetail("urn:ihe:iti:xca:2010:homeCommunityId", homeCommunityId.getBytes()));
		
		res.getParticipantObjectIdentification().add(
			ATNAUtil.buildParticipantObjectIdentificationType(
				uniqueId, (short)2, (short)24, "IHE Transactions", "ITI-18", "Registry Stored Query", request, pod
			)
		);
		
		return ATNAUtil.marshallATNAObject(res);
	}
	
    /* */

	public String getXdsRegistryHost() {
		return xdsRegistryHost;
	}

	public void setXdsRegistryHost(String xdsRegistryHost) {
		this.xdsRegistryHost = xdsRegistryHost;
	}

	public String getRequestedAssigningAuthority() {
		return requestedAssigningAuthority;
	}

	public void setRequestedAssigningAuthority(String requestedAssigningAuthority) {
		this.requestedAssigningAuthority = requestedAssigningAuthority;
	}

	public String getXdsRegistryPath() {
		return xdsRegistryPath;
	}

	public void setXdsRegistryPath(String xdsRegistryPath) {
		this.xdsRegistryPath = xdsRegistryPath;
	}

	public String getXdsRegistryPort() {
		return xdsRegistryPort;
	}

	public void setXdsRegistryPort(String xdsRegistryPort) {
		this.xdsRegistryPort = xdsRegistryPort;
	}

	public String getXdsRegistrySecurePort() {
		return xdsRegistrySecurePort;
	}

	public void setXdsRegistrySecurePort(String xdsRegistrySecurePort) {
		this.xdsRegistrySecurePort = xdsRegistrySecurePort;
	}

	public String getIheSecure() {
		return iheSecure;
	}

	public void setIheSecure(String iheSecure) {
		this.iheSecure = iheSecure;
	}
	
	private String buildRegistryPath() {
		return String.format("%s:%s/%s", xdsRegistryHost, ((iheSecure.equalsIgnoreCase("true")) ? xdsRegistrySecurePort : xdsRegistryPort), xdsRegistryPath);
	}
}
