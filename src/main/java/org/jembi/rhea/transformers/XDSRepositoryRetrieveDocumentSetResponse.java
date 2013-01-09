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
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.JAXBException;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jembi.ihe.atna.ATNAUtil;
import org.jembi.ihe.atna.ATNAUtil.ParticipantObjectDetail;
import org.jembi.rhea.Constants;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.module.client.MuleClient;
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
	//not thread safe...
	private String _homeCommunityId;
	private String _reposUniqueId;
	private String _docUniqueId;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		// process response					
		if (message.getPayload()==null) {
			
			log.error("Null response received from XDS repository");
			return null;
			
		} else if (message.getPayload() instanceof RetrieveDocumentSetResponseType) {
			
			RetrieveDocumentSetResponseType response = (RetrieveDocumentSetResponseType) message.getPayload();
			return Collections.singleton(processResponse(message, response));
			
		} else if (message.getPayload() instanceof List &&
				((List)message.getPayload()).size()>0) {
			
			if (!(((List)message.getPayload()).get(0) instanceof RetrieveDocumentSetResponseType)) {
				log.error("Unknown response type received from XDS repository in list: " + ((List)message.getPayload()).get(0).getClass());
				return null;
			}
			if (((List)message.getPayload()).isEmpty()) {
				log.info("No documents for patient in repository");
				return null;
			}
			
			List<RetrieveDocumentSetResponseType> responses = (List<RetrieveDocumentSetResponseType>)message.getPayload();
			List<String> res = new ArrayList<String>(responses.size());
			for (RetrieveDocumentSetResponseType response : responses) {
				String doc = processResponse(message, response);
				res.add(doc);
				log.info("Received document:\n" + doc);
			}
			return res;
			
		} else {
			
			log.error("Unknown response type received from XDS repository: " + message.getPayload().getClass());
			return null;
			
		}
	}
	
	@SuppressWarnings("unchecked")
	private String processResponse(MuleMessage message, RetrieveDocumentSetResponseType response) throws TransformerException {
		boolean outcome = false;
		String document = null;

		try {
		    // get a list of doc unique id separated by ":"
			document = getDocument(response);
			outcome = true;
			
		} finally {
			try {
				//generate audit message
				String request = ((Map<String, String>)message.getProperty(Constants.XDS_ITI_43, PropertyScope.SESSION)).get(_docUniqueId);
				String patientId = (String)message.getProperty(Constants.XDS_ITI_18_PATIENTID_PROPERTY, PropertyScope.SESSION);
				ATNAUtil.dispatchAuditMessage(muleContext, generateATNAMessage(request, patientId, outcome));
				log.info("Dispatched ATNA message");
			} catch (Exception e) {
				//If the auditing breaks, it shouldn't break the flow, so catch and log
				log.error("Failed to dispatch ATNA message", e);
			}
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
                _homeCommunityId = dr.getHomeCommunityId();               //  <ns2:HomeCommunityId>urn:oid:1.3.6.1.4.1.12009.6.1</ns2:HomeCommunityId>
                _reposUniqueId = dr.getRepositoryUniqueId();   //  <ns2:RepositoryUniqueId>1</ns2:RepositoryUniqueId>
                _docUniqueId = dr.getDocumentUniqueId();       //  <ns2:DocumentUniqueId>1.123401.11111</ns2:DocumentUniqueId>
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
	
	protected String generateATNAMessage(String request, String patientId, boolean outcome)
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
		
		res.getAuditSourceIdentification().add(ATNAUtil.buildAuditSource());
		
		res.getParticipantObjectIdentification().add(
			ATNAUtil.buildParticipantObjectIdentificationType(String.format("%s^^^&%s&ISO", patientId, requestedAssigningAuthority), (short)1, (short)1, "RFC-3881", "2", "PatientNumber", null)
		);
		
		List<ParticipantObjectDetail> pod = new ArrayList<ParticipantObjectDetail>();
		if (_reposUniqueId!=null) pod.add(new ParticipantObjectDetail("Repository Unique Id", _reposUniqueId.getBytes()));
		if (_homeCommunityId!=null) pod.add(new ParticipantObjectDetail("ihe:homeCommunityID", _homeCommunityId.getBytes()));
		
		res.getParticipantObjectIdentification().add(
			ATNAUtil.buildParticipantObjectIdentificationType(
				_docUniqueId, (short)2, (short)3, "RFC-3881", "9", "Report Number", request, pod
			)
		);
		
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
}
