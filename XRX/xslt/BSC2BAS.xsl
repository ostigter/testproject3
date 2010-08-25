<?xml version="1.0" encoding="UTF-8"?>
<!--
    File: plc2bsc.xsl
    
    Description:
        XSLT stylesheet to transform the CM message body as received from BSC
        to the format as expected by the BAS.
        The toplevel element is removed and, based on the root element name, a
        'messageId' element is inserted.
    
    History:
        12-Mar-2010 nlost Created.
        
    Copyright 2010 Vanderlande Industries
 -->
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:env="http://xmlns.vanderlande.com/cm/envelope">
        
    <xsl:output method="xml" version="1.0" encoding="UTF-8" />
    
    <xsl:template match="/env:Envelope">
        <!-- Create new envelope as toplevel element -->
        <xsl:element name="env:Envelope">
            <!-- Copy header element as-is -->
            <xsl:copy-of select="env:Header" />
            <!-- Create a new body element -->
            <xsl:element name="env:Body">
	            <!-- Transform the message specific body element -->
	            <xsl:apply-templates select="env:Body/child::node()" />
            </xsl:element>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="SetCartGroup">
        <MessageId>0352</MessageId>
        <xsl:copy-of select="ClientId" />
        <xsl:copy-of select="ServerId" />
        <xsl:for-each select="CartGroupTable/CartGroup">
            <xsl:variable name="index" select="position(.)" />
            <xsl:variable name="elementName" select="concat('CartId', $index)" />
            <xsl:copy-of select="CartId" />
            <xsl:copy-of select="GroupId" />
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
