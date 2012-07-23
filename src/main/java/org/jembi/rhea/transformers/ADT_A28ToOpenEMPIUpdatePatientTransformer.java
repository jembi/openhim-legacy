package org.jembi.rhea.transformers;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.jembi.rhea.RestfulHttpRequest;
import org.jembi.rhea.Util;
import org.mule.api.MuleMessage;
import org.mule.api.client.LocalMuleClient;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class ADT_A28ToOpenEMPIUpdatePatientTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage msg, String enc)
			throws TransformerException {
		
		try {
			RestfulHttpRequest request = (RestfulHttpRequest) msg.getPayload();
			String ADT_A28_xml = request.getBody();
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			Document document = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(ADT_A28_xml)));

			XPathFactory xpf = XPathFactory.newInstance();
			XPath xpath = xpf.newXPath();
			
			// get fields to be updated out of ADT_A28
			XPathExpression expression = xpath.compile("/ADT_A05/PID/PID.5/XPN.2");
			Node givenNameNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String givenName = givenNameNode.getTextContent();
			
			expression = xpath.compile("/ADT_A05/PID/PID.5/XPN.1/FN.1");
			Node familyNameNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String familyName = familyNameNode.getTextContent();
			
			// Fetch current person record in OpenEMPI
			String path = request.getPath();
			int i = path.lastIndexOf('/');
			String id_str = path.substring(i + 1);
			String[] splitIdentifer = Util.splitIdentifer(id_str);
			String personIdentiferXML = "<personIdentifier>\n" +
			  			  "   <identifier>" + splitIdentifer[1] + "</identifier>\n" +
			  			  "   <identifierDomain>\n" +
			  			  "     <universalIdentifier>" + splitIdentifer[0] + "</universalIdentifier>\n" +
			  			  "     <universalIdentifierTypeCode>" + splitIdentifer[0] + "</universalIdentifierTypeCode>\n" +
			  			  "   </identifierDomain>\n" +
			  			  "</personIdentifier>";
			LocalMuleClient client = msg.getMuleContext().getClient();
			MuleMessage response = client.send("vm://getRawPersonRecordPatient-De-normailization-OpenEMPI", personIdentiferXML, null);
			String personXML = response.getPayloadAsString();
			
			// Replace updated fields
			document = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(personXML)));
			
			expression = xpath.compile("/person");
			Node personNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			
			expression = xpath.compile("/person/givenName");
			givenNameNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			if (givenNameNode == null) {
				// Create node
				Element newElement = document.createElement("givenName");
				newElement.setTextContent(givenName);
				personNode.appendChild(newElement);
			} else {
				// update node
				givenNameNode.setTextContent(givenName);
			}
			
			expression = xpath.compile("/person/familyName");
			familyNameNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			if (familyNameNode == null) {
				// Create node
				Element newElement = document.createElement("familyName");
				newElement.setTextContent(familyName);
				personNode.appendChild(newElement);
			} else {
				// update node
				familyNameNode.setTextContent(familyName);
			}
			
			// Return full updated person xml
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			StringWriter sw = new StringWriter();
			t.transform(new DOMSource(document), new StreamResult(sw));
			
			msg.setPayload(sw.toString());
			
		} catch (Exception e) {
			throw new TransformerException(this, e);
		}
		
		return msg;
	}

}
