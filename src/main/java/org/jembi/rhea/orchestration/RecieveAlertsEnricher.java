/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.orchestration;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jembi.rhea.Constants;
import org.jembi.rhea.RestfulHttpRequest;
import org.mule.api.MuleContext;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.module.client.MuleClient;

import ca.uhn.hl7v2.model.v25.datatype.CX;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.model.v25.segment.PV1;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;

public class RecieveAlertsEnricher implements Callable {
	
	Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		MuleContext muleContext = eventContext.getMuleContext();
		MuleClient client = new MuleClient(muleContext);
		MuleMessage msg = eventContext.getMessage();
		
		RestfulHttpRequest payload = (RestfulHttpRequest) msg.getPayload();
		String oru_r01_str = payload.getBody();
		
		Parser p = new GenericParser();
		ORU_R01 oru_r01 = (ORU_R01) p.parse(oru_r01_str);
		
		// Replace EPID with Provider NID
		PV1 pv1 = oru_r01.getPATIENT_RESULT().getPATIENT().getVISIT().getPV1();
		String proIDType = pv1.getAttendingDoctor(0).getIdentifierTypeCode().getValue();
		String proID = pv1.getAttendingDoctor(0).getIDNumber().getValue();
		
		if (proID == null || proIDType == null) {
			throw new Exception("Invalid Provider: id or id type is null");
		}
		
		Map<String, String> ProIdMap = new HashMap<String, String>();
		ProIdMap.put("id", proID);
		ProIdMap.put("idType", proIDType);
		
		MuleMessage responce = client.send("vm://getnid-openldap", ProIdMap, null, 5000);
		
		String proNID = null;
		String success = responce.getInboundProperty("success");
		if (success.equals("true")) {
			proNID = responce.getPayloadAsString();
			
			// Enrich message
			pv1.getAttendingDoctor(0).getIdentifierTypeCode().setValue(Constants.NID_ID_TYPE);
			pv1.getAttendingDoctor(0).getIDNumber().setValue(proNID);
			
			log.info("Validated Provider and enriched message with Provider NID");
		} else {
			throw new Exception("Invalid Provider: NID for EPID:" + proID + " could not be found in Provier Registry");
		}
		
		// Replace ECID with Client NID
		PID pid = oru_r01.getPATIENT_RESULT().getPATIENT().getPID();
		CX[] patientIdentifierList = pid.getPatientIdentifierList();
		
		String nid = null;
		if (patientIdentifierList.length < 1) {
			throw new Exception("Invalid client ID");
		}
		
		String id = patientIdentifierList[0].getIDNumber().getValue();
		String idType = patientIdentifierList[0].getIdentifierTypeCode().getValue();
		
		Map<String, String> idMap = new HashMap<String, String>();
		idMap.put("id", id);
		idMap.put("idType", idType);
		
		responce = client.send("vm://getnid-openempi", idMap, null, 5000);
		
		success = responce.getInboundProperty("success");
		if (success != null && success.equals("true")) {
			nid = responce.getPayloadAsString();
		}
		
		if (nid == null) {
			throw new Exception("Invalid Client: NID for ECID:" + id + " could not be found in Client Registry");
		} else {
			// Enrich the message
			CX idCX = pid.getPatientIdentifierList(0);
			idCX.getIdentifierTypeCode().setValue(Constants.NID_ID_TYPE);
			idCX.getIDNumber().setValue(nid);
			
			log.info("Validated Client and enriched message with Client NID");
		}
		
		payload.setBody(p.encode(oru_r01, "XML"));
		
		log.info("Recieve Alerts, enriched message: " + payload.getBody());
		
		return msg;
	}
	
}
