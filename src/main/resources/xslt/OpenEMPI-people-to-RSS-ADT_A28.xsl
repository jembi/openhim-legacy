<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:rhea="http://rw.moh.gov/rhea/1.0" version="1.0">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <xsl:element name="rss">
            <xsl:attribute name="version">
                <xsl:value-of select="'2.0'"/>
            </xsl:attribute>
            <xsl:element name="channel">
                <xsl:element name="title">
                    <xsl:value-of select="'ADT_A28 Feed'"/>
                </xsl:element>
                <xsl:element name="link">
                    <xsl:value-of select="'http://rw.moh.gov/rhea/1.0'"/>
                </xsl:element>
                <xsl:element name="description">
                    <xsl:value-of select="'RHEA HIE ADT_A28 Feed'"/>
                </xsl:element>
                <xsl:apply-templates select="/people/person"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="/people/person">
        <xsl:element name="item">
            <xsl:element name="title">
                <xsl:value-of select="'ADT_A28'"/>
            </xsl:element>
            <xsl:element name="link"></xsl:element>
            <xsl:element name="description"/>
            <xsl:element name="rhea:content">
                <xsl:element name="ADT_A05">
                    <xsl:element name="MSH">
                        <xsl:element name="MSH.1">
                            <xsl:value-of select="'|'"/>
                        </xsl:element>
                        <xsl:element name="MSH.2">
                            <xsl:value-of select="'^~\&amp;'"/>
                        </xsl:element>
                        <xsl:element name="MSH.7">
                            <xsl:element name="TS.1">
                                <xsl:value-of select="''"/>
                            </xsl:element>
                        </xsl:element>
                        <xsl:element name="MSH.9">
                            <xsl:element name="MSG.1">
                                <xsl:value-of select="'ADT'"/>
                            </xsl:element>
                            <xsl:element name="MSG.2">
                                <xsl:value-of select="'A28'"/>
                            </xsl:element>
                            <xsl:element name="MSG.3">
                                <xsl:value-of select="'ADT_A05'"/>
                            </xsl:element>
                        </xsl:element>
                        <xsl:element name="MSH.12">
                            <xsl:element name="VID.1">
                                <xsl:value-of select="'2.5'"/>
                            </xsl:element>
                        </xsl:element>
                    </xsl:element>
                    <xsl:element name="PID">
                        <xsl:apply-templates select="personIdentifiers"/>
                        <xsl:element name="PID.5">
                            <xsl:element name="XPN.1">
                                <xsl:element name="FN.1">
                                    <xsl:value-of select="familyName"/>
                                </xsl:element>
                            </xsl:element>
                            <xsl:element name="XPN.2">
                                <xsl:value-of select="givenName"/>
                            </xsl:element>
                        </xsl:element>
                        <xsl:element name="PID.7">
                            <xsl:element name="TS.1">
                                <xsl:call-template name="FormatDate">
                                    <xsl:with-param name="date" select="dateOfBirth"/>
                                </xsl:call-template>
                            </xsl:element>
                        </xsl:element>
                        <xsl:element name="PID.8">
                            <xsl:value-of select="gender/genderCode"/>
                        </xsl:element>
                        <xsl:element name="PID.11">
                            <!-- District -->
                            <xsl:element name="XAD.3">
                                <xsl:value-of select="city"/>
                            </xsl:element>
                            <!-- Province -->
                            <xsl:element name="XAD.4">
                                <xsl:value-of select="state"/>
                            </xsl:element>
                            <!-- Country -->
                            <xsl:element name="XAD.6">
                                <xsl:value-of select="country"/>
                            </xsl:element>
                            <!-- Cell -->
                            <xsl:element name="XAD.8">
                                <xsl:value-of select="address1"/>
                            </xsl:element>
                            <!-- Sector -->
                            <xsl:element name="XAD.9">
                                <xsl:value-of select="address2"/>
                            </xsl:element>
                            <!-- Village -->
                            <xsl:element name="XAD.10">
                                <xsl:value-of select="village"/>
                            </xsl:element>
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template name="FormatDate">
        <xsl:param name="date"/>
        <xsl:variable name="yy">
            <xsl:value-of select="substring($date,1,4)"/>
        </xsl:variable>
        <xsl:variable name="mm">
            <xsl:value-of select="substring($date,6,2)"/>
        </xsl:variable>
        <xsl:variable name="dd">
            <xsl:value-of select="substring($date,9,2)"/>
        </xsl:variable>
        <xsl:value-of select="$yy"/>
        <xsl:value-of select="$mm"/>
        <xsl:value-of select="$dd"/>
    </xsl:template>

    <xsl:template match="people/person/personIdentifiers">
        <xsl:element name="PID.3">
            <xsl:element name="CX.1">
                <xsl:value-of select="identifier"/>
            </xsl:element>
            <xsl:element name="CX.5">
                <xsl:value-of select="identifierDomain/universalIdentifierTypeCode"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>
