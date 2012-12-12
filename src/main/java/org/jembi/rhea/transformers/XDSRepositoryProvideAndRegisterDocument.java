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

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jembi.ihe.xds.XDSAffinityDomain;
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

	private Log log = LogFactory.getLog(this.getClass());
	
	private static SimpleDateFormat formatter_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	
	private String _uniqueId;
	
	/* Transform ORU_R01 and generate register request */
	
	@Override
	public Object transformMessage(MuleMessage message, String encoding)
			throws TransformerException {
		
		try {
			String request = (String)message.getPayload();
			EncounterInfo enc = parseEncounterRequest(request, XDSAffinityDomain.IHE_CONNECTATHON_NA2013_RHEAHIE);
			ProvideAndRegisterDocumentSetRequestType prRequest = buildRegisterRequest(
				request, enc, XDSAffinityDomain.IHE_CONNECTATHON_NA2013_RHEAHIE
			);
			
			// add request to session prop so that we can access it when processing the response
			message.setSessionProperty("XDS-ITI-41", marshall(prRequest));
			message.setSessionProperty("XDS-ITI-41_uniqueId", _uniqueId);
			message.setSessionProperty("XDS-ITI-41_patientId", enc.getPID());
			
			log.info("Generated XDS Provide and Register Document Set.b request");
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
	
	protected ProvideAndRegisterDocumentSetRequestType buildRegisterRequest(String oru_r01_request, EncounterInfo enc, XDSAffinityDomain domain) {
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
		if (domain.getRepositoryUniqueId()!=null)
			document.getSlot().add(XDSUtil.createSlot("repositoryUniqueId", domain.getRepositoryUniqueId()));
		if (domain.getHomeCommunityId()!=null)
			document.getSlot().add(XDSUtil.createSlot("homeCommunityId", domain.getHomeCommunityId()));
		
		// To add classifications
		SlotType1[] authorSlots = new SlotType1[] {
			XDSUtil.createSlot("authorPerson", enc.getAttendingDoctor()),
			XDSUtil.createSlot("authorInstitution", enc.getLocation()),
		};
		document.getClassification().add(XDSUtil.createClassification(document, XdsGuidType.XDSDocumentEntry_Author, null, null, authorSlots));
		
		SlotType1[] classCodeSlots = new SlotType1[] {
			XDSUtil.createSlot("codingScheme", domain.getClassCode().getCodingScheme())
		};
		document.getClassification().add(XDSUtil.createClassification(document, XdsGuidType.XDSDocumentEntry_ClassCode, domain.getClassCode().getCode(), domain.getClassCode().getDisplay(), classCodeSlots));
		
		SlotType1[] confidentialitySlots = new SlotType1[] {
			XDSUtil.createSlot("codingScheme", domain.getConfidentialityCode().getCodingScheme())
		};
		document.getClassification().add(XDSUtil.createClassification(document, XdsGuidType.XDSDocumentEntry_ConfidentialityCode, domain.getConfidentialityCode().getCode(), domain.getConfidentialityCode().getDisplay(), confidentialitySlots));
		
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
		
		//TODO eventCodeList

		// Create submission set
		RegistryPackageType pkg = XDSUtil.createRegistryPackage();
		pkg.getSlot().add(XDSUtil.createSlot("submissionTime", formatter_yyyyMMdd.format(now)));
		
		// To add classifications
		SlotType1[] contentTypeSlots = new SlotType1[] {
				XDSUtil.createSlot("codingScheme", domain.getContentTypeCode().getCodingScheme())
		};
		pkg.getClassification().add(XDSUtil.createClassification(document, XdsGuidType.XDSSubmissionSet_ContentType, domain.getContentTypeCode().getCode(), domain.getContentTypeCode().getDisplay(), contentTypeSlots));
		
		SlotType1[] formatSlots = new SlotType1[] {
			XDSUtil.createSlot("codingScheme", domain.getFormatCode().getCodingScheme())
		};
		document.getClassification().add(XDSUtil.createClassification(document, XdsGuidType.XDSDocumentEntry_FormatCode, domain.getFormatCode().getCode(), domain.getFormatCode().getDisplay(), formatSlots));
		
		SlotType1[] healthcareFacilityTypeSlots = new SlotType1[] {
			XDSUtil.createSlot("codingScheme", domain.getHealthcareFacilityTypeCode().getCodingScheme())
		};
		document.getClassification().add(XDSUtil.createClassification(document, XdsGuidType.XDSDocumentEntry_HealthcareFacilityCode, domain.getHealthcareFacilityTypeCode().getCode(), domain.getHealthcareFacilityTypeCode().getDisplay(), healthcareFacilityTypeSlots));
		
		SlotType1[] practiceSettingSlots = new SlotType1[] {
			XDSUtil.createSlot("codingScheme", domain.getPracticeSettingCode().getCodingScheme())
		};
		document.getClassification().add(XDSUtil.createClassification(document, XdsGuidType.XDSDocumentEntry_PracticeSettingCode, domain.getPracticeSettingCode().getCode(), domain.getPracticeSettingCode().getDisplay(), practiceSettingSlots));
		
		SlotType1[] typeSlots = new SlotType1[] {
			XDSUtil.createSlot("codingScheme", domain.getTypeCode().getCodingScheme())
		};
		document.getClassification().add(XDSUtil.createClassification(document, XdsGuidType.XDSDocumentEntry_TypeCode, domain.getTypeCode().getCode(), domain.getTypeCode().getDisplay(), typeSlots));
		
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
		content.setValue(new DataHandler(new ByteArrayDataSource(oru_r01_request.getBytes(), "application/octet-stream")));
		xdsRequest.getDocument().add(content);

		return xdsRequest;
	}
	
	/* */
	
    
    /* Parse ORU_R01 */
    
	protected static EncounterInfo parseEncounterRequest(String oru_r01, XDSAffinityDomain domain) throws HL7Exception {
		EncounterInfo res = new EncounterInfo(domain);
		Parser parser = new GenericParser();
		ORU_R01 msg = (ORU_R01)parser.parse(oru_r01);
		
		PID pid = msg.getPATIENT_RESULT().getPATIENT().getPID();
		for (CX cx : pid.getPatientIdentifierList()) {
			if (domain.getAffinityDomainIDType().equalsIgnoreCase(cx.getIdentifierTypeCode().getValue()))
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
    	protected XDSAffinityDomain domain;
    	protected String pid;
    	protected String firstName, lastName;
    	protected String encounterDateTime;
    	protected String attendingDoctorFirstName, attendingDoctorLastName;
    	protected String attendingDoctorID;
    	protected String location;
    	protected String encounterType;
    	
    	
	    EncounterInfo(XDSAffinityDomain domain) {
			this.domain = domain;
		}

		public String getPID() {
	    	return pid + "^^^&" + domain.getAffinityDomainIDType() +"&ISO";
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
