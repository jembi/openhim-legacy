<?xml version="1.0" encoding="UTF-8"?>
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
            <xsl:element name="state">
                <xsl:value-of select="hl7:ADT_A05/hl7:PID/hl7:PID.11/hl7:XAD.4"></xsl:value-of>
            </xsl:element>
            <xsl:element name="city">
                <xsl:value-of select="hl7:ADT_A05/hl7:PID/hl7:PID.11/hl7:XAD.3"></xsl:value-of>
            </xsl:element>
            <xsl:element name="address2">
                <xsl:value-of select="hl7:ADT_A05/hl7:PID/hl7:PID.11/hl7:XAD.9"></xsl:value-of>
            </xsl:element>
            <xsl:element name="address1">
                <xsl:value-of select="hl7:ADT_A05/hl7:PID/hl7:PID.11/hl7:XAD.8"></xsl:value-of>
            </xsl:element>
            
            <xsl:apply-templates select="hl7:ADT_A05/hl7:PID/hl7:PID.3"></xsl:apply-templates>
            
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
                <xsl:element name="identifierDomainName">
                    <xsl:value-of select="hl7:CX.5"></xsl:value-of>
                </xsl:element>
                <xsl:element name="identifierDomainDescription">
                    <xsl:value-of select="hl7:CX.5"></xsl:value-of>
                </xsl:element>
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