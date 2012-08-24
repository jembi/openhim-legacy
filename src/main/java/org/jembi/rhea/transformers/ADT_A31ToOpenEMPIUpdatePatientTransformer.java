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

public class ADT_A31ToOpenEMPIUpdatePatientTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage msg, String enc)
			throws TransformerException {
		
		try {
			RestfulHttpRequest request = (RestfulHttpRequest) msg.getPayload();
			String adt_a31_xml = request.getBody();
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			Document document = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(adt_a31_xml)));

			XPathFactory xpf = XPathFactory.newInstance();
			XPath xpath = xpf.newXPath();
			
			// get fields to be updated out of ADT_A31
			// Names
			XPathExpression expression = xpath.compile("/ADT_A05/PID/PID.5/XPN.2");
			Node givenNameNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String givenName = "";
			if (givenNameNode != null) {
				givenName = givenNameNode.getTextContent();
			}
			
			expression = xpath.compile("/ADT_A05/PID/PID.5/XPN.1/FN.1");
			Node familyNameNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String familyName = "";
			if (familyNameNode != null) {
				familyName = familyNameNode.getTextContent();
			}
			
			// DoB
			expression = xpath.compile("/ADT_A05/PID/PID.7/TS.1");
			Node dobNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String dob = "";
			if (dobNode != null) {
				dob = dobNode.getTextContent();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				Date date = sdf.parse(dob);
				sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				dob = sdf.format(date);
			}
			
			// Gender
			expression = xpath.compile("/ADT_A05/PID/PID.8");
			Node genderCodeNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String genderCode = "";
			if (genderCodeNode != null) {
				genderCode = genderCodeNode.getTextContent();
			}
			
			// Address
			expression = xpath.compile("/ADT_A05/PID/PID.11/XAD.6");
			Node countryNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String country = "";
			if (countryNode != null) {
				country = countryNode.getTextContent();
			}
			
			expression = xpath.compile("/ADT_A05/PID/PID.11/XAD.4");
			Node provinceNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String province = "";
			if (provinceNode != null) {
				province = provinceNode.getTextContent();
			}
			
			expression = xpath.compile("/ADT_A05/PID/PID.11/XAD.3");
			Node districtNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String district = "";
			if (districtNode != null) {
				district = districtNode.getTextContent();
			}
			
			expression = xpath.compile("/ADT_A05/PID/PID.11/XAD.9");
			Node sectorNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String sector = "";
			if (sectorNode != null) {
				sector = sectorNode.getTextContent();
			}
			
			expression = xpath.compile("/ADT_A05/PID/PID.11/XAD.8");
			Node cellNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String cell = "";
			if (cellNode != null) {
				cell = cellNode.getTextContent();
			}
			
			expression = xpath.compile("/ADT_A05/PID/PID.11/XAD.10");
			Node villageNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String village = "";
			if (villageNode != null) {
				village = villageNode.getTextContent();
			}
			
			// Phone Number
			expression = xpath.compile("/ADT_A05/PID/PID.13/XTN.1");
			Node phoneNumberNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String phoneNumber = "";
			if (phoneNumberNode != null) {
				phoneNumber = phoneNumberNode.getTextContent();
			}
			
			// Marital Status
			/*
			expression = xpath.compile("/ADT_A05/PID/PID.16/CE.1");
			Node maritalStatusCodeNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String maritalStatusCode = "";
			if (maritalStatusCodeNode != null) {
				maritalStatusCode = maritalStatusCodeNode.getTextContent();
			}
			*/
			
			// Parents Names
			expression = xpath.compile("/ADT_A05/NK1[NK1.3/CE.1='MTH']/NK1.2/XPN.1/FN.1");
			Node motherNameNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String motherName = "";
			if (motherNameNode != null) {
				motherName = motherNameNode.getTextContent();
			}
			
			expression = xpath.compile("/ADT_A05/NK1[NK1.3/CE.1='FTH']/NK1.2/XPN.1/FN.1");
			Node fatherNameNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			String fatherName = "";
			if (fatherNameNode != null) {
				fatherName = fatherNameNode.getTextContent();
			}
			
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
			
			expression = xpath.compile("/person/province");
			provinceNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			if (provinceNode == null) {
				// Create node
				Element newElement = document.createElement("province");
				newElement.setTextContent(province);
				personNode.appendChild(newElement);
			} else {
				// update node
				provinceNode.setTextContent(province);
			}
			
			expression = xpath.compile("/person/district");
			districtNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			if (districtNode == null) {
				// Create node
				Element newElement = document.createElement("district");
				newElement.setTextContent(district);
				personNode.appendChild(newElement);
			} else {
				// update node
				districtNode.setTextContent(district);
			}
			
			expression = xpath.compile("/person/sector");
			sectorNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			if (sectorNode == null) {
				// Create node
				Element newElement = document.createElement("sector");
				newElement.setTextContent(sector);
				personNode.appendChild(newElement);
			} else {
				// update node
				sectorNode.setTextContent(sector);
			}
			
			expression = xpath.compile("/person/cell");
			cellNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			if (cellNode == null) {
				// Create node
				Element newElement = document.createElement("cell");
				newElement.setTextContent(cell);
				personNode.appendChild(newElement);
			} else {
				// update node
				cellNode.setTextContent(cell);
			}
			
			expression = xpath.compile("/person/village");
			villageNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			if (villageNode == null) {
				// Create node
				Element newElement = document.createElement("village");
				newElement.setTextContent(village);
				personNode.appendChild(newElement);
			} else {
				// update node
				villageNode.setTextContent(village);
			}
			
			// Phone number
			expression = xpath.compile("/person/phoneNumber");
			phoneNumberNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			if (phoneNumberNode == null) {
				// Create node
				Element newElement = document.createElement("phoneNumber");
				newElement.setTextContent(phoneNumber);
				personNode.appendChild(newElement);
			} else {
				// update node
				phoneNumberNode.setTextContent(phoneNumber);
			}
			
			// Marital Status
			/*
			expression = xpath.compile("/person/maritalStatusCode");
			maritalStatusCodeNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			if (maritalStatusCodeNode == null) {
				// Create node
				Element newElement = document.createElement("maritalStatusCode");
				newElement.setTextContent(maritalStatusCode);
				personNode.appendChild(newElement);
			} else {
				// update node
				maritalStatusCodeNode.setTextContent(maritalStatusCode);
			}
			*/
			
			// Parents Names
			expression = xpath.compile("/person/motherName");
			motherNameNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			if (motherNameNode == null) {
				// Create node
				Element newElement = document.createElement("motherName");
				newElement.setTextContent(motherName);
				personNode.appendChild(newElement);
			} else {
				// update node
				motherNameNode.setTextContent(motherName);
			}
			
			expression = xpath.compile("/person/fatherName");
			fatherNameNode = (Node) expression.evaluate(document, XPathConstants.NODE);
			if (fatherNameNode == null) {
				// Create node
				Element newElement = document.createElement("fatherName");
				newElement.setTextContent(fatherName);
				personNode.appendChild(newElement);
			} else {
				// update node
				fatherNameNode.setTextContent(fatherName);
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
