/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class Util {
	
	//creating a new JAXB context is expensive, so keep static instances
	private static Map<String, JAXBContext> JAXBContextInstances = new HashMap<String, JAXBContext>();
	
	/**
	 * Split an id string into the id type and the id number
	 * @param id_str the id string to split
	 * @return an array with the first value being the id type and the second being the id number
	 */
	public static String[] splitIdentifer(String id_str) {
		int index = id_str.indexOf('-');
		String idType = id_str.substring(0, index);
		String id = id_str.substring(index + 1);
		String[] ret = new String[2];
		ret[0] = idType;
		ret[1] = id;
		return ret;
	}

	/**
	 * Marshall a JAXB object and return the XML as a string. The XML declaration will be added.
	 */
	public static String marshallJAXBObject(String namespace, Object o) throws JAXBException {
		return marshallJAXBObject(namespace, o, true);
	}
	
	/**
	 * Marshall a JAXB object and return the XML as a string
	 */
	public static String marshallJAXBObject(String namespace, Object o, boolean addXMLDeclaration) throws JAXBException {
		JAXBContext jc = getJAXBContext(namespace);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", addXMLDeclaration);
		StringWriter sw = new StringWriter();
		marshaller.marshal(o, sw);
		return sw.toString();
	}
	
	public static JAXBContext getJAXBContext(String namespace) throws JAXBException {
		if (!JAXBContextInstances.containsKey(namespace))
			JAXBContextInstances.put(namespace, JAXBContext.newInstance(namespace));
		return JAXBContextInstances.get(namespace);
	}
}
