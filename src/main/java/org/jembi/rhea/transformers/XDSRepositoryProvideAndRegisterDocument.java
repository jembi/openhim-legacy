/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.jembi.rhea.transformers;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;

import org.jembi.rhea.RestfulHttpRequest;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import ca.marc.ihe.xds.XDSUtil;
import ca.marc.ihe.xds.XdsGuidType;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.datatype.CX;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.model.v25.segment.PV1;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;

/**
 * XDS ITI-41 Provide and Register Document Set-b
 */
public class XDSRepositoryProvideAndRegisterDocument extends
		AbstractMessageTransformer {

	private static SimpleDateFormat formatter_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	
	private String _uniqueId;
	
	/* Transform ORU_R01 and generate register request */
	
	@Override
	public Object transformMessage(MuleMessage message, String encoding)
			throws TransformerException {
		
		try {
			RestfulHttpRequest request = (RestfulHttpRequest)message.getPayload();
			EncounterInfo enc = parseEncounterRequest(request.getBody());
			ProvideAndRegisterDocumentSetRequestType prRequest = buildRegisterRequest(enc);
			
			// add request to session prop so that we can access it when processing the response
			message.setSessionProperty("XDS-ITI-41", marshall(prRequest));
			message.setSessionProperty("XDS-ITI-41_uniqueId", _uniqueId);
			message.setSessionProperty("XDS-ITI-41_patientId", enc.getPID());
			
			return prRequest;
		} catch (HL7Exception ex) {
			throw new TransformerException(this, ex);
		} catch (JAXBException ex) {
			throw new TransformerException(this, ex);
		}
	}
	
	/* 
	 * Please note that the following method (buildRegisterRequest) contains modified code
	 * originally written by Mohawk College and released under the Apache 2.0 license
	 * (http://www.apache.org/licenses/LICENSE-2.0)
	 */
	
	protected ProvideAndRegisterDocumentSetRequestType buildRegisterRequest(EncounterInfo enc) {
		ProvideAndRegisterDocumentSetRequestType xdsRequest = new ProvideAndRegisterDocumentSetRequestType();
		SubmitObjectsRequest submissionRequest = new SubmitObjectsRequest();
		RegistryObjectListType registryObjects = new RegistryObjectListType();
		submissionRequest.setRegistryObjectList(registryObjects);
		xdsRequest.setSubmitObjectsRequest(submissionRequest);
		Date now = new Date();
		
		// For each document
		ExtrinsicObjectType document = XDSUtil.createExtrinsicObject("text/xml", "Physical", XdsGuidType.XDSDocumentEntry);
		
		// To add slots
		document.getSlot().add(XDSUtil.createSlot("creationTime", formatter_yyyyMMdd.format(now)));
		document.getSlot().add(XDSUtil.createSlot("languageCode", "en-us"));
		document.getSlot().add(XDSUtil.createSlot("serviceStartTime", enc.getEncounterDateTime()));
		document.getSlot().add(XDSUtil.createSlot("serviceStopTime", enc.getEncounterDateTime()));
		document.getSlot().add(XDSUtil.createSlot("sourcePatientId", enc.getPID()));
		document.getSlot().add(XDSUtil.createSlot("sourcePatientInfo", "PID-3|" + enc.getPID(), "PID-5|" + enc.getName()));
		
		// To add classifications
		SlotType1[] authorSlots = new SlotType1[] {
			XDSUtil.createSlot("authorPerson", enc.getAttendingDoctor()),
			XDSUtil.createSlot("authorInstitution", enc.getLocation()),
		};
		document.getClassification().add(XDSUtil.createClassification(document, XdsGuidType.XDSDocumentEntry_Author, null, null, authorSlots));
		
		//TODO the class code needs to be set correctly according to the document type
		SlotType1[] classCodeSlots = new SlotType1[] {
			XDSUtil.createSlot("codingScheme", "1.3.6.1.4.1.19376.1.5.3.1.1.10")
		};
		document.getClassification().add(XDSUtil.createClassification(document, XdsGuidType.XDSDocumentEntry_ClassCode, "", enc.getEncounterType(), classCodeSlots));
		SlotType1[] confidentialitySlots = new SlotType1[] {
			XDSUtil.createSlot("codingScheme", "Connect-a-thon confidentialityCodes")
		};
		document.getClassification().add(XDSUtil.createClassification(document, XdsGuidType.XDSDocumentEntry_ConfidentialityCode, "1.3.6.1.4.1.21367.2006.7.107", "Normal", confidentialitySlots));
		
		// To add external ids
		document.getExternalIdentifier().add(XDSUtil.createExternalIdentifier(document, XdsGuidType.XDSDocumentEntry_PatientId, enc.getPID()));
		_uniqueId = String.format("urn:uuid:%s", UUID.randomUUID());
		document.getExternalIdentifier().add(XDSUtil.createExternalIdentifier(document, XdsGuidType.XDSDocumentEntry_UniqueId, _uniqueId));

		// Add to list of objects
		registryObjects.getIdentifiable().add(new JAXBElement<ExtrinsicObjectType>(
                new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "ExtrinsicObject"),
                ExtrinsicObjectType.class,
                document
        ));

		// Create submission set
		RegistryPackageType pkg = XDSUtil.createRegistryPackage();
		pkg.getSlot().add(XDSUtil.createSlot("submissionTime", formatter_yyyyMMdd.format(now)));
		// To add classifications
		SlotType1[] contentTypeSlots = new SlotType1[] {
				XDSUtil.createSlot("condingScheme", "Connect-a-thon contentTypeCodes")
		};
		pkg.getClassification().add(XDSUtil.createClassification(document, XdsGuidType.XDSSubmissionSet_ContentType, "History and Physical", "History and Physical", contentTypeSlots));
		
		// To add external ids
		pkg.getExternalIdentifier().add(XDSUtil.createExternalIdentifier(document, XdsGuidType.XDSSubmissionSet_PatientId, enc.getPID()));

		// Add package to submission
		registryObjects.getIdentifiable().add(new JAXBElement<RegistryPackageType>(
                new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "RegistryPackage"),
                RegistryPackageType.class,
                pkg
        ));

		// Add classification for state
		registryObjects.getIdentifiable().add(new JAXBElement<ClassificationType>(
                new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "Classification"),
                ClassificationType.class,
                XDSUtil.createNodeClassification(pkg, XdsGuidType.XDSSubmissionSet)
            ));
		
		// Add association
		registryObjects.getIdentifiable().add(new JAXBElement<AssociationType1>(
                new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "Association"),
                AssociationType1.class,
                XDSUtil.createAssociation(pkg, document, "original")
            ));
		
		// Add document
		Document content = new Document();
		content.setId(document.getId());
		content.setValue(new byte[] { 1, 2, 3, 4, 5, 6, 7 });
		xdsRequest.getDocument().add(content);

		return xdsRequest;
	}
	
	/* */
	
    
    /* Parse ORU_R01 */
    
	protected static EncounterInfo parseEncounterRequest(String oru_r01) throws HL7Exception {
		EncounterInfo res = new EncounterInfo();
		Parser parser = new GenericParser();
		ORU_R01 msg = (ORU_R01)parser.parse(oru_r01);
		
		PID pid = msg.getPATIENT_RESULT().getPATIENT().getPID();
		for (CX cx : pid.getPatientIdentifierList()) {
			if ("ECID".equalsIgnoreCase(cx.getIdentifierTypeCode().getValue()))
				res.pid = cx.getIDNumber().getValue();
		}
		if (pid.getPatientNameReps()>0) {
			res.lastName = pid.getPatientName(0).getXpn1_FamilyName().getFn1_Surname().getValue();
			res.firstName = pid.getPatientName(0).getXpn2_GivenName().getValue();
		}
		
		PV1 pv1 = msg.getPATIENT_RESULT().getPATIENT().getVISIT().getPV1();
		if (pv1.getAttendingDoctorReps()>0) {
			res.attendingDoctorID = pv1.getAttendingDoctor(0).getIDNumber().getValue();
			res.attendingDoctorFirstName = pv1.getAttendingDoctor(0).getGivenName().getValue();
			res.attendingDoctorLastName = pv1.getAttendingDoctor(0).getFamilyName().getFn1_Surname().getValue();
		}
		res.location = pv1.getPv13_AssignedPatientLocation().getPl4_Facility().getHd1_NamespaceID().getValue();
		res.encounterDateTime = pv1.getPv144_AdmitDateTime().getTime().getValue();
		res.encounterType = pv1.getPv14_AdmissionType().getValue();
		
		return res;
	}
	
    
    protected static class EncounterInfo {
    	protected String pid;
    	protected String firstName, lastName;
    	protected String encounterDateTime;
    	protected String attendingDoctorFirstName, attendingDoctorLastName;
    	protected String attendingDoctorID;
    	protected String location;
    	protected String encounterType;
    	
	    public String getPID() {
	    	return pid + "^^^&ECID&ISO";
	    }
	    
	    public String getName() {
	    	return lastName + "^" + firstName + "^^^";
	    }
	    
	    public String getEncounterDateTime() {
	    	return encounterDateTime;
	    }
	    
	    public String getAttendingDoctor() {
	    	return String.format("%s %s (%s)", attendingDoctorFirstName, attendingDoctorLastName, attendingDoctorID);
	    }
	    
	    public String getLocation() {
	    	return location;
	    }
	    
	    public String getEncounterType() {
	    	return encounterType;
	    }
    }
    
    /* */
    
	private static String marshall(ProvideAndRegisterDocumentSetRequestType prRequest) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance("ihe.iti.xds_b._2007");
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		StringWriter sw = new StringWriter();
		marshaller.marshal(prRequest, sw);
		return sw.toString();
	}
}
