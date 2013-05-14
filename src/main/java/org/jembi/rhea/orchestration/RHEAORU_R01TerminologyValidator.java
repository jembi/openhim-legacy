/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.orchestration;

import java.util.HashMap;
import java.util.Map;

import org.jembi.rhea.RestfulHttpRequest;
import org.mule.api.MuleContext;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.module.client.MuleClient;

import ca.uhn.hl7v2.model.v25.group.ORU_R01_OBSERVATION;
import ca.uhn.hl7v2.model.v25.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;

public class RHEAORU_R01TerminologyValidator implements Callable {
	
	private static boolean VALIDATE_TERMINOLOGY = true;
	
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		MuleContext muleContext = eventContext.getMuleContext();
		MuleClient client = new MuleClient(muleContext);
		MuleMessage msg = eventContext.getMessage();
		
		if (!VALIDATE_TERMINOLOGY) {
			return msg;
		}
		
		RestfulHttpRequest payload = (RestfulHttpRequest) msg.getPayload();
		String oru_r01_str = payload.getBody();
		
		validateTerminologyInORU_R01(oru_r01_str, new MuleFlowValidator(client));
		
		return msg;
	}
	
	protected void validateTerminologyInORU_R01(String oru_r01_str, CodeValidator validator) throws Exception {
		Parser p = new GenericParser();
		ORU_R01 oru_r01 = (ORU_R01) p.parse(oru_r01_str);
		
		int reps = oru_r01.getPATIENT_RESULT().getORDER_OBSERVATIONReps();
		
		for (int j = 0 ; j < reps ; j++) {
			ORU_R01_ORDER_OBSERVATION observations = oru_r01.getPATIENT_RESULT().getORDER_OBSERVATION(j);
			
			for (int i = 0 ; i < observations.getOBSERVATIONReps() ; i++) {
				ORU_R01_OBSERVATION obs = observations.getOBSERVATION(i);
				String id = obs.getOBX().getObservationIdentifier().getIdentifier().getValue();
				String namespace = obs.getOBX().getObservationIdentifier().getCe3_NameOfCodingSystem().getValue();
				
				if (id == null || id.equals("") || namespace == null || namespace.equals("")) {
					throw new InvalidTerminologyException("No code or namespace set in ORU_R01, these are required.");
				}
				
				if (!validator.validateCode(namespace, id)) {
					throw new UnknownTerminologyException("Unknown term used in ORU_R01, please use only RHEA codes. Unknown code: " + namespace + " " + id);
				}
			}
		}
	}
	
	public static interface CodeValidator {
		boolean validateCode(String namespace, String code) throws Exception;
	}
	
	public static class MuleFlowValidator implements CodeValidator {
		private MuleClient client;
		private Map<String, String> idMap = new HashMap<String, String>();
		
		public MuleFlowValidator(MuleClient client) {
			this.client = client;
		}
		
		@Override
		public boolean validateCode(String namespace, String code) throws MuleException {
			idMap.put("id", code);
			idMap.put("namespace", namespace);
			
			MuleMessage response = client.send("vm://validateterm", idMap, null);
			String success = response.getInboundProperty("success");
			return success.equals("true");
		}
	}
	
	public static class UnknownTerminologyException extends Exception {
		private static final long serialVersionUID = 1L;

		public UnknownTerminologyException() {
			super();
		}
		
		public UnknownTerminologyException(String msg) {
			super(msg);
		}
		
		public UnknownTerminologyException(Throwable t) {
			super(t);
		}
	}
	
	public static class InvalidTerminologyException extends Exception {
		private static final long serialVersionUID = 1L;

		public InvalidTerminologyException() {
			super();
		}
		
		public InvalidTerminologyException(String msg) {
			super(msg);
		}
		
		public InvalidTerminologyException(Throwable t) {
			super(t);
		}
	}
}
