/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.orchestration;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class QueryPatientGetListOfOpenEMPIPersonIds implements Callable {
	
	Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		MuleMessage msg = eventContext.getMessage();
		
		String openempiPeopleXml = (String) msg.getPayloadAsString();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document document = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(openempiPeopleXml)));
		
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		
		XPathExpression expression = xpath.compile("/people/person/personId");
		NodeList patientIdNodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
		
		String idListStr = "";
		for (int i = 0 ; i < patientIdNodeList.getLength() ; i++) {
			Node node = patientIdNodeList.item(i);
			String id = node.getTextContent();
			if (idListStr.isEmpty()) {
				//idListStr = id;
				idListStr = "personId=" + id;
			} else {
				//idListStr += "," + id;
				idListStr += "&personId=" + id;
			}
		}
		
		msg.setOutboundProperty("idList", idListStr);
		
		return msg;
	}
	
}
