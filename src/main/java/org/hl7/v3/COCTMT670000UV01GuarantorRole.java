
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


/**
 * <p>Java class for COCT_MT670000UV01.GuarantorRole complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="COCT_MT670000UV01.GuarantorRole">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="id" type="{urn:hl7-org:v3}II" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="addr" type="{urn:hl7-org:v3}AD" minOccurs="0"/>
 *         &lt;element name="telecom" type="{urn:hl7-org:v3}TEL" minOccurs="0"/>
 *         &lt;element name="statusCode" type="{urn:hl7-org:v3}CS" minOccurs="0"/>
 *         &lt;element name="effectiveTime" type="{urn:hl7-org:v3}IVL_TS" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="guarantorPerson" type="{urn:hl7-org:v3}COCT_MT670000UV01.GuarantorPerson" minOccurs="0"/>
 *           &lt;element name="guarantorOrganization" type="{urn:hl7-org:v3}COCT_MT670000UV01.GuarantorOrganization" minOccurs="0"/>
 *         &lt;/choice>
 *         &lt;element name="contractHeld" type="{urn:hl7-org:v3}COCT_MT670000UV01.Holder" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="classCode" type="{urn:hl7-org:v3}RoleClass" fixed="GUAR" />
 *       &lt;attribute name="negationInd" type="{urn:hl7-org:v3}bl" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT670000UV01.GuarantorRole", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "id",
    "addr",
    "telecom",
    "statusCode",
    "effectiveTime",
    "guarantorPerson",
    "guarantorOrganization",
    "contractHeld"
})
public class COCTMT670000UV01GuarantorRole {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    protected List<II> id;
    protected AD addr;
    protected TEL telecom;
    protected CS statusCode;
    protected IVLTS effectiveTime;
    @XmlElementRef(name = "guarantorPerson", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT670000UV01GuarantorPerson> guarantorPerson;
    @XmlElementRef(name = "guarantorOrganization", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT670000UV01GuarantorOrganization> guarantorOrganization;
    @XmlElement(nillable = true)
    protected List<COCTMT670000UV01Holder> contractHeld;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "classCode")
    protected List<String> classCode;
    @XmlAttribute(name = "negationInd")
    protected Boolean negationInd;

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
     * Gets the value of the addr property.
     * 
     * @return
     *     possible object is
     *     {@link AD }
     *     
     */
    public AD getAddr() {
        return addr;
    }

    /**
     * Sets the value of the addr property.
     * 
     * @param value
     *     allowed object is
     *     {@link AD }
     *     
     */
    public void setAddr(AD value) {
        this.addr = value;
    }

    /**
     * Gets the value of the telecom property.
     * 
     * @return
     *     possible object is
     *     {@link TEL }
     *     
     */
    public TEL getTelecom() {
        return telecom;
    }

    /**
     * Sets the value of the telecom property.
     * 
     * @param value
     *     allowed object is
     *     {@link TEL }
     *     
     */
    public void setTelecom(TEL value) {
        this.telecom = value;
    }

    /**
     * Gets the value of the statusCode property.
     * 
     * @return
     *     possible object is
     *     {@link CS }
     *     
     */
    public CS getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CS }
     *     
     */
    public void setStatusCode(CS value) {
        this.statusCode = value;
    }

    /**
     * Gets the value of the effectiveTime property.
     * 
     * @return
     *     possible object is
     *     {@link IVLTS }
     *     
     */
    public IVLTS getEffectiveTime() {
        return effectiveTime;
    }

    /**
     * Sets the value of the effectiveTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link IVLTS }
     *     
     */
    public void setEffectiveTime(IVLTS value) {
        this.effectiveTime = value;
    }

    /**
     * Gets the value of the guarantorPerson property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT670000UV01GuarantorPerson }{@code >}
     *     
     */
    public JAXBElement<COCTMT670000UV01GuarantorPerson> getGuarantorPerson() {
        return guarantorPerson;
    }

    /**
     * Sets the value of the guarantorPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT670000UV01GuarantorPerson }{@code >}
     *     
     */
    public void setGuarantorPerson(JAXBElement<COCTMT670000UV01GuarantorPerson> value) {
        this.guarantorPerson = value;
    }

    /**
     * Gets the value of the guarantorOrganization property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT670000UV01GuarantorOrganization }{@code >}
     *     
     */
    public JAXBElement<COCTMT670000UV01GuarantorOrganization> getGuarantorOrganization() {
        return guarantorOrganization;
    }

    /**
     * Sets the value of the guarantorOrganization property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT670000UV01GuarantorOrganization }{@code >}
     *     
     */
    public void setGuarantorOrganization(JAXBElement<COCTMT670000UV01GuarantorOrganization> value) {
        this.guarantorOrganization = value;
    }

    /**
     * Gets the value of the contractHeld property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contractHeld property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContractHeld().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT670000UV01Holder }
     * 
     * 
     */
    public List<COCTMT670000UV01Holder> getContractHeld() {
        if (contractHeld == null) {
            contractHeld = new ArrayList<COCTMT670000UV01Holder>();
        }
        return this.contractHeld;
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
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getClassCode() {
        if (classCode == null) {
            classCode = new ArrayList<String>();
        }
        return this.classCode;
    }

    /**
     * Gets the value of the negationInd property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNegationInd() {
        return negationInd;
    }

    /**
     * Sets the value of the negationInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNegationInd(Boolean value) {
        this.negationInd = value;
    }

}
