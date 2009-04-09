<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:res="http://www.example.com/response">
	    
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" />
    
    <xsl:template match="/">
        <xsl:element name="res:Response">
            <xsl:element name="res:TicketNr">
                <xsl:value-of select="//res:TicketNr/text()" />
            </xsl:element>
            <xsl:element name="res:Status">
                <xsl:text>Closed</xsl:text>
            </xsl:element>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>

