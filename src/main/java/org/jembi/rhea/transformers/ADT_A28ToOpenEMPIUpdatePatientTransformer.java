package org.jembi.rhea.transformers;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

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
			// Names
			XPathExpression expression = xpath.compile("/ADT_A05/PID/PID.5/XPN.2");
			Node givenNameNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String givenName = givenNameNode.getTextContent();
			
			expression = xpath.compile("/ADT_A05/PID/PID.5/XPN.1/FN.1");
			Node familyNameNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String familyName = familyNameNode.getTextContent();
			
			// DoB
			expression = xpath.compile("/ADT_A05/PID/PID.7/TS.1");
			Node dobNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String dob = dobNode.getTextContent();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date = sdf.parse(dob);
			sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			dob = sdf.format(date);
			
			// Gender
			expression = xpath.compile("/ADT_A05/PID/PID.8");
			Node genderCodeNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String genderCode = genderCodeNode.getTextContent();
			
			// Address
			expression = xpath.compile("/ADT_A05/PID/PID.11/XAD.6");
			Node countryNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String country = countryNode.getTextContent();
			
			expression = xpath.compile("/ADT_A05/PID/PID.11/XAD.4");
			Node provinceNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String province = provinceNode.getTextContent();
			
			expression = xpath.compile("/ADT_A05/PID/PID.11/XAD.3");
			Node districtNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String district = districtNode.getTextContent();
			
			expression = xpath.compile("/ADT_A05/PID/PID.11/XAD.9");
			Node sectorNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String sector = sectorNode.getTextContent();
			
			expression = xpath.compile("/ADT_A05/PID/PID.11/XAD.9");
			Node cellNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String cell = cellNode.getTextContent();
			
			expression = xpath.compile("/ADT_A05/PID/PID.11/XAD.9");
			Node villageNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String village = villageNode.getTextContent();
			
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
			
			// Names
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
			
			// DoB
			expression = xpath.compile("/person/dateOfBirth");
			dobNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			if (dobNode == null) {
				// Create node
				Element newElement = document.createElement("dateOfBirth");
				newElement.setTextContent(dob);
				personNode.appendChild(newElement);
			} else {
				// update node
				dobNode.setTextContent(dob);
			}
			
			// Gender
			expression = xpath.compile("/person/gender");
			Node genderNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			if (genderNode != null) {
				// remove gender node
				genderNode.getParentNode().removeChild(genderNode);
			}
			// add gender element
			Element genderElement = document.createElement("gender");
			Element genderCodeElement = document.createElement("genderCode");
			genderCodeElement.setTextContent(genderCode);
			genderElement.appendChild(genderCodeElement);
			personNode.appendChild(genderNode);
			
			// Address
			expression = xpath.compile("/person/country");
			countryNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			if (countryNode == null) {
				// Create node
				Element newElement = document.createElement("country");
				newElement.setTextContent(country);
				personNode.appendChild(newElement);
			} else {
				// update node
				countryNode.setTextContent(country);
			}
			
			expression = xpath.compile("/person/state");
			provinceNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			if (provinceNode == null) {
				// Create node
				Element newElement = document.createElement("state");
				newElement.setTextContent(province);
				personNode.appendChild(newElement);
			} else {
				// update node
				provinceNode.setTextContent(province);
			}
			
			expression = xpath.compile("/person/city");
			districtNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			if (districtNode == null) {
				// Create node
				Element newElement = document.createElement("city");
				newElement.setTextContent(district);
				personNode.appendChild(newElement);
			} else {
				// update node
				districtNode.setTextContent(district);
			}
			
			expression = xpath.compile("/person/address2");
			sectorNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			if (sectorNode == null) {
				// Create node
				Element newElement = document.createElement("address2");
				newElement.setTextContent(sector);
				personNode.appendChild(newElement);
			} else {
				// update node
				sectorNode.setTextContent(sector);
			}
			
			expression = xpath.compile("/person/address1");
			cellNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			if (cellNode == null) {
				// Create node
				Element newElement = document.createElement("address1");
				newElement.setTextContent(cell);
				personNode.appendChild(newElement);
			} else {
				// update node
				cellNode.setTextContent(cell);
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
