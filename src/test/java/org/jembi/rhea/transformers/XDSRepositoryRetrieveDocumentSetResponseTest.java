package org.jembi.rhea.transformers;

import static org.junit.Assert.fail;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.jembi.ihe.xds.XDSAffinityDomain;
import org.jembi.rhea.RestfulHttpRequest;
import org.junit.Test;
import org.mule.api.ExceptionPayload;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.DataType;
import org.mule.api.transformer.Transformer;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import junit.framework.Assert;

public class XDSRepositoryRetrieveDocumentSetResponseTest {
	
	@Test
	public void testTransformMessageMuleMessageString() throws JAXBException {
		MuleMessage testMsg = new TestMuleMessage();
		
		XDSRepositoryRetrieveDocumentSetResponse t = new XDSRepositoryRetrieveDocumentSetResponse();
		try {
			String document = (String) t.transformMessage(testMsg, null);
			Assert.assertNotNull(document);
			Assert.assertEquals(TEST_ORU_R01_MSG,document);		
			System.out.println(document);
			
		} catch (TransformerException e) {
			fail("Failed due to exception " + e);
			e.printStackTrace();
		}
	}
	
	private static class TestMuleMessage implements MuleMessage {
		@Override
		public Object getPayload() {
			RetrieveDocumentSetResponseType payload = new RetrieveDocumentSetResponseType();
			RetrieveDocumentSetResponseType.DocumentResponse dr = new RetrieveDocumentSetResponseType.DocumentResponse();
			dr.setDocument(TEST_ORU_R01_MSG.getBytes());
			dr.setDocumentUniqueId("111111111");
			dr.setHomeCommunityId(XDSAffinityDomain.IHE_CONNECTATHON_NA2013_RHEAHIE.getHomeCommunityId());
			dr.setMimeType("text/plain");
			dr.setRepositoryUniqueId("1");
			payload.getDocumentResponse().add(dr);
			return payload;
		}

		@Override
		public String getPayloadAsString() throws Exception {
			return (String)getPayload();
		}

