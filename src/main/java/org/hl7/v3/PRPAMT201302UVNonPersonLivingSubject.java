
package org.hl7.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for PRPA_MT201302UV.NonPersonLivingSubject complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PRPA_MT201302UV.NonPersonLivingSubject">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="id" type="{urn:hl7-org:v3}II" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="code" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="name" type="{urn:hl7-org:v3}EN" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="desc" type="{urn:hl7-org:v3}ED" minOccurs="0"/>
 *         &lt;element name="existenceTime" type="{urn:hl7-org:v3}IVL_TS" minOccurs="0"/>
 *         &lt;element name="telecom" type="{urn:hl7-org:v3}TEL" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="riskCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="handlingCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="administrativeGenderCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="birthTime" type="{urn:hl7-org:v3}TS" minOccurs="0"/>
 *         &lt;element name="deceasedInd" type="{urn:hl7-org:v3}BL" minOccurs="0"/>
 *         &lt;element name="deceasedTime" type="{urn:hl7-org:v3}TS" minOccurs="0"/>
 *         &lt;element name="multipleBirthInd" type="{urn:hl7-org:v3}BL" minOccurs="0"/>
 *         &lt;element name="multipleBirthOrderNumber" type="{urn:hl7-org:v3}INT" minOccurs="0"/>
 *         &lt;element name="organDonorInd" type="{urn:hl7-org:v3}BL" minOccurs="0"/>
 *         &lt;element name="strainText" type="{urn:hl7-org:v3}ED" minOccurs="0"/>
 *         &lt;element name="genderStatusCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="asPatientOfOtherProvider" type="{urn:hl7-org:v3}PRPA_MT201302UV.PatientOfOtherProvider" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="asMember" type="{urn:hl7-org:v3}PRPA_MT201302UV.Member" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="asOtherIDs" type="{urn:hl7-org:v3}PRPA_MT201302UV.OtherIDs" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="contactParty" type="{urn:hl7-org:v3}PRPA_MT201302UV.ContactParty" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="guardian" type="{urn:hl7-org:v3}PRPA_MT201302UV.Guardian" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="personalRelationship" type="{urn:hl7-org:v3}PRPA_MT201302UV.PersonalRelationship" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="careGiver" type="{urn:hl7-org:v3}PRPA_MT201302UV.CareGiver" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="birthPlace" type="{urn:hl7-org:v3}PRPA_MT201302UV.BirthPlace" minOccurs="0"/>
 *         &lt;element name="guarantorRole" type="{urn:hl7-org:v3}COCT_MT670000UV01.GuarantorRole" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="classCode" use="required" type="{urn:hl7-org:v3}cs" />
 *       &lt;attribute name="determinerCode" type="{urn:hl7-org:v3}cs" fixed="INSTANCE" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PRPA_MT201302UV.NonPersonLivingSubject", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "id",
    "code",
    "name",
    "desc",
    "existenceTime",
    "telecom",
    "riskCode",
    "handlingCode",
    "administrativeGenderCode",
    "birthTime",
    "deceasedInd",
    "deceasedTime",
    "multipleBirthInd",
    "multipleBirthOrderNumber",
    "organDonorInd",
    "strainText",
    "genderStatusCode",
    "asPatientOfOtherProvider",
    "asMember",
    "asOtherIDs",
    "contactParty",
    "guardian",
    "personalRelationship",
    "careGiver",
    "birthPlace",
    "guarantorRole"
})
public class PRPAMT201302UVNonPersonLivingSubject {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    protected List<II> id;
    protected CE code;
    protected List<EN> name;
    protected ED desc;
    protected IVLTS existenceTime;
    protected List<TEL> telecom;
    protected List<CE> riskCode;
    protected List<CE> handlingCode;
    protected CE administrativeGenderCode;
    protected TS birthTime;
    protected BL deceasedInd;
    protected TS deceasedTime;
    protected BL multipleBirthInd;
    protected INT multipleBirthOrderNumber;
    protected BL organDonorInd;
    protected ED strainText;
    protected CE genderStatusCode;
    @XmlElement(nillable = true)
    protected List<PRPAMT201302UVPatientOfOtherProvider> asPatientOfOtherProvider;
    @XmlElement(nillable = true)
    protected List<PRPAMT201302UVMember> asMember;
    @XmlElement(nillable = true)
    protected List<PRPAMT201302UVOtherIDs> asOtherIDs;
    @XmlElement(nillable = true)
    protected List<PRPAMT201302UVContactParty> contactParty;
    @XmlElement(nillable = true)
    protected List<PRPAMT201302UVGuardian> guardian;
    @XmlElement(nillable = true)
    protected List<PRPAMT201302UVPersonalRelationship> personalRelationship;
    @XmlElement(nillable = true)
    protected List<PRPAMT201302UVCareGiver> careGiver;
    @XmlElementRef(name = "birthPlace", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<PRPAMT201302UVBirthPlace> birthPlace;
    @XmlElement(nillable = true)
    protected List<COCTMT670000UV01GuarantorRole> guarantorRole;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "classCode", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String classCode;
    @XmlAttribute(name = "determinerCode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String determinerCode;

    /**
     * Gets the value of the realmCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the realmCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRealmCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CS }
     * 
     * 
     */
    public List<CS> getRealmCode() {
        if (realmCode == null) {
            realmCode = new ArrayList<CS>();
        }
        return this.realmCode;
    }

    /**
     * Gets the value of the typeId property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getTypeId() {
        return typeId;
    }

    /**
     * Sets the value of the typeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setTypeId(II value) {
        this.typeId = value;
    }

    /**
     * Gets the value of the templateId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the templateId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTemplateId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link II }
     * 
     * 
     */
    public List<II> getTemplateId() {
        if (templateId == null) {
            templateId = new ArrayList<II>();
        }
        return this.templateId;
    }

    /**
     * Gets the value of the id property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the id property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link II }
     * 
     * 
     */
    public List<II> getId() {
        if (id == null) {
            id = new ArrayList<II>();
        }
        return this.id;
    }

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setCode(CE value) {
        this.code = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the name property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EN }
     * 
     * 
     */
    public List<EN> getName() {
        if (name == null) {
            name = new ArrayList<EN>();
        }
        return this.name;
    }

    /**
     * Gets the value of the desc property.
     * 
     * @return
     *     possible object is
     *     {@link ED }
     *     
     */
    public ED getDesc() {
        return desc;
    }

    /**
     * Sets the value of the desc property.
     * 
     * @param value
     *     allowed object is
     *     {@link ED }
     *     
     */
    public void setDesc(ED value) {
        this.desc = value;
    }

    /**
     * Gets the value of the existenceTime property.
     * 
     * @return
     *     possible object is
     *     {@link IVLTS }
     *     
     */
    public IVLTS getExistenceTime() {
        return existenceTime;
    }

    /**
     * Sets the value of the existenceTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link IVLTS }
     *     
     */
    public void setExistenceTime(IVLTS value) {
        this.existenceTime = value;
    }

    /**
     * Gets the value of the telecom property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the telecom property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTelecom().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TEL }
     * 
     * 
     */
    public List<TEL> getTelecom() {
        if (telecom == null) {
            telecom = new ArrayList<TEL>();
        }
        return this.telecom;
    }

    /**
     * Gets the value of the riskCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the riskCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRiskCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CE }
     * 
     * 
     */
    public List<CE> getRiskCode() {
        if (riskCode == null) {
            riskCode = new ArrayList<CE>();
        }
        return this.riskCode;
    }

    /**
     * Gets the value of the handlingCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the handlingCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHandlingCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CE }
     * 
     * 
     */
    public List<CE> getHandlingCode() {
        if (handlingCode == null) {
            handlingCode = new ArrayList<CE>();
        }
        return this.handlingCode;
    }

    /**
     * Gets the value of the administrativeGenderCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getAdministrativeGenderCode() {
        return administrativeGenderCode;
    }

    /**
     * Sets the value of the administrativeGenderCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setAdministrativeGenderCode(CE value) {
        this.administrativeGenderCode = value;
    }

    /**
     * Gets the value of the birthTime property.
     * 
     * @return
     *     possible object is
     *     {@link TS }
     *     
     */
    public TS getBirthTime() {
        return birthTime;
    }

    /**
     * Sets the value of the birthTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link TS }
     *     
     */
    public void setBirthTime(TS value) {
        this.birthTime = value;
    }

    /**
     * Gets the value of the deceasedInd property.
     * 
     * @return
     *     possible object is
     *     {@link BL }
     *     
     */
    public BL getDeceasedInd() {
        return deceasedInd;
    }

    /**
     * Sets the value of the deceasedInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BL }
     *     
     */
    public void setDeceasedInd(BL value) {
        this.deceasedInd = value;
    }

    /**
     * Gets the value of the deceasedTime property.
     * 
     * @return
     *     possible object is
     *     {@link TS }
     *     
     */
    public TS getDeceasedTime() {
        return deceasedTime;
    }

    /**
     * Sets the value of the deceasedTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link TS }
     *     
     */
    public void setDeceasedTime(TS value) {
        this.deceasedTime = value;
    }

    /**
     * Gets the value of the multipleBirthInd property.
     * 
     * @return
     *     possible object is
     *     {@link BL }
     *     
     */
    public BL getMultipleBirthInd() {
        return multipleBirthInd;
    }

    /**
     * Sets the value of the multipleBirthInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BL }
     *     
     */
    public void setMultipleBirthInd(BL value) {
        this.multipleBirthInd = value;
    }

    /**
     * Gets the value of the multipleBirthOrderNumber property.
     * 
     * @return
     *     possible object is
     *     {@link INT }
     *     
     */
    public INT getMultipleBirthOrderNumber() {
        return multipleBirthOrderNumber;
    }

    /**
     * Sets the value of the multipleBirthOrderNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link INT }
     *     
     */
    public void setMultipleBirthOrderNumber(INT value) {
        this.multipleBirthOrderNumber = value;
    }

    /**
     * Gets the value of the organDonorInd property.
     * 
     * @return
     *     possible object is
     *     {@link BL }
     *     
     */
    public BL getOrganDonorInd() {
        return organDonorInd;
    }

    /**
     * Sets the value of the organDonorInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BL }
     *     
     */
    public void setOrganDonorInd(BL value) {
        this.organDonorInd = value;
    }

    /**
     * Gets the value of the strainText property.
     * 
     * @return
     *     possible object is
     *     {@link ED }
     *     
     */
    public ED getStrainText() {
        return strainText;
    }

    /**
     * Sets the value of the strainText property.
     * 
     * @param value
     *     allowed object is
     *     {@link ED }
     *     
     */
    public void setStrainText(ED value) {
        this.strainText = value;
    }

    /**
     * Gets the value of the genderStatusCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getGenderStatusCode() {
        return genderStatusCode;
    }

    /**
     * Sets the value of the genderStatusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setGenderStatusCode(CE value) {
        this.genderStatusCode = value;
    }

    /**
     * Gets the value of the asPatientOfOtherProvider property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the asPatientOfOtherProvider property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAsPatientOfOtherProvider().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PRPAMT201302UVPatientOfOtherProvider }
     * 
     * 
     */
    public List<PRPAMT201302UVPatientOfOtherProvider> getAsPatientOfOtherProvider() {
        if (asPatientOfOtherProvider == null) {
            asPatientOfOtherProvider = new ArrayList<PRPAMT201302UVPatientOfOtherProvider>();
        }
        return this.asPatientOfOtherProvider;
    }

    /**
     * Gets the value of the asMember property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the asMember property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAsMember().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PRPAMT201302UVMember }
     * 
     * 
     */
    public List<PRPAMT201302UVMember> getAsMember() {
        if (asMember == null) {
            asMember = new ArrayList<PRPAMT201302UVMember>();
        }
        return this.asMember;
    }

    /**
     * Gets the value of the asOtherIDs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the asOtherIDs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAsOtherIDs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PRPAMT201302UVOtherIDs }
     * 
     * 
     */
    public List<PRPAMT201302UVOtherIDs> getAsOtherIDs() {
        if (asOtherIDs == null) {
            asOtherIDs = new ArrayList<PRPAMT201302UVOtherIDs>();
        }
        return this.asOtherIDs;
    }

    /**
     * Gets the value of the contactParty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contactParty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContactParty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PRPAMT201302UVContactParty }
     * 
     * 
     */
    public List<PRPAMT201302UVContactParty> getContactParty() {
        if (contactParty == null) {
            contactParty = new ArrayList<PRPAMT201302UVContactParty>();
        }
        return this.contactParty;
    }

    /**
     * Gets the value of the guardian property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the guardian property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGuardian().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PRPAMT201302UVGuardian }
     * 
     * 
     */
    public List<PRPAMT201302UVGuardian> getGuardian() {
        if (guardian == null) {
            guardian = new ArrayList<PRPAMT201302UVGuardian>();
        }
        return this.guardian;
    }

    /**
     * Gets the value of the personalRelationship property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the personalRelationship property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPersonalRelationship().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PRPAMT201302UVPersonalRelationship }
     * 
     * 
     */
    public List<PRPAMT201302UVPersonalRelationship> getPersonalRelationship() {
        if (personalRelationship == null) {
            personalRelationship = new ArrayList<PRPAMT201302UVPersonalRelationship>();
        }
        return this.personalRelationship;
    }

    /**
     * Gets the value of the careGiver property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the careGiver property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCareGiver().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PRPAMT201302UVCareGiver }
     * 
     * 
     */
    public List<PRPAMT201302UVCareGiver> getCareGiver() {
        if (careGiver == null) {
            careGiver = new ArrayList<PRPAMT201302UVCareGiver>();
        }
        return this.careGiver;
    }

    /**
     * Gets the value of the birthPlace property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PRPAMT201302UVBirthPlace }{@code >}
     *     
     */
    public JAXBElement<PRPAMT201302UVBirthPlace> getBirthPlace() {
        return birthPlace;
    }

    /**
     * Sets the value of the birthPlace property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PRPAMT201302UVBirthPlace }{@code >}
     *     
     */
    public void setBirthPlace(JAXBElement<PRPAMT201302UVBirthPlace> value) {
        this.birthPlace = value;
    }

    /**
     * Gets the value of the guarantorRole property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the guarantorRole property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGuarantorRole().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT670000UV01GuarantorRole }
     * 
     * 
     */
    public List<COCTMT670000UV01GuarantorRole> getGuarantorRole() {
        if (guarantorRole == null) {
            guarantorRole = new ArrayList<COCTMT670000UV01GuarantorRole>();
        }
        return this.guarantorRole;
    }

    /**
     * Gets the value of the nullFlavor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nullFlavor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNullFlavor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNullFlavor() {
        if (nullFlavor == null) {
            nullFlavor = new ArrayList<String>();
        }
        return this.nullFlavor;
    }

    /**
     * Gets the value of the classCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassCode() {
        return classCode;
    }

    /**
     * Sets the value of the classCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassCode(String value) {
        this.classCode = value;
    }

    /**
     * Gets the value of the determinerCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeterminerCode() {
        if (determinerCode == null) {
            return "INSTANCE";
        } else {
            return determinerCode;
        }
    }

    /**
     * Sets the value of the determinerCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeterminerCode(String value) {
        this.determinerCode = value;
    }

}
