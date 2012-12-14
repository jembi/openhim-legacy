/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import javax.xml.bind.JAXBException;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;

import org.jembi.ihe.xds.XDSAffinityDomain;
import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.Util;
import org.jembi.rhea.transformers.XDSRepositoryProvideAndRegisterDocument.EncounterInfo;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import ca.uhn.hl7v2.HL7Exception;

/**
 * XDS ITI-43 Retrieve Document Set
 */
public class XDSRepositoryRetrieveDocumentSet extends
		AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {		
		// extract patient id, repository id and a list of document unique id's from the request in the following format, e.g. 
		// ws/rest/v1/retrieve_document_set/?patient_id=GHHS-2552234100&document_unique_id=111111111~222222222&repository_unique_id=1
		RestfulHttpRequest restRequest = (RestfulHttpRequest) message.getPayload();
		String patientId = restRequest.getRequestParams().get("patient_id");
		String idStr = restRequest.getRequestParams().get("document_unique_id");
		String[] identifers = idStr.split("~");
				
		// construct RetrieveDocumentSetRequestType
		RetrieveDocumentSetRequestType rdRequest = new RetrieveDocumentSetRequestType();
		String homeCommunityId = XDSAffinityDomain.IHE_CONNECTATHON_NA2013_RHEAHIE.getHomeCommunityId();
		String repositoryUniqueId = restRequest.getRequestParams().get("repository_unique_id");  //??XDSAffinityDomain.IHE_CONNECTATHON_NA2013_RHEAHIE.getRepositoryUniqueId();??
		
		// add unique document id list to document request
		for(String docUniqueId : identifers) {			
			rdRequest.getDocumentRequest().add(createDocumentRequest(docUniqueId, homeCommunityId, repositoryUniqueId));
		}
		
		// add request to session prop so that we can access it when processing the response
		message.setSessionProperty("XDS-ITI-43", rdRequest);
		message.setSessionProperty("XDS-ITI-43_uniqueId", idStr);
		message.setSessionProperty("XDS-ITI-43_patientId", patientId);
		
		return rdRequest;
	}
	
   private DocumentRequest createDocumentRequest(String docUniqueId, String homeCommunityId, String repositoryUniqueId) {
       DocumentRequest dr = new DocumentRequest();
       dr.setHomeCommunityId(getUrnOidFormat(homeCommunityId));
       dr.setDocumentUniqueId(docUniqueId);
       dr.setRepositoryUniqueId(repositoryUniqueId);

       return dr;
   }	
   
   private String getUrnOidFormat(final String oid) {
	   if(oid == null) return "";
	   else if(oid.startsWith("urn:oid:")) return oid;
	   else return "urn:oid:" + oid;
   }   
}
