<?xml version="1.0" encoding="UTF-8"?>
<!-- This Source Code Form is subject to the terms of the Mozilla Public
   - License, v. 2.0. If a copy of the MPL was not distributed with this
   - file, You can obtain one at http://mozilla.org/MPL/2.0/. -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0" xmlns:hl7="urn:hl7-org:v2xml">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
    
    <xsl:template match="/">
        <xsl:element name="person">
            <xsl:element name="dateOfBirth">
                <xsl:call-template name="FormatDate">
                    <xsl:with-param name="date" select="hl7:ADT_A05/hl7:PID/hl7:PID.7/hl7:TS.1"></xsl:with-param>
                </xsl:call-template>
            </xsl:element>
            
            <xsl:element name="givenName">
                <xsl:value-of select="hl7:ADT_A05/hl7:PID/hl7:PID.5/hl7:XPN.2"/>
            </xsl:element>
            <xsl:element name="familyName">
                <xsl:value-of select="hl7:ADT_A05/hl7:PID/hl7:PID.5/hl7:XPN.1/hl7:FN.1"/>
            </xsl:element>
            
            <xsl:element name="gender">
                <xsl:element name="genderCode">
                    <xsl:value-of select="hl7:ADT_A05/hl7:PID/hl7:PID.8"></xsl:value-of>
                </xsl:element>
            </xsl:element>
            
            <xsl:element name="country">
                <xsl:value-of select="hl7:ADT_A05/hl7:PID/hl7:PID.11/hl7:XAD.6"></xsl:value-of>
            </xsl:element>
            <xsl:element name="province">
                <xsl:value-of select="hl7:ADT_A05/hl7:PID/hl7:PID.11/hl7:XAD.4"></xsl:value-of>
            </xsl:element>
            <xsl:element name="district">
                <xsl:value-of select="hl7:ADT_A05/hl7:PID/hl7:PID.11/hl7:XAD.3"></xsl:value-of>
            </xsl:element>
            <xsl:element name="sector">
                <xsl:value-of select="hl7:ADT_A05/hl7:PID/hl7:PID.11/hl7:XAD.9"></xsl:value-of>
            </xsl:element>
            <xsl:element name="cell">
                <xsl:value-of select="hl7:ADT_A05/hl7:PID/hl7:PID.11/hl7:XAD.8"></xsl:value-of>
            </xsl:element>
            <xsl:element name="village">
                <xsl:value-of select="hl7:ADT_A05/hl7:PID/hl7:PID.11/hl7:XAD.10"></xsl:value-of>
            </xsl:element>
            
            <xsl:apply-templates select="hl7:ADT_A05/hl7:PID/hl7:PID.3"></xsl:apply-templates>
            
            <xsl:element name="motherName">
                <xsl:value-of select="hl7:ADT_A05/hl7:NK1[hl7:NK1.3/hl7:CE.1='MTH']/hl7:NK1.2/hl7:XPN.1/hl7:FN.1"></xsl:value-of>
            </xsl:element>
            <xsl:element name="fatherName">
                <xsl:value-of select="hl7:ADT_A05/hl7:NK1[hl7:NK1.3/hl7:CE.1='FTH']/hl7:NK1.2/hl7:XPN.1/hl7:FN.1"></xsl:value-of>
            </xsl:element>
            
            <!-- 
            <xsl:element name="maritalStatusCode">
                <xsl:value-of select="hl7:ADT_A05/hl7:PID/hl7:PID.16/hl7:CE.1"></xsl:value-of>
            </xsl:element>
            -->
            
            <xsl:element name="phoneNumber">
                <xsl:value-of select="hl7:ADT_A05/hl7:PID/hl7:PID.13/hl7:XTN.1"></xsl:value-of>
            </xsl:element>
            
        </xsl:element>
        
    </xsl:template>
    
    <xsl:template name="FormatDate">
        <xsl:param name="date"></xsl:param>
        <xsl:variable name="yy">
            <xsl:value-of select="substring($date,1,4)" />
        </xsl:variable>
        <xsl:variable name="mm">
            <xsl:value-of select="substring($date,5,2)" />
        </xsl:variable>
        <xsl:variable name="dd">
            <xsl:value-of select="substring($date,7,2)" />
        </xsl:variable>
        <xsl:value-of select="$yy"></xsl:value-of>
        <xsl:value-of select="'-'"></xsl:value-of>
        <xsl:value-of select="$mm"></xsl:value-of>
        <xsl:value-of select="'-'"></xsl:value-of>
        <xsl:value-of select="$dd"></xsl:value-of>
        <xsl:value-of select="'T00:00:00Z'"></xsl:value-of>
    </xsl:template>
    
    <xsl:template match="hl7:ADT_A05/hl7:PID/hl7:PID.3">
        <xsl:element name="personIdentifiers">
            <xsl:element name="identifier">
                <xsl:value-of select="hl7:CX.1"></xsl:value-of>
            </xsl:element>
            <xsl:element name="identifierDomain">
                <xsl:element name="namespaceIdentifier">
                    <xsl:value-of select="hl7:CX.5"></xsl:value-of>
                </xsl:element>
                <xsl:element name="universalIdentifier">
                    <xsl:value-of select="hl7:CX.5"></xsl:value-of>
                </xsl:element>
                <xsl:element name="universalIdentifierTypeCode">
                    <xsl:value-of select="hl7:CX.5"></xsl:value-of>
                </xsl:element>
            </xsl:element>
        </xsl:element>
    </xsl:template>
    
</xsl:stylesheet>