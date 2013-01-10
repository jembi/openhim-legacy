/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.xml.bind.JAXBException;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.ResponseOptionType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;

import org.jembi.rhea.Constants;
import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.Util;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;

import ca.marc.ihe.xds.XDSUtil;

/**
 * XDS ITI-18 Registry Stored Query
 */
public class XDSRegistryStoredQuery extends AbstractMessageTransformer {
	
	private String requestedAssigningAuthorityId = "";

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
        String storedQueryId = (String.format("urn:uuid:%s", "14d4debf-8f97-4251-9a74-a90016b0af0d"));
        adhocQuery.setId(storedQueryId);
        
        // Slots, first setup slot for patient ID
        String srcPatientId = String.format("'%s^^^&%s&ISO'", id, requestedAssigningAuthorityId);
        adhocQuery.getSlot().add(XDSUtil.createQuerySlot("$XDSDocumentEntryPatientId", srcPatientId));
        
        // Setup status slot 
        adhocQuery.getSlot().add(XDSUtil.createQuerySlot("$XDSDocumentEntryStatus", "('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')"));
        
        // From time
        SimpleDateFormat hl7DateFormat = new SimpleDateFormat("yyyyMMddHHmm"); 
        if(startDate != null)
        	adhocQuery.getSlot().add(XDSUtil.createQuerySlot("$XDSDocumentEntryCreationTimeFrom", hl7DateFormat.format(startDate)));
        if(endDate != null)
        	adhocQuery.getSlot().add(XDSUtil.createQuerySlot("$XDSDocumentEntryCreationTimeTo", hl7DateFormat.format(endDate)));
        
        // Append the ad-hoc query to the request 
        request.setAdhocQuery(adhocQuery);
		
        try {
			// add request to session prop so that we can access it when processing the response
			message.setProperty(Constants.XDS_ITI_18_PROPERTY, Util.marshallJAXBObject("oasis.names.tc.ebxml_regrep.xsd.query._3", request, false), PropertyScope.SESSION);
			message.setProperty(Constants.XDS_ITI_18_UNIQUEID_PROPERTY, storedQueryId, PropertyScope.SESSION);
			message.setProperty(Constants.XDS_ITI_18_PATIENTID_PROPERTY, id, PropertyScope.SESSION);
		} catch (JAXBException ex) {
			throw new TransformerException(this, ex);
		}
		
		return request;
	}

	public String getRequestedAssigningAuthorityId() {
		return requestedAssigningAuthorityId;
	}

	public void setRequestedAssigningAuthorityId(
			String requestedAssigningAuthorityId) {
		this.requestedAssigningAuthorityId = requestedAssigningAuthorityId;
	}
}