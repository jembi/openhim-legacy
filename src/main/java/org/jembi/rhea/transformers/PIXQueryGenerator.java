/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.jembi.rhea.Constants;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.message.QBP_Q21;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;

public class PIXQueryGenerator  extends AbstractMessageTransformer {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssZ");
	
	private String _msh10;
	
	private String assigningAuthorityId = "";
	private String requestedAssigningAuthority = "";
	private String requestedAssigningAuthorityId = "";
	private String sendingApplication = "";
	private String sendingFacility = "";
	private String receivingApplication = "";
	private String receivingFacility = "";
	
	@SuppressWarnings("unchecked")
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		Map<String, String> idMap = (Map<String, String>) message.getPayload();
		
		String id = idMap.get("id");
		String idType = idMap.get("idType");
		
		String pix_query;
		try {
			pix_query = constructPIXQuery(id, idType, assigningAuthorityId, requestedAssigningAuthority, requestedAssigningAuthorityId);
		} catch (HL7Exception e) {
			throw new TransformerException(this, e);
		}
		
		// add request to session prop so that we can access it when
		// processing the response in PIXQueryResponseTransformer
		message.setProperty(Constants.PIX_REQUEST_PROPERTY, pix_query, PropertyScope.SESSION);
		message.setProperty(Constants.PIX_REQUEST_MSH10_PROPERTY, _msh10, PropertyScope.SESSION);
		
		return pix_query;
	}

	public String constructPIXQuery(String id, String assigningAuthority, String assigningAuthorityId, String requestedAssigningAuthority, String requestedAssigningAuthorityId) throws HL7Exception {
		
		QBP_Q21 qbp_q21 = new QBP_Q21();
		Terser t = new Terser(qbp_q21);
		
		MSH msh = (MSH) t.getSegment("MSH");
		t.set("MSH-1", "|");
		t.set("MSH-2", "^~\\&");
		t.set("MSH-3-1", sendingApplication);
		t.set("MSH-4-1", sendingFacility);
		t.set("MSH-5-1", receivingApplication);
		t.set("MSH-6-1", receivingFacility);
		msh.getDateTimeOfMessage().getTime().setValue(dateFormat.format(new Date()));
		t.set("MSH-9-1", "QBP");
		t.set("MSH-9-2", "Q23");
		t.set("MSH-9-3", "QBP_Q21");
		//MSH-10 message control id
		_msh10 = UUID.randomUUID().toString();
		t.set("MSH-10", _msh10);
		t.set("MSH-11-1", "P");
		t.set("MSH-12-1-1", "2.5");
		
		t.set("QPD-1-1", "IHE PIX Query");
		t.set("QPD-2", UUID.randomUUID().toString());
		t.set("QPD-3-1", id);
		t.set("QPD-3-4", assigningAuthority);
		t.set("QPD-3-4-2", assigningAuthorityId);
		t.set("QPD-3-4-3", "ISO");
		t.set("QPD-3-5", "PI");
		
		if (requestedAssigningAuthority!=null && !requestedAssigningAuthority.isEmpty()) {
			t.set("QPD-4-4", requestedAssigningAuthority);
			t.set("QPD-4-4-2", requestedAssigningAuthorityId);
			t.set("QPD-4-4-3", "ISO");
			t.set("QPD-4-5", "PI");
		}
		
		t.set("RCP-1", "I");
		
		Parser p = new GenericParser();
		return p.encode(qbp_q21);
	}

	public String getAssigningAuthorityId() {
		return assigningAuthorityId;
	}

	public void setAssigningAuthorityId(String assigningAuthorityId) {
		this.assigningAuthorityId = assigningAuthorityId;
	}

	public String getRequestedAssigningAuthority() {
		return requestedAssigningAuthority;
	}

	public void setRequestedAssigningAuthority(String requestedAssigningAuthority) {
		this.requestedAssigningAuthority = requestedAssigningAuthority;
	}

	public String getRequestedAssigningAuthorityId() {
		return requestedAssigningAuthorityId;
	}

	public void setRequestedAssigningAuthorityId(
			String requestedAssigningAuthorityId) {
		this.requestedAssigningAuthorityId = requestedAssigningAuthorityId;
	}

	public String getSendingApplication() {
		return sendingApplication;
	}

	public void setSendingApplication(String sendingApplication) {
		this.sendingApplication = sendingApplication;
	}

	public String getSendingFacility() {
		return sendingFacility;
	}

	public void setSendingFacility(String sendingFacility) {
		this.sendingFacility = sendingFacility;
	}

	public String getReceivingApplication() {
		return receivingApplication;
	}

	public void setReceivingApplication(String receivingApplication) {
		this.receivingApplication = receivingApplication;
	}

	public String getReceivingFacility() {
		return receivingFacility;
	}

	public void setReceivingFacility(String receivingFacility) {
		this.receivingFacility = receivingFacility;
	}
}
