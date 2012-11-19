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

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		Map<String, String> idMap = (Map<String, String>) message.getPayload();
		
		String id = idMap.get("id");
		String idType = idMap.get("idType");
		
		String pix_query;
		try {
			pix_query = constructPIXQuery(id, idType, null);
		} catch (HL7Exception e) {
			throw new TransformerException(this, e);
		}
		
		// add MLLP header and footer chars
		pix_query = "\013" + pix_query + "\034\r";
		
		return pix_query;
	}

	public String constructPIXQuery(String id, String assigningAuthority, String assigningAuthorityId) throws HL7Exception {
		
		QBP_Q21 qbp_q21 = new QBP_Q21();
		Terser t = new Terser(qbp_q21);
		
		MSH msh = (MSH) t.getSegment("MSH");
		t.set("MSH-1", "|");
		t.set("MSH-2", "^~\\&");
		t.set("MSH-3-1", "sending_application"); // check
		t.set("MSH-4-1", "recieving_application"); // check
		t.set("MSH-5-1", ""); // check
		t.set("MSH-6-1", ""); // check
		msh.getDateTimeOfMessage().getTime().setValue(new Date());
		t.set("MSH-9-1", "QBP");
		t.set("MSH-9-2", "Q23");
		t.set("MSH-9-3", "QBP_Q21");
		t.set("MSH-10", "123"); // check
		t.set("MSH-11-1", "P");
		t.set("MSH-12-1-1", "2.5");
		
		t.set("QPD-1-1", "IHE PIX Query");
		t.set("QPD-1-2", UUID.randomUUID().toString());
		t.set("QPD-3-1", id);
		t.set("QPD-3-4", assigningAuthority);
		t.set("QPD-3-4-2", assigningAuthorityId);
		t.set("QPD-3-4-3", "ISO");
		t.set("QPD-3-5", "PI");
		
		t.set("RCP-1", "I");
		
		Parser p = new GenericParser();
		return p.encode(qbp_q21);
	}

}
