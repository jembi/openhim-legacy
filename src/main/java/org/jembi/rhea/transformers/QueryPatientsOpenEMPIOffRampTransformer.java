package org.jembi.rhea.transformers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.Util;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class QueryPatientsOpenEMPIOffRampTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage msg, String enc)
			throws TransformerException {
		
		RestfulHttpRequest request = (RestfulHttpRequest) msg.getPayload();
		Map<String, String> requestParams = request.getRequestParams();
		
		String body = "<?xml version='1.0' encoding='UTF-8'?>\n";
		
		body += "<person>\n";
		
		//dateofbirth
		String dob_str = requestParams.get("dob");
		if (dob_str != null) {
			SimpleDateFormat sdf_input1 = new SimpleDateFormat("yyyy");
			SimpleDateFormat sdf_input2 = new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat sdf_input3 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf_output = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			try {
				Date dob = null;
				boolean skip = false;
				if (dob_str.length() == 4) {
					dob = sdf_input1.parse(dob_str);
					skip = true;
				} else if (dob_str.length() == 7) {
					dob = sdf_input2.parse(dob_str);
					skip = true;
				} else if (dob_str.length() == 10) {
					dob = sdf_input3.parse(dob_str);
				}
				
				// TODO Need OpenEMPI to support partial dates, remove skip boolean when they do
				if (dob != null && !skip) {
					body += "<dateOfBirth>" + sdf_output.format(dob) + "</dateOfBirth>\n";
				}
				
			} catch (ParseException e) {
				throw new TransformerException(this, e);
			}
		}
		
		//familyname
		String familyName = requestParams.get("family_name");
		if (familyName != null) {
			body += "<familyName>" + familyName + "</familyName>\n";
		}
		
		//givenname
		String givenName = requestParams.get("given_name");
		if (givenName != null) {
			body += "<givenName>" + givenName + "</givenName>\n";
		}
		
		//gender - does this work in OpenEMPI??
		//String gender = requestParams.get("gender");
		
		//identifiers
		String id_str = requestParams.get("id");
		if (id_str != null) {
			String[] splitIdentifer = Util.splitIdentifer(id_str);
			body += "<personIdentifiers>\n";
			body += "	<identifier>" + splitIdentifer[1] + "</identifier>\n";
			body += "	<identifierDomain>\n";
			body += "		<universalIdentifier>" + splitIdentifer[0] + "</universalIdentifier>\n";
			body += "		<universalIdentifierTypeCode>" + splitIdentifer[0] + "</universalIdentifierTypeCode>\n";
			body += "	</identifierDomain>\n";
			body += "</personIdentifiers>\n";
		}
		
		body += "</person>\n";
		
		request.setBody(body);
		
		return msg;
	}

}