		@Override public void addProperties(Map<String, Object> properties) {}
		@Override public void addProperties(Map<String, Object> properties, PropertyScope scope) {}
		@Override public void clearProperties() {}
		@Override public void clearProperties(PropertyScope scope) {}
		@Override public Object getProperty(String key) { return null; }
		@Override public void setProperty(String key, Object value) {}
		@Override public void setInvocationProperty(String key, Object value) {}
		@Override public void setOutboundProperty(String key, Object value) {}
		@Override public void setProperty(String key, Object value, PropertyScope scope) {}
		@Override public Object removeProperty(String key) { return null; }
		@Override public Object removeProperty(String key, PropertyScope scope) { return null; }
		@Override public Set<String> getPropertyNames() { return null; }
		@Override public Set<String> getPropertyNames(PropertyScope scope) { return null; }
		@Override public Set<String> getInvocationPropertyNames() { return null; }
		@Override public Set<String> getInboundPropertyNames() { return null; }
		@Override public Set<String> getOutboundPropertyNames() { return null; }
		@Override public Set<String> getSessionPropertyNames() { return null; }
		@Override public String getUniqueId() { return null; }
		@Override public String getMessageRootId() { return null; }
		@Override public void setMessageRootId(String rootId) {}
		@Override public void propagateRootId(MuleMessage parent) {}
		@Override public Object getProperty(String name, Object defaultValue) { return null; }
		@Override public <T> T getProperty(String name, PropertyScope scope) { return null; }
		@Override public <T> T getInboundProperty(String name, T defaultValue) { return null; }
		@Override public <T> T getInboundProperty(String name) { return null; }
		@Override public <T> T getInvocationProperty(String name, T defaultValue) { return null; }
		@Override public <T> T getInvocationProperty(String name) { return null; }
		@Override public <T> T getOutboundProperty(String name, T defaultValue) { return null; }
		@Override public <T> T getOutboundProperty(String name) { return null; }
		@Override public <T> T findPropertyInAnyScope(String name, T defaultValue) { return null; }
		@Override public <T> T getProperty(String name, PropertyScope scope, T defaultValue) { return null; }
		@Override public int getIntProperty(String name, int defaultValue) { return 0; }
		@Override public long getLongProperty(String name, long defaultValue) { return 0; }
		@Override public double getDoubleProperty(String name, double defaultValue) { return 0; }
		@Override public String getStringProperty(String name, String defaultValue) { return null; }
		@Override public boolean getBooleanProperty(String name, boolean defaultValue) { return false; }
		@Override public void setBooleanProperty(String name, boolean value) {}
		@Override public void setIntProperty(String name, int value) {}
		@Override public void setLongProperty(String name, long value) {}
		@Override public void setDoubleProperty(String name, double value) {}
		@Override public void setStringProperty(String name, String value) {}
		@Override public void setCorrelationId(String id) {}
		@Override public String getCorrelationId() { return null; }
		@Override public int getCorrelationSequence() { return 0; }
		@Override public void setCorrelationSequence(int sequence) {}
		@Override public int getCorrelationGroupSize() { return 0; }
		@Override public void setCorrelationGroupSize(int size) {}
		@Override public void setReplyTo(Object replyTo) {}
		@Override public Object getReplyTo() { return null; }
		@Override public ExceptionPayload getExceptionPayload() { return null; }
		@Override public void setExceptionPayload(ExceptionPayload payload) {}
		@Override public void addAttachment(String name, DataHandler dataHandler) {}
		@Override public void addOutboundAttachment(String name, DataHandler dataHandler) {}
		@Override public void addOutboundAttachment(String name, Object object, String contentType) throws Exception {}
		@Override public void removeAttachment(String name) throws Exception {}
		@Override public void removeOutboundAttachment(String name) throws Exception {}
		@Override public DataHandler getAttachment(String name) { return null; }
		@Override public DataHandler getInboundAttachment(String name) { return null; }
		@Override public DataHandler getOutboundAttachment(String name) { return null; }
		@Override public Set<String> getAttachmentNames() { return null; }
		@Override public Set<String> getInboundAttachmentNames() { return null; }
		@Override public Set<String> getOutboundAttachmentNames() { return null; }
		@Override public String getEncoding() { return null; }
		@Override public void setEncoding(String encoding) {}
		@Override public void release() {}
		@Override public void applyTransformers(MuleEvent event, List<? extends Transformer> transformers) throws MuleException {}
		@Override public void applyTransformers(MuleEvent event, Transformer... transformers) throws MuleException {}
		@Override public void applyTransformers(MuleEvent event, List<? extends Transformer> transformers, Class<?> outputType) throws MuleException {}
		@Override public void setPayload(Object payload) {}
		@Override public <T> T getPayload(Class<T> outputType) throws TransformerException { return null; }
		@Override public <T> T getPayload(DataType<T> outputType) throws TransformerException { return null; }
		@Override public String getPayloadAsString(String encoding) throws Exception { return null; }
		@Override public byte[] getPayloadAsBytes() throws Exception { return null; }
		@Override public Object getOriginalPayload() { return null; }
		@Override public String getPayloadForLogging() { return null; }
		@Override public String getPayloadForLogging(String encoding) { return null; }
		@Override public MuleContext getMuleContext() { return null; }
		@Override public DataType<?> getDataType() { return null; }
		@Override public <T> T getSessionProperty(String name, T defaultValue) { return null; }
		@Override public <T> T getSessionProperty(String name) { return null; }
		@Override public void setSessionProperty(String key, Object value) {}
		@Override public MuleMessage createInboundMessage() throws Exception { return null; }
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
		"		    <CX.5>MOH_CAAT_MARC_HI</CX.5>" +
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
