<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:req="http://www.example.com/request"
        xmlns:res="http://www.example.com/response">
	    
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" />
    
    <xsl:template match="/">
        <xsl:element name="res:Response">
            <xsl:element name="res:TicketNr">
                <xsl:value-of select="//req:TicketNr/text()" />
            </xsl:element>
            <xsl:element name="res:Status">
                <xsl:text>Created</xsl:text>
            </xsl:element>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>

