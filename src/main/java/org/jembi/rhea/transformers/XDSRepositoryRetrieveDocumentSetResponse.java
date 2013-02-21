/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

/**
 * Handle XDS ITI-43 Retrieve Document Set response
 */
import ihe.iti.atna.AuditMessage;
import ihe.iti.atna.EventIdentificationType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jembi.ihe.atna.ATNAUtil;
import org.jembi.ihe.atna.ATNAUtil.ParticipantObjectDetail;
import org.jembi.rhea.Constants;
import org.jembi.rhea.Util;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;

public class XDSRepositoryRetrieveDocumentSetResponse extends
		AbstractMessageTransformer {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private String xdsRepositoryHost = "";
	private String xdsRepositoryPath = "";
	private String xdsRepositoryPort = "";
	private String xdsRepositorySecurePort = "";
	private String iheSecure = "";
	private String requestedAssigningAuthority = "";

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		// process response					
		if (message.getPayload()==null) {
			
			log.error("Null response received from XDS repository");
			return null;
			
		} else if (message.getPayload() instanceof RetrieveDocumentSetResponseType) {
			
			String request = null;
			try {
				RetrieveDocumentSetRequestType originalRequest = (RetrieveDocumentSetRequestType)message.getProperty(Constants.XDS_ITI_43, PropertyScope.SESSION);
				request = Util.marshallJAXBObject("ihe.iti.xds_b._2007", originalRequest, false);
			} catch (JAXBException e) {
				log.error("Error marshalling request ", e);
			}
			String patientId = (String)message.getProperty(Constants.XDS_ITI_18_PATIENTID_PROPERTY, PropertyScope.SESSION);
			List<String> doc = processResponse((RetrieveDocumentSetResponseType)message.getPayload(), patientId, request);
			return doc;
			
		} else {
			
			log.error("Unknown response type received from XDS repository: " + message.getPayload().getClass());
			return null;
			
		}
	}
	
	private List<String> processResponse(RetrieveDocumentSetResponseType response, String patientId, String originalRequest) throws TransformerException {
		boolean outcome = false;
		List<String> documents = null;
		List<DocumentInfo> documentsInfo = new LinkedList<DocumentInfo>();

		try {
		    // get a list of doc unique id separated by ":"
			documents = getDocuments(response, documentsInfo);
			outcome = true;
			
		} finally {
			try {
				//generate audit message
				ATNAUtil.dispatchAuditMessage(muleContext, generateATNAMessage(originalRequest, patientId, outcome, documentsInfo));
				log.info("Dispatched ATNA message");
			} catch (Exception e) {
				//If the auditing breaks, it shouldn't break the flow, so catch and log
				log.error("Failed to dispatch ATNA message", e);
			}
		}
		
		// return the content of the document
		return documents;
	}

    private List<String> getDocuments(RetrieveDocumentSetResponseType drResponse, List<DocumentInfo> documentsInfo) throws TransformerException {

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

       List<DocumentResponse> drList =  drResponse.getDocumentResponse();  // <ns2:DocumentResponse>
    	List<String> res = new ArrayList<String>( (drList!=null ? drList.size() : 0) );

       if (drList != null && drList.size() > 0 && drList.get(0) != null) {
    	   totalResultCnt = drList.size();
           for (DocumentResponse dr : drList) {       // may want to loop thru the results at some point, but for now......
                String homeCommunityId = dr.getHomeCommunityId();               //  <ns2:HomeCommunityId>urn:oid:1.3.6.1.4.1.12009.6.1</ns2:HomeCommunityId>
                String reposUniqueId = dr.getRepositoryUniqueId();   //  <ns2:RepositoryUniqueId>1</ns2:RepositoryUniqueId>
                String docUniqueId = dr.getDocumentUniqueId();       //  <ns2:DocumentUniqueId>1.123401.11111</ns2:DocumentUniqueId>
                documentsInfo.add(new DocumentInfo(homeCommunityId, reposUniqueId, docUniqueId));
                String mimeType = dr.getMimeType();                  //  <ns2:mimeType>text/xml</ns2:mimeType>
                if(dr.getDocument()!=null) {
                    String document = new String(dr.getDocument());       //  <ns2:Document>VEVTVCBET0NVTUVOVCBDT05URU5U</ns2:Document>
                    int documentLength = dr.getDocument().length;
                    res.add(document);
                    log.info("Received document: " + docUniqueId);
                } else {
                	throw new TransformerException(this, new Exception("dr.getDocument() returns null!"));
                }
           }
        }

        return res;
    }
    
    /* Auditing */
	
	protected String generateATNAMessage(String request, String patientId, boolean outcome, List<DocumentInfo> documentsInfo)
			throws JAXBException {
		AuditMessage res = new AuditMessage();
		
		EventIdentificationType eid = new EventIdentificationType();
		eid.setEventID( ATNAUtil.buildCodedValueType("DCM", "110107", "Import") );
		eid.setEventActionCode("C");
		eid.setEventDateTime( ATNAUtil.newXMLGregorianCalendar() );
		eid.getEventTypeCode().add( ATNAUtil.buildCodedValueType("IHE Transactions", "ITI-43", "Retrieve Document Set") );
		eid.setEventOutcomeIndicator(outcome ? BigInteger.ZERO : new BigInteger("4"));
		res.setEventIdentification(eid);
		
		res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant(buildRepositoryPath(), xdsRepositoryHost, false, xdsRepositoryHost, (short)1, "DCM", "110153", "Source"));
		res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant(ATNAUtil.WSA_REPLYTO_ANON, ATNAUtil.getProcessID(), true, ATNAUtil.getHostIP(), (short)2, "DCM", "110152", "Destination"));
		
		res.getAuditSourceIdentification().add(ATNAUtil.buildAuditSource("openhie-repository"));
		
		res.getParticipantObjectIdentification().add(
			ATNAUtil.buildParticipantObjectIdentificationType(String.format("%s^^^&%s&ISO", patientId, requestedAssigningAuthority), (short)1, (short)1, "RFC-3881", "2", "PatientNumber", null)
		);
		
		for (DocumentInfo docInfo : documentsInfo) {
			List<ParticipantObjectDetail> pod = new ArrayList<ParticipantObjectDetail>();
			if (docInfo._reposUniqueId!=null) pod.add(new ParticipantObjectDetail("Repository Unique Id", docInfo._reposUniqueId.getBytes()));
			if (docInfo._homeCommunityId!=null) pod.add(new ParticipantObjectDetail("ihe:homeCommunityID", docInfo._homeCommunityId.getBytes()));
			
			res.getParticipantObjectIdentification().add(
				ATNAUtil.buildParticipantObjectIdentificationType(
					docInfo._docUniqueId, (short)2, (short)3, "RFC-3881", "9", "Report Number", request, pod
				)
			);
		}
		
		return ATNAUtil.marshallATNAObject(res);
	}
	
    /* */

	public String getXdsRepositoryHost() {
		return xdsRepositoryHost;
	}

	public void setXdsRepositoryHost(String xdsRepositoryHost) {
		this.xdsRepositoryHost = xdsRepositoryHost;
	}

	public String getRequestedAssigningAuthority() {
		return requestedAssigningAuthority;
	}

	public void setRequestedAssigningAuthority(String requestedAssigningAuthority) {
		this.requestedAssigningAuthority = requestedAssigningAuthority;
	}

	public String getXdsRepositoryPath() {
		return xdsRepositoryPath;
	}

	public void setXdsRepositoryPath(String xdsRepositoryPath) {
		this.xdsRepositoryPath = xdsRepositoryPath;
	}

	public String getXdsRepositoryPort() {
		return xdsRepositoryPort;
	}

	public void setXdsRepositoryPort(String xdsRepositoryPort) {
		this.xdsRepositoryPort = xdsRepositoryPort;
	}

	public String getXdsRepositorySecurePort() {
		return xdsRepositorySecurePort;
	}

	public void setXdsRepositorySecurePort(String xdsRepositorySecurePort) {
		this.xdsRepositorySecurePort = xdsRepositorySecurePort;
	}

	public String getIheSecure() {
		return iheSecure;
	}

	public void setIheSecure(String iheSecure) {
		this.iheSecure = iheSecure;
	}
	
	private String buildRepositoryPath() {
		return String.format("%s:%s/%s", xdsRepositoryHost, ((iheSecure.equalsIgnoreCase("true")) ? xdsRepositorySecurePort : xdsRepositoryPort), xdsRepositoryPath);
	}
	
	
	private static class DocumentInfo {
		String _homeCommunityId;
		String _reposUniqueId;
		String _docUniqueId;
		
		public DocumentInfo(String _homeCommunityId, String _reposUniqueId, String _docUniqueId) {
			this._homeCommunityId = _homeCommunityId;
			this._reposUniqueId = _reposUniqueId;
			this._docUniqueId = _docUniqueId;
		}
	}
}
