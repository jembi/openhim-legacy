/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

/**
 * Handle XDS ITI-43 Retrieve Document Set response
 */
import ihe.iti.atna.AuditMessage;
import ihe.iti.atna.EventIdentificationType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jembi.ihe.atna.ATNAUtil;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.client.MuleClient;
import org.mule.transformer.AbstractMessageTransformer;

public class XDSRepositoryRetrieveDocumentSetResponse extends
		AbstractMessageTransformer {
	
	private Log log = LogFactory.getLog(this.getClass());

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		try {
			// process response					
			if (message.getPayload()==null) {
				log.error("Null response received from XDS repository");
				return null;
			} else if (message.getPayload() instanceof RetrieveDocumentSetResponseType) {
				RetrieveDocumentSetResponseType response = (RetrieveDocumentSetResponseType) message.getPayload();
				return Collections.singleton(processResponse(response));
			} else if (message.getPayload() instanceof ArrayList &&
					((List)message.getPayload()).size()>0 &&
					((List)message.getPayload()).get(0) instanceof RetrieveDocumentSetResponseType) {
				List<RetrieveDocumentSetResponseType> responses = (List<RetrieveDocumentSetResponseType>)message.getPayload();
				List<String> res = new ArrayList<String>(responses.size());
				for (RetrieveDocumentSetResponseType response : responses)
					res.add(processResponse(response));
				return res;
			} else {
				log.error("Unknown response type received from XDS repository: " + message.getPayload().getClass());
				return null;
			}
			
		} catch (JAXBException e) {
			throw new TransformerException(this, e);
		} catch (MuleException e) {
			throw new TransformerException(this, e);
		}
	}
	
	private String processResponse(RetrieveDocumentSetResponseType response) throws JAXBException, MuleException {
		boolean outcome = false;
		String repositoryUniqueId = null;

	    // get a list of doc unique id separated by ":"
		String document = getDocument(response);
	
		//generate audit message
		//String request = (String)message.getSessionProperty("XDS-ITI-43");
		//String uniqueId = (String)message.getSessionProperty("XDS-ITI-43_uniqueId");
		//String patientId = (String)message.getSessionProperty("XDS-ITI-43_patientId");
		String request = null, uniqueId = null, patientId = null;
		
		String at = generateATNAMessage(request, patientId, uniqueId, repositoryUniqueId, outcome); //??shall we log the response as well as the request??
		if(muleContext != null) {
			MuleClient client = new MuleClient(muleContext);
			at = ATNAUtil.build_TCP_Msg_header() + at;
			client.dispatch("vm://atna_auditing", at.length() + " " + at, null);
		}
		// return the content of the document
		
		return document;
	}

    private String getDocument(RetrieveDocumentSetResponseType drResponse) throws TransformerException {

        RegistryResponseType rrt = drResponse.getRegistryResponse();

        if (rrt!= null && rrt.getRegistryErrorList() != null) {
           RegistryErrorList rel = rrt.getRegistryErrorList();

           if (rel != null &&
                   rel.getRegistryError() != null &&
                   rel.getRegistryError().size() > 0 &&
                   rel.getRegistryError().get(0) != null) {
        	   throw new TransformerException(this, new Exception("TotalErrors: " + rel.getRegistryError().size() + "FirstError: " + rel.getRegistryError().get(0).getValue()));
           }
       }

       String status = (rrt==null? "" : rrt.getStatus());   // ??Shall we log this and other information(e.g. totalResultCnt, documentLength, mimeType, etc) anywhere??
       int totalResultCnt = 0;     
       String document = null;

       List<DocumentResponse> drList =  drResponse.getDocumentResponse();  // <ns2:DocumentResponse>

       if (drList != null && drList.size() > 0 && drList.get(0) != null) {
    	   totalResultCnt = drList.size();
           for (DocumentResponse dr : drList) {       // may want to loop thru the results at some point, but for now......
                String home = dr.getHomeCommunityId();               //  <ns2:HomeCommunityId>urn:oid:1.3.6.1.4.1.12009.6.1</ns2:HomeCommunityId>
                String reposUniqueId = dr.getRepositoryUniqueId();   //  <ns2:RepositoryUniqueId>1</ns2:RepositoryUniqueId>
                String docUniqueId = dr.getDocumentUniqueId();       //  <ns2:DocumentUniqueId>1.123401.11111</ns2:DocumentUniqueId>
                String mimeType = dr.getMimeType();                  //  <ns2:mimeType>text/xml</ns2:mimeType>
                if(dr.getDocument()!=null) {
                    document = new String(dr.getDocument());       //  <ns2:Document>VEVTVCBET0NVTUVOVCBDT05URU5U</ns2:Document>
                    int documentLength = dr.getDocument().length;
                } else {
                	throw new TransformerException(this, new Exception("dr.getDocument() returns null!"));
                }
           }
        }

        return document;
    }
    
    /* Auditing */
	
	protected String generateATNAMessage(String request, String patientId, String documentUniqueId, String repositoryUniqueId, boolean outcome)
			throws JAXBException {
		AuditMessage res = new AuditMessage();
		
		EventIdentificationType eid = new EventIdentificationType();
		eid.setEventID( ATNAUtil.buildCodedValueType("DCM", "110107", "Import") );
		eid.setEventActionCode("C");
		eid.setEventDateTime( ATNAUtil.newXMLGregorianCalendar() );
		eid.getEventTypeCode().add( ATNAUtil.buildCodedValueType("IHE Transactions", "ITI-43", "Retrieve Document Set") );
		eid.setEventOutcomeIndicator(outcome ? BigInteger.ONE : BigInteger.ZERO);
		res.setEventIdentification(eid);
		
		//TODO reference the SHR from the configuration
		res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant("localhost", false, "localhost", (short)1, "DCM", "110153", "Source"));
		//TODO userId should be content of <wsa:ReplyTo/>
		res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant("userId", ATNAUtil.getProcessID(), true, ATNAUtil.getHostIP(), (short)2, "DCM", "110152", "Destination"));
		
		res.getAuditSourceIdentification().add(ATNAUtil.buildAuditSource());
		
		res.getParticipantObjectIdentification().add(
			ATNAUtil.buildParticipantObjectIdentificationType(patientId +  "^^^&ECID&ISO", (short)1, (short)1, "RFC-3881", "2", "PatientNumber", null, null, null)
		);
		
		//TODO homeCommunityId: if known, then add it as an additional participantObjectDetail
		res.getParticipantObjectIdentification().add(
			ATNAUtil.buildParticipantObjectIdentificationType(
				documentUniqueId, (short)2, (short)3, "RFC-3881", "9", "Report Number", request, "Repository Unique Id", (repositoryUniqueId==null? null : repositoryUniqueId.getBytes())
			)
		);
		
		return ATNAUtil.marshall(res);
	}
	
    /* */
}
