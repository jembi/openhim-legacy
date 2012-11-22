package org.jembi.rhea.transformers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;

import ca.uhn.hl7v2.HL7Exception;

public class XDSRepositoryProvideAndRegisterDocumentTest {
	
	@Test
	public void testParseEncounterRequest() {
		try {
			XDSRepositoryProvideAndRegisterDocument.EncounterInfo enc =
				XDSRepositoryProvideAndRegisterDocument.parseEncounterRequest(TEST_ORU_R01_MSG);
			assertEquals(TEST_ENC.pid, enc.pid);
			assertEquals(TEST_ENC.firstName, enc.firstName);
			assertEquals(TEST_ENC.lastName, enc.lastName);
			assertEquals(TEST_ENC.encounterDateTime, enc.encounterDateTime);
			assertEquals(TEST_ENC.location, enc.location);
			assertEquals(TEST_ENC.attendingDoctorID, enc.attendingDoctorID);
			assertEquals(TEST_ENC.attendingDoctorFirstName, enc.attendingDoctorFirstName);
			assertEquals(TEST_ENC.attendingDoctorLastName, enc.attendingDoctorLastName);
		} catch (HL7Exception e) {
			fail("Failed due to exception: " + e);
		}
	}
	
	@Test
	public void testBuildRegisterRequest() {
		ProvideAndRegisterDocumentSetRequestType request =
			XDSRepositoryProvideAndRegisterDocument.buildRegisterRequest(TEST_ENC);
		
		try {
			JAXBContext jc = JAXBContext.newInstance("ihe.iti.xds_b._2007");
			Marshaller marshaller = jc.createMarshaller();
			StringWriter sw = new StringWriter();
			marshaller.marshal(request, sw);
			System.out.println(sw.toString());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
			
		fail("Oh no!");
	}
	
	private static XDSRepositoryProvideAndRegisterDocument.EncounterInfo TEST_ENC;
	
	static {
		TEST_ENC = new XDSRepositoryProvideAndRegisterDocument.EncounterInfo();
		TEST_ENC.pid = "2552234100";
		TEST_ENC.encounterDateTime = "201211150855";
		TEST_ENC.firstName = "Mary"; TEST_ENC.lastName = "Patient";
		TEST_ENC.encounterDateTime = "201211150855";
		TEST_ENC.location = "Simbi MU";
		TEST_ENC.attendingDoctorID = "3525410";
		TEST_ENC.attendingDoctorFirstName = "John";
		TEST_ENC.attendingDoctorLastName = "Doctor";
	}

	private static final String TEST_ORU_R01_MSG =
		"<?xml version=\"1.0\"?>" +
		"<ORU_R01 xmlns=\"urn:hl7-org:v2xml\">" +
		"    <MSH>" +
		"	<MSH.1>|</MSH.1>" +
		"	<MSH.2>^~\\&amp;amp;</MSH.2>" +
		"	<MSH.4>" +
		"	    <HD.1>357</HD.1>" +
		"	</MSH.4>" +
		"	<MSH.6>" +
		"	    <HD.1>Shared Health Record</HD.1>" +
		"	</MSH.6>" +
		"	<MSH.7>" +
		"	    <TS.1>20121115205521</TS.1>" +
		"	</MSH.7>" +
		"	<MSH.9>" +
		"	    <MSG.1>ORU</MSG.1>" +
		"	    <MSG.2>R01</MSG.2>" +
		"	    <MSG.3>ORU_R01</MSG.3>" +
		"	</MSH.9>" +
		"	<MSH.10>d5f96a73-3cb3-4297-8a04-9b08c51e032a</MSH.10>" +
		"	<MSH.11>" +
		"	    <PT.1>D</PT.1>" +
		"	    <PT.2>C</PT.2>" +
		"	</MSH.11>" +
		"	<MSH.12>" +
		"	    <VID.1>2.5</VID.1>" +
		"	    <VID.2>" +
		"		<CE.1>RWA</CE.1>" +
		"	    </VID.2>" +
		"	</MSH.12>" +
		"	<MSH.21>" +
		"	    <EI.1>CLSM_V0.83</EI.1>" +
		"	</MSH.21>" +
		"    </MSH>" +
		"    <ORU_R01.PATIENT_RESULT>" +
		"	<ORU_R01.PATIENT>" +
		"	    <PID>" +
		"		<PID.3>" +
		"		    <CX.1>987654321</CX.1>" +
		"		    <CX.5>NID</CX.5>" +
		"		</PID.3>" +
		"		<PID.3>" +
		"		    <CX.1>2552234100</CX.1>" +
		"		    <CX.5>ECID</CX.5>" +
		"		</PID.3>" +
		"		<PID.3>" +
		"		    <CX.1>100002-P</CX.1>" +
		"		    <CX.5>OMRS357</CX.5>" +
		"		</PID.3>" +
		"		<PID.5>" +
		"		    <XPN.1>" +
		"			<FN.1>Patient</FN.1>" +
		"		    </XPN.1>" +
		"		    <XPN.2>Mary</XPN.2>" +
		"		</PID.5>" +
		"	    </PID>" +
		"	    <ORU_R01.VISIT>" +
		"		<PV1>" +
		"		    <PV1.2>0</PV1.2>" +
		"		    <PV1.3>" +
		"			<PL.1>871</PL.1>" +
		"			<PL.4>" +
		"			    <HD.1>Simbi MU</HD.1>" +
		"			</PL.4>" +
		"		    </PV1.3>" +
		"		    <PV1.4>ANC Referral</PV1.4>" +
		"		    <PV1.7>" +
		"			<XCN.1>3525410</XCN.1>" +
		"			<XCN.2>" +
		"			    <FN.1>Doctor</FN.1>" +
		"			</XCN.2>" +
		"			<XCN.3>John</XCN.3>" +
		"			<XCN.13>EPID</XCN.13>" +
		"		    </PV1.7>" +
		"		    <PV1.44>" +
		"			<TS.1>201211150855</TS.1>" +
		"		    </PV1.44>" +
		"		</PV1>" +
		"	    </ORU_R01.VISIT>" +
		"	</ORU_R01.PATIENT>" +
		"	<ORU_R01.ORDER_OBSERVATION>" +
		"	    <ORC>" +
		"		<ORC.1>RE</ORC.1>" +
		"		<ORC.9>" +
		"		    <TS.1>201211152055</TS.1>" +
		"		</ORC.9>" +
		"		<ORC.12>" +
		"		    <XCN.1>3</XCN.1>" +
		"		</ORC.12>" +
		"		<ORC.16>" +
		"		    <CE.1>Identifier</CE.1>" +
		"		    <CE.2>Text</CE.2>" +
		"		    <CE.3>Name of Coding System</CE.3>" +
		"		</ORC.16>" +
		"	    </ORC>" +
		"	    <OBR>" +
		"		<OBR.1>0</OBR.1>" +
		"		<OBR.3>" +
		"		    <EI.1>228</EI.1>" +
		"		</OBR.3>" +
		"		<OBR.4>" +
		"		    <CE.2>ANC Referral</CE.2>" +
		"		</OBR.4>" +
		"		<OBR.7>" +
		"		    <TS.1>201211150000</TS.1>" +
		"		</OBR.7>" +
		"		<OBR.20>871</OBR.20>" +
		"		<OBR.21>Simbi MU</OBR.21>" +
		"	    </OBR>" +
		"	</ORU_R01.ORDER_OBSERVATION>" +
		"	<ORU_R01.ORDER_OBSERVATION>" +
		"	    <OBR>" +
		"		<OBR.1>1</OBR.1>" +
		"		<OBR.18>0</OBR.18>" +
		"		<OBR.29>" +
		"		    <EIP.2>" +
		"			<EI.3>228</EI.3>" +
		"		    </EIP.2>" +
		"		</OBR.29>" +
		"	    </OBR>" +
		"	    <ORU_R01.OBSERVATION>" +
		"		<OBX>" +
		"		    <OBX.1>0</OBX.1>" +
		"		    <OBX.2>CE</OBX.2>" +
		"		    <OBX.3>" +
		"			<CE.1>8517</CE.1>" +
		"			<CE.2>Referral Urgency</CE.2>" +
		"			<CE.3>RWCS</CE.3>" +
		"		    </OBX.3>" +
		"		    <OBX.5>" +
		"			<CE.1>8612</CE.1>" +
		"			<CE.2>Immediate Referral</CE.2>" +
		"			<CE.3>RWCS</CE.3>" +
		"		    </OBX.5>" +
		"		    <OBX.14>" +
		"			<TS.1>20121115205521</TS.1>" +
		"		    </OBX.14>" +
		"		</OBX>" +
		"	    </ORU_R01.OBSERVATION>" +
		"	    <ORU_R01.OBSERVATION>" +
		"		<OBX>" +
		"		    <OBX.1>1</OBX.1>" +
		"		    <OBX.2>ST</OBX.2>" +
		"		    <OBX.3>" +
		"			<CE.1>57133-1</CE.1>" +
		"			<CE.2>Refer patient to</CE.2>" +
		"			<CE.3>LOINC</CE.3>" +
		"		    </OBX.3>" +
		"		    <OBX.5>67</OBX.5>" +
		"		    <OBX.14>" +
		"			<TS.1>20121115205521</TS.1>" +
		"		    </OBX.14>" +
		"		</OBX>" +
		"	    </ORU_R01.OBSERVATION>" +
		"	    <ORU_R01.OBSERVATION>" +
		"		<OBX>" +
		"		    <OBX.1>2</OBX.1>" +
		"		    <OBX.2>ST</OBX.2>" +
		"		    <OBX.3>" +
		"			<CE.1>42349-1</CE.1>" +
		"			<CE.2>REASON FOR REFERRAL TO ANOTHER SITE</CE.2>" +
		"			<CE.3>LOINC</CE.3>" +
		"		    </OBX.3>" +
		"		    <OBX.5>67</OBX.5>" +
		"		    <OBX.14>" +
		"			<TS.1>20121115205521</TS.1>" +
		"		    </OBX.14>" +
		"		</OBX>" +
		"	    </ORU_R01.OBSERVATION>" +
		"	    <ORU_R01.OBSERVATION>" +
		"		<OBX>" +
		"		    <OBX.1>3</OBX.1>" +
		"		    <OBX.2>TS</OBX.2>" +
		"		    <OBX.3>" +
		"			<CE.1>57202-4</CE.1>" +
		"			<CE.2>DATE OF REFERRAL REQUEST</CE.2>" +
		"			<CE.3>LOINC</CE.3>" +
		"		    </OBX.3>" +
		"		    <OBX.5>" +
		"			<TS.1>20121030000000</TS.1>" +
		"		    </OBX.5>" +
		"		    <OBX.14>" +
		"			<TS.1>20121115</TS.1>" +
		"		    </OBX.14>" +
		"		</OBX>" +
		"	    </ORU_R01.OBSERVATION>" +
		"	</ORU_R01.ORDER_OBSERVATION>" +
		"    </ORU_R01.PATIENT_RESULT>" +
		"</ORU_R01>";

}
