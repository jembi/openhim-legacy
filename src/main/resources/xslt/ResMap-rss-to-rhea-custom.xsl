<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#" xmlns:rm="http://resourcemap.instedd.org/api/1.0">
	<xsl:output method="xml" indent="yes"/>
	<xsl:strip-space elements="*"/>

	<xsl:template match="/">
		<xsl:element name="rss">
			<xsl:attribute name="version">2.0</xsl:attribute>
			<xsl:element name="channel">
				<xsl:element name="title">Rwanda</xsl:element>
				<xsl:apply-templates select="rss/channel/item"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>

	<xsl:template match="item">
		<xsl:element name="item">
			<xsl:element name="title"><xsl:value-of select="title"/></xsl:element>
			<xsl:element name="pubDate"><xsl:value-of select="pubDate"/></xsl:element>
			<xsl:element name="facilityReport">
				<xsl:element name="name">
					<xsl:value-of select="title"/>
				</xsl:element>
				<xsl:element name="latitude">
					<xsl:value-of select="geo:lat"/>
				</xsl:element>
				<xsl:element name="longitude">
					<xsl:value-of select="geo:long"/>
				</xsl:element>
				<xsl:element name="updatedSince">
					<xsl:value-of select="pubDate"/>
				</xsl:element>
				<xsl:for-each select="rm:properties/*">
					<xsl:variable name="elementName">
						<xsl:value-of select="local-name(.)"/>
					</xsl:variable>
					<xsl:element name="{$elementName}">
						<xsl:value-of select="." />
					</xsl:element>
				</xsl:for-each>
			</xsl:element>
		</xsl:element>
	</xsl:template>
		
</xsl:stylesheet>