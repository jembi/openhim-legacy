/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.message.QBP_Q21;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;

public class PIXQueryGenerator  extends AbstractMessageTransformer {

	private String _msh10;
	
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		Map<String, String> idMap = (Map<String, String>) message.getPayload();
		
		String id = idMap.get("id");
		String idType = idMap.get("idType");
		
		String pix_query;
		try {
			//TODO externalise these strings
			pix_query = constructPIXQuery(id, idType, null, "MOH_CAAT_MARC_HI", "1.3.6.1.4.1.33349.3.1.2.1.0.1");
			//NIST test 1
			//pix_query = constructPIXQuery("PIXL1", "NIST2010", "2.16.840.1.113883.3.72.5.9.1", "NIST2010-3", "2.16.840.1.113883.3.72.5.9.3");
			//NIST test 2
			//pix_query = constructPIXQuery("PIXL1", "NIST2010", "2.16.840.1.113883.3.72.5.9.1", "", "");
		} catch (HL7Exception e) {
			throw new TransformerException(this, e);
		}
		
		// add request to session prop so that we can access it when
		// processing the response in PIXQueryResponseTransformer
		message.setSessionProperty("PIX-ITI-9", pix_query);
		message.setSessionProperty("PIX-ITI-9_MSH-10", _msh10);
		
		return pix_query;
	}

	public String constructPIXQuery(String id, String assigningAuthority, String assigningAuthorityId, String requestedAssigningAuthority, String requestedAssigningAuthorityId) throws HL7Exception {
		
		QBP_Q21 qbp_q21 = new QBP_Q21();
		Terser t = new Terser(qbp_q21);
		
		MSH msh = (MSH) t.getSegment("MSH");
		t.set("MSH-1", "|");
		t.set("MSH-2", "^~\\&");
		t.set("MSH-3-1", "sending_application"); // check
		t.set("MSH-4-1", "recieving_application"); // check
		t.set("MSH-5-1", ""); // check
		t.set("MSH-6-1", ""); // check
		/* NIST Testing values */
		//t.set("MSH-3-1", "openhim"); //sending application
		//t.set("MSH-4-1", "RHEA-HIE"); //sending facility
		//t.set("MSH-5-1", "NIST_RCVR_HANNES"); //receiving application
		//t.set("MSH-6-1", "NIST"); //receiving facility
		/* */
		msh.getDateTimeOfMessage().getTime().setValue(new Date());
		t.set("MSH-9-1", "QBP");
		t.set("MSH-9-2", "Q23");
		t.set("MSH-9-3", "QBP_Q21");
		//MSH-10 message control id
		_msh10 = UUID.randomUUID().toString();
		t.set("MSH-10", _msh10); // check
		t.set("MSH-11-1", "P");
		t.set("MSH-12-1-1", "2.5");
		
		t.set("QPD-1-1", "IHE PIX Query");
		t.set("QPD-2", UUID.randomUUID().toString());
		t.set("QPD-3-1", id);
		t.set("QPD-3-4", assigningAuthority);
		t.set("QPD-3-4-2", assigningAuthorityId);
		t.set("QPD-3-4-3", "ISO");
		t.set("QPD-3-5", "PI");
		
		t.set("QPD-4-4", requestedAssigningAuthority);
		t.set("QPD-4-4-2", requestedAssigningAuthorityId);
		t.set("QPD-4-4-3", "ISO");
		t.set("QPD-4-5", "PI");
		
		t.set("RCP-1", "I");
		
		Parser p = new GenericParser();
		return p.encode(qbp_q21);
	}
}
