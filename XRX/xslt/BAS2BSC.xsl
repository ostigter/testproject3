<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:env="http://www.example.org/envelope">
        
    <xsl:output method="xml" version="1.0" encoding="UTF-8" />
    
    <xsl:template match="/env:Envelope">
        <!-- Create new envelope as toplevel element -->
        <xsl:element name="env:Envelope">
            <!-- Copy header element as-is -->
            <xsl:copy-of select="env:Header" />
	        <!-- Create new body element specific for the message type -->
	        <xsl:element name="env:Body">
		        <!-- Get message ID -->
		        <xsl:variable name="messageId" select="env:Body/MessageId/text()" />

                <!-- Message ID 125: AreaControllerStatus -->
                <xsl:if test="$messageId = '125'">
                    <xsl:element name="AreaControllerStatus">
                       <xsl:copy-of select="env:Body/AreaId" />
                       <xsl:copy-of select="env:Body/ZoneId" />
                       <xsl:copy-of select="env:Body/ModuleId" />
                       <xsl:copy-of select="env:Body/RebootStatus" />
                    </xsl:element>
                </xsl:if>

	            <!-- Message ID 365: AlterTableBinReq -->
	            <xsl:if test="$messageId = '365'">
	                <xsl:element name="AlterTableBinReq">
	                   <xsl:copy-of select="env:Body/AreaId" />
                       <xsl:copy-of select="env:Body/ZoneId" />
                       <xsl:copy-of select="env:Body/ModuleId" />
	                </xsl:element>
	            </xsl:if>

	        </xsl:element>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>
