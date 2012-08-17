package org.jembi.rhea.transformers;

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
			if (dob_str.length() < 10) {
				dob_str += "%";
			}
			body += "<dateOfBirth>" + dob_str + "</dateOfBirth>\n";
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
