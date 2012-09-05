<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0">
    <xsl:output method="text" encoding="UTF-8"/>
    
    <xsl:template match="/">
        <xsl:apply-templates select="person/personIdentifiers"></xsl:apply-templates>
    </xsl:template>
    
    <xsl:template match="person/personIdentifiers">
        <xsl:param name="idType">
            <xsl:value-of select="identifierDomain/universalIdentifierTypeCode"/>
        </xsl:param>
        <xsl:if test="$idType = 'NID'">
            <xsl:value-of select="identifier"/>
        </xsl:if>
    </xsl:template>
    
</xsl:stylesheet>