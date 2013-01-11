/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jembi.rhea.xds.DocumentMetaData;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

/**
 * XDS ITI-43 Retrieve Document Set
 */
public class XDSRepositoryRetrieveDocumentSet extends
		AbstractMessageTransformer {

	@SuppressWarnings("unchecked")
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {		
		// extract patient id, repository id and a list of document unique id's from the payload
		Map<String, List<DocumentMetaData>> repoDocumentsMap = (Map<String, List<DocumentMetaData>>) message.getPayload();
		List<RetrieveDocumentSetRequestType> retrieveDocumentMessages = new ArrayList<RetrieveDocumentSetRequestType>();
		
		Set<String> keySet = repoDocumentsMap.keySet();
		for (String key : keySet) {
			List<DocumentMetaData> docList = repoDocumentsMap.get(key);
			
			// construct RetrieveDocumentSetRequestType
			RetrieveDocumentSetRequestType rdRequest = new RetrieveDocumentSetRequestType();
			
			String repositoryUniqueId = key;
			
			// add unique document id list to document request
			for(DocumentMetaData documentMetaData : docList) {			
				DocumentRequest dr = createDocumentRequest(documentMetaData.getDocumentUniqueId(), documentMetaData.getHomeCommunityId(), repositoryUniqueId);
				rdRequest.getDocumentRequest().add(dr);
			}
			
			retrieveDocumentMessages.add(rdRequest);
		}
				
		return retrieveDocumentMessages;
	}
	
   private DocumentRequest createDocumentRequest(String docUniqueId, String homeCommunityId, String repositoryUniqueId) {
       DocumentRequest dr = new DocumentRequest();
       dr.setHomeCommunityId(getUrnOidFormat(homeCommunityId));
       dr.setDocumentUniqueId(docUniqueId);
       dr.setRepositoryUniqueId(repositoryUniqueId);
       //dr.setRepositoryUniqueId("1.3.6.1.4.1.21367.2011.2.3.7");

       return dr;
   }	
   
   private String getUrnOidFormat(final String oid) {
	   if(oid == null) return "";
	   else if(oid.startsWith("urn:oid:")) return oid;
	   else return "urn:oid:" + oid;
   }   
}
