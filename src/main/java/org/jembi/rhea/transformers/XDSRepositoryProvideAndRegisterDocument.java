package org.jembi.rhea.transformers;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;

import org.jembi.rhea.RestfulHttpRequest;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import ca.marc.ihe.xds.XdsGuidType;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.datatype.CX;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.model.v25.segment.PV1;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;

public class XDSRepositoryProvideAndRegisterDocument extends
		AbstractMessageTransformer {

	private static SimpleDateFormat formatter_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	
	@Override
	public Object transformMessage(MuleMessage src, String encoding)
			throws TransformerException {
		
		try {
			RestfulHttpRequest request = (RestfulHttpRequest)src.getPayload();
			EncounterInfo enc = parseEncounterRequest(request.getBody());
			return buildRegisterRequest(enc);
		} catch (HL7Exception ex) {
			throw new TransformerException(this, ex);
		}
	}
	
	protected static ProvideAndRegisterDocumentSetRequestType buildRegisterRequest(EncounterInfo enc) {
		ProvideAndRegisterDocumentSetRequestType xdsRequest = new ProvideAndRegisterDocumentSetRequestType();
		SubmitObjectsRequest submissionRequest = new SubmitObjectsRequest();
		RegistryObjectListType registryObjects = new RegistryObjectListType();
		submissionRequest.setRegistryObjectList(registryObjects);
		xdsRequest.setSubmitObjectsRequest(submissionRequest);
		Date now = new Date();
		
		// For each document
		ExtrinsicObjectType document = createExtrinsicObject("text/xml", "Physical", XdsGuidType.XDSDocumentEntry);
		
		// To add slots
		document.getSlot().add(createSlot("creationTime", formatter_yyyyMMdd.format(now)));
		document.getSlot().add(createSlot("languageCode", "en-us"));
		document.getSlot().add(createSlot("serviceStartTime", enc.getEncounterDateTime()));
		document.getSlot().add(createSlot("serviceStopTime", enc.getEncounterDateTime()));
		document.getSlot().add(createSlot("sourcePatientId", enc.getPID()));
		document.getSlot().add(createSlot("sourcePatientInfo", "PID-3|" + enc.getPID(), "PID-5|" + enc.getName()));
		
		// To add classifications
		SlotType1[] authorSlots = new SlotType1[] {
			createSlot("authorPerson", enc.getAttendingDoctor()),
			createSlot("authorInstitution", enc.getLocation()),
		};
		document.getClassification().add(createClassification(document, XdsGuidType.XDSDocumentEntry_Author, null, null, authorSlots));
		
		SlotType1[] confidentialitySlots = new SlotType1[] {
			createSlot("codingScheme", "Connect-a-thon confidentialityCodes")
		};
		document.getClassification().add(createClassification(document, XdsGuidType.XDSDocumentEntry_ConfidentialityCode, "1.3.6.1.4.1.21367.2006.7.107", "Normal", confidentialitySlots));
		
		// To add external ids
		document.getExternalIdentifier().add(createExternalIdentifier(document, XdsGuidType.XDSDocumentEntry_PatientId, enc.getPID()));
		document.getExternalIdentifier().add(createExternalIdentifier(document, XdsGuidType.XDSDocumentEntry_UniqueId, String.format("urn:uuid:%s", UUID.randomUUID())));

		// Add to list of objects
		registryObjects.getIdentifiable().add(new JAXBElement<ExtrinsicObjectType>(
                new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "ExtrinsicObject"),
                ExtrinsicObjectType.class,
                document
        ));

		// Create submission set
		RegistryPackageType pkg = createRegistryPackage();
		pkg.getSlot().add(createSlot("submissionTime", formatter_yyyyMMdd.format(now)));
		// To add classifications
		SlotType1[] contentTypeSlots = new SlotType1[] {
				createSlot("condingScheme", "Connect-a-thon contentTypeCodes")
		};
		pkg.getClassification().add(createClassification(document, XdsGuidType.XDSSubmissionSet_ContentType, "History and Physical", "History and Physical", contentTypeSlots));
		
		// To add external ids
		pkg.getExternalIdentifier().add(createExternalIdentifier(document, XdsGuidType.XDSSubmissionSet_PatientId, enc.getPID()));

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
                createNodeClassification(pkg, XdsGuidType.XDSSubmissionSet)
            ));
		
		// Add association
		registryObjects.getIdentifiable().add(new JAXBElement<AssociationType1>(
                new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "Association"),
                AssociationType1.class,
                createAssociation(pkg, document, "original")
            ));
		
		// Add document
		Document content = new Document();
		content.setId(document.getId());
		content.setValue(new byte[] { 1, 2, 3, 4, 5, 6, 7 });
		xdsRequest.getDocument().add(content);

		return xdsRequest;
	}
	
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
		
		return res;
	}
	

	/**
	 * Create registry package
	 */
	private static RegistryPackageType createRegistryPackage()
	{
		RegistryPackageType retVal = new RegistryPackageType();
		retVal.setId(String.format("urn:uuid:%s", UUID.randomUUID().toString()));
		return retVal;
	}
	
	/**
	 * Create Extrinsic object
	 */
	private static ExtrinsicObjectType createExtrinsicObject(String mimeType, String name, XdsGuidType objectType)
	{

		ExtrinsicObjectType retVal = new ExtrinsicObjectType();
		retVal.setId(String.format("urn:uuid:%s", UUID.randomUUID().toString()));
		retVal.setObjectType(objectType.toString());
		retVal.setMimeType(mimeType);
		
		InternationalStringType localName = new InternationalStringType();
		LocalizedStringType stringValue = new LocalizedStringType();
		stringValue.setValue(name);
		localName.getLocalizedString().add(stringValue);
		retVal.setName(localName);
		
		return retVal;
	}
	
    /**
     * Create a query slot
     */
    private static SlotType1 createSlot(String slotName, String... value)
    {
    	SlotType1 retSlot = new SlotType1();
        ValueListType patientValueList = new ValueListType();
        patientValueList.getValue().addAll(Arrays.asList(value));
        retSlot.setName(slotName);
        retSlot.setValueList(patientValueList);
        return retSlot;
    }
    
	/**
	 * Create association type
	 */
	private static AssociationType1 createAssociation(RegistryObjectType source,
			ExtrinsicObjectType target, String status) {
		AssociationType1 retAssoc = new AssociationType1();
		retAssoc.setId(String.format("urn:uuid:%s", UUID.randomUUID().toString()));
		retAssoc.setSourceObject(source.getId());
		retAssoc.setTargetObject(target.getId());
		retAssoc.getSlot().add(createSlot("SubmissionSetStatus", status));
		return retAssoc;
	}

    /**
     * Create classification object
     */
    private static ClassificationType createClassification(RegistryObjectType classifiedObject, XdsGuidType scheme, String nodeRepresentation, String name, SlotType1... slots)
    {

    	ClassificationType retClass = new ClassificationType();
    	retClass.setId(String.format("urn:uuid:%s", UUID.randomUUID().toString()));
    	retClass.setClassificationScheme(scheme.toString());
    	retClass.setClassifiedObject(classifiedObject.getId());
    	if (nodeRepresentation!=null) retClass.setNodeRepresentation(nodeRepresentation);
    	retClass.getSlot().addAll(Arrays.asList(slots));
    	
    	if (name!=null) {
	    	InternationalStringType localName = new InternationalStringType();
			LocalizedStringType stringValue = new LocalizedStringType();
			stringValue.setValue(name);
			localName.getLocalizedString().add(stringValue);
			retClass.setName(localName);
    	}
		
    	return retClass;
    }
    
    /**
	 * Create node classification
	 * @param registryObject
	 * @param classificationNode
	 * @return
	 */
	private static ClassificationType createNodeClassification(RegistryObjectType registryObject, XdsGuidType classificationNode)
	{
		ClassificationType retClass = new ClassificationType();
		retClass.setId(String.format("urn:uuid:%s", UUID.randomUUID()));
		retClass.setClassificationNode(classificationNode.toString());
		retClass.setClassifiedObject(registryObject.getId());
		return retClass;
	}
	
    /**
     * Create external identifier
     */
    private static ExternalIdentifierType createExternalIdentifier(RegistryObjectType registryObject, XdsGuidType scheme, String value)
    {

    	ExternalIdentifierType retId = new ExternalIdentifierType();
    	retId.setId(String.format("urn:uuid:%s", UUID.randomUUID().toString()));
    	
    	retId.setRegistryObject(registryObject.getId());
    	retId.setIdentificationScheme(scheme.toString());
    	retId.setValue(value);
    	
		InternationalStringType localName = new InternationalStringType();
		LocalizedStringType stringValue = new LocalizedStringType();
		stringValue.setValue(scheme.getName());
		localName.getLocalizedString().add(stringValue);
		retId.setName(localName);
		
		return retId;
    }
    
    
    protected static class EncounterInfo {
    	protected String pid;
    	protected String firstName, lastName;
    	protected String encounterDateTime;
    	protected String attendingDoctorFirstName, attendingDoctorLastName;
    	protected String attendingDoctorID;
    	protected String location;
    	
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
    }
}
