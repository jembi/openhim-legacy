package org.jembi.rhea.transformers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jembi.rhea.Constants;
import org.jembi.rhea.RestfulHttpResponse;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.client.MuleClient;
import org.mule.transformer.AbstractMessageTransformer;

import ca.uhn.hl7v2.model.v25.datatype.CX;
import ca.uhn.hl7v2.model.v25.datatype.XCN;
import ca.uhn.hl7v2.model.v25.group.ORU_R01_PATIENT_RESULT;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.validation.impl.DefaultValidation;

public class QueryEncounterInjectNIDTransformer extends
		AbstractMessageTransformer {
	
	Log log = LogFactory.getLog(this.getClass());

	@Override
	public Object transformMessage(MuleMessage msg, String enc)
			throws TransformerException {
		
		try {
			MuleClient client = new MuleClient(muleContext);
			
			RestfulHttpResponse res = (RestfulHttpResponse) msg.getPayload();
			
			String ORU_R01_str = res.getBody();
			
			if (ORU_R01_str == null || ORU_R01_str.equals("")) {
				return msg;
			}
			
			Parser parser = new GenericParser();
			DefaultValidation defaultValidation = new DefaultValidation();
			parser.setValidationContext(defaultValidation);
			
			ca.uhn.hl7v2.model.Message hl7_msg = parser.parse(ORU_R01_str);
			
			ORU_R01 oru_r01 = (ORU_R01) hl7_msg;
			
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
			
			MuleMessage responce = client.send("vm://getnid-openempi", idMap, null, 5000);
			
			String success = responce.getInboundProperty("success");
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
			
			// Validate and replace provider id's in each obr
			for (int i = 0 ; i < oru_r01.getPATIENT_RESULTReps() ; i++) {
				ORU_R01_PATIENT_RESULT patient_RESULT = oru_r01.getPATIENT_RESULT(i);
				
				for (int j = 0 ; j < patient_RESULT.getORDER_OBSERVATIONReps(); j++) {
					OBR obr = patient_RESULT.getORDER_OBSERVATION(j).getOBR();
					
					// Validate provider ID
					XCN orderingProvider = obr.getObr16_OrderingProvider(0);
					String proID = orderingProvider.getIDNumber().getValue();
					String proIDType = orderingProvider.getIdentifierTypeCode().getValue();
					
					// if this is an obs grouping OBR
					if (proID == null && proIDType == null) {
						break;
					}
					
					// if one of the id values is missing
					if (proID == null || proIDType == null) {
						throw new Exception("Invalid Provider: id or id type is null");
					}
					
					Map<String, String> ProIdMap = new HashMap<String, String>();
					ProIdMap.put("id", proID);
					ProIdMap.put("idType", proIDType);
					
					responce = client.send("vm://getnid-openldap", ProIdMap, null, 5000);
					
					nid = null;
					success = responce.getInboundProperty("success");
					if (success.equals("true")) {
						nid = responce.getPayloadAsString();
						
						// Enrich message
						orderingProvider.getIDNumber().setValue(nid);
						orderingProvider.getIdentifierTypeCode().setValue(Constants.NID_ID_TYPE);
					} else {
						throw new Exception("Invalid Provider: NID for EPID:" + proID + " could not be found in Provier Registry");
					}
				}
			}
			
			String new_hl7_msg = parser.encode(oru_r01, "XML");
			res.setBody(new_hl7_msg);
			
		} catch (Exception e) {
			throw new TransformerException(this, e);
		}
		
		return msg;
	}
}
