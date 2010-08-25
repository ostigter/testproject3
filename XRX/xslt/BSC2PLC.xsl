<?xml version="1.0" encoding="UTF-8"?>
<!--
    File: plc2bsc.xsl
    
    Description:
        XSLT stylesheet to transform the CM message body as received from BSC
        to the format as expected by a PLC.
        The toplevel element is removed and, based on the root element name, a
        'messageId' element is inserted.
    
    History:
        08-Mar-2010 nlost Created.
        
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
    
    <xsl:template match="AreaControllerStatus">
        <MessageId>0125</MessageId>
        <xsl:copy-of select="AreaId" />
        <xsl:copy-of select="ZoneId" />
        <xsl:copy-of select="ModuleId" />
        <xsl:copy-of select="RebootStatus" />
    </xsl:template>

    <xsl:template match="AlterTableBinReq">
        <MessageId>0365</MessageId>
        <xsl:copy-of select="AreaId" />
        <xsl:copy-of select="ZoneId" />
        <xsl:copy-of select="ModuleId" />
    </xsl:template>

</xsl:stylesheet>
