package org.jembi.rhea.transformers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jembi.rhea.RestfulHttpResponse;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;

import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.util.Terser;

public class XDSRepositoryResponseToRestfulHttpResponseTransformer extends
		AbstractMessageTransformer {
	
	private final Log log = LogFactory.getLog(this.getClass());

	@SuppressWarnings("unchecked")
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		RestfulHttpResponse res = new RestfulHttpResponse();
		
		List<String> documentList = (List<String>)message.getPayload();
		
		if (documentList.size() > 0) {
			res.setHttpStatus(200);
		} else {
			res.setHttpStatus(400);
			return res;
		}
		
		documentList = tranformORU_R01ToMultipeEncounterForm(documentList);
		
		String elid = message.getProperty("elid", PropertyScope.SESSION);
		String oru_r01_str = combindORU_R01Messages(documentList, elid);
		
		res.setBody(oru_r01_str);

		return res;
	}

	private List<String> tranformORU_R01ToMultipeEncounterForm(
			List<String> documentList) {
		
		GenericParser parser = new GenericParser();
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddmmss");
		List<String> newDocList = new ArrayList<String>();
		
		 for (String oru_r01_str : documentList) {
			try {
				ORU_R01 oru_r01 = null;
				
				try {
					oru_r01 = (ORU_R01) parser.parse(oru_r01_str);
				} catch (EncodingNotSupportedException ex) {
					//Seems we're getting docs back from certain OpenXDS instances
					//as a hex encoded string, with an incorrect starting character
					String doc = decodeWeirdHex(oru_r01_str);
					oru_r01 = (ORU_R01) parser.parse(doc);
				}
				Terser t = new Terser(oru_r01);
				
				String facilityId = t.get("/PATIENT_RESULT/PATIENT/VISIT/PV1-3-1");
				String facilityName = t.get("/PATIENT_RESULT/PATIENT/VISIT/PV1-3-4");
				String encType = t.get("/PATIENT_RESULT/PATIENT/VISIT/PV1-4");
				String proId = t.get("/PATIENT_RESULT/PATIENT/VISIT/PV1-7-1-1");
				String proFamilyName = t.get("/PATIENT_RESULT/PATIENT/VISIT/PV1-7-2-1");
				String proGivenName = t.get("/PATIENT_RESULT/PATIENT/VISIT/PV1-7-3-1");
				String proIdType = t.get("/PATIENT_RESULT/PATIENT/VISIT/PV1-7-13-1");
				String encounterDate = t.get("/PATIENT_RESULT/PATIENT/VISIT/PV1-44-1");
				
				t.set("/PATIENT_RESULT/ORDER_OBSERVATION/OBR-20", facilityId);
				t.set("/PATIENT_RESULT/ORDER_OBSERVATION/OBR-21", facilityName);
				t.set("/PATIENT_RESULT/ORDER_OBSERVATION/OBR-4-2", encType);
				t.set("/PATIENT_RESULT/ORDER_OBSERVATION/OBR-16-1-1", proId);
				t.set("/PATIENT_RESULT/ORDER_OBSERVATION/OBR-16-2-1", proFamilyName);
				t.set("/PATIENT_RESULT/ORDER_OBSERVATION/OBR-16-3-1", proGivenName);
				t.set("/PATIENT_RESULT/ORDER_OBSERVATION/OBR-16-13-1", proIdType);
				t.set("/PATIENT_RESULT/ORDER_OBSERVATION/OBR-7-1", encounterDate);
				
				oru_r01.getPATIENT_RESULT().getPATIENT().getVISIT().clear();
				
				String new_oru_r01_str = parser.encode(oru_r01, "XML");
				
				newDocList.add(new_oru_r01_str);
				
				System.out.println(new_oru_r01_str);
				
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Could not parse recieved document as ORU_R01 message: ", e);
			}
		 }
		
		return newDocList;
	}

	private String combindORU_R01Messages(List<String> documentList, String elid) {
		// combine all document into a single ORU_R01 message
		// Hapi attempt
		/*Parser parser = new GenericParser();
		ORU_R01 oru_r01 = null;
		
		try {
			//Message msg = parser.parse(oru_r01_str);
			//oru_r01 = (ORU_R01) msg;
		
			// HAPI attempt
			for (int i = 1 ; i < documentList.size() ; i++) {
				Message msgToAdd = parser.parse(documentList.get(0));
				ORU_R01 oru_r01ToAdd = (ORU_R01) msgToAdd;
				
				if (oru_r01 == null) {
					oru_r01 = oru_r01ToAdd;
					continue;
				}
				
				int observationReps = oru_r01ToAdd.getPATIENT_RESULT().getORDER_OBSERVATIONReps();
				
				for (int j = 0; j < observationReps; j++) {
					ORU_R01_ORDER_OBSERVATION observation = oru_r01ToAdd.getPATIENT_RESULT().getORDER_OBSERVATION(j);
					oru_r01.getPATIENT_RESULT().insertORDER_OBSERVATION(observation, oru_r01.getPATIENT_RESULT().getORDER_OBSERVATIONReps());
				}
			}
			
			res.setBody(parser.encode(oru_r01));
		
		} catch (EncodingNotSupportedException e) {
			log.error("Could not parse documents into a HL7 message: " + e);
		} catch (HL7Exception e) {
			log.error("Could not parse documents into a HL7 message: " + e);
		}*/
		
		// String processing attempt
		String oru_r01_str = null;
		
		for (int i = 0 ; i < documentList.size() ; i++) {
			String oru_r01ToAdd_str = documentList.get(i);
			// skip messages from this location
			if (oru_r01ToAdd_str.contains("<HD.1>"+ elid +"</HD.1>")) {
				continue;
			}
			if (oru_r01ToAdd_str.contains("ORU_R01") && oru_r01_str == null) {
				oru_r01_str = oru_r01ToAdd_str;
				continue;
			}
			
			if (oru_r01ToAdd_str.contains("ORU_R01")) {
				int lastOrderObsIndex = oru_r01_str.lastIndexOf("</ORU_R01.ORDER_OBSERVATION>") + "</ORU_R01.ORDER_OBSERVATION>".length();
			
				int beginIndex = oru_r01ToAdd_str.indexOf("<ORU_R01.ORDER_OBSERVATION>");
				int endIndex = oru_r01ToAdd_str.lastIndexOf("</ORU_R01.ORDER_OBSERVATION>") + "</ORU_R01.ORDER_OBSERVATION>".length();
				String sectionToAdd = oru_r01ToAdd_str.substring(beginIndex, endIndex);
				
				String beginningSection = oru_r01_str.substring(0, lastOrderObsIndex);
				String endSection = oru_r01_str.substring(lastOrderObsIndex);
				
				oru_r01_str = beginningSection + sectionToAdd + endSection;
			}
		}
		return oru_r01_str;
	}

	//Why is it weird? it starts with a non-hex single character 'K'
	//(which is supposed to encode the character '<')
	//TODO figure out what's going on. it's likely an issue with OpenXDS though
	private static String decodeWeirdHex(String oruDoc) {
		String hex = oruDoc.substring(1);
		return "<" + convertHexToString(hex);
	}
	
	/*
	 * From mkyong's excellent blog at:
	 * http://www.mkyong.com/java/how-to-convert-hex-to-ascii-in-java/
	 * */
	public static String convertHexToString(String hex){
		 
		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();
	 
		//49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for( int i=0; i<hex.length()-1; i+=2 ){
	 
			//grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			//convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			//convert the decimal to character
			sb.append((char)decimal);
	 
			temp.append(decimal);
		}
	 
		return sb.toString();
	}
	/**/
}
