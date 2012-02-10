<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:foo="http://www.example.org/XMLSchema/Foo/v1.0">

    <xsl:output media-type="xml" version="1.0" encoding="UTF-8" indent="yes" />
    
    <!-- External parameters -->
    <xsl:param name="documentId" />
    <xsl:param name="machineId" />
    <xsl:param name="lotId" />
    
    <!-- Identity template, copying everything by default -->
    <xsl:template match="@* | node()">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()" />
        </xsl:copy>
    </xsl:template>
    
    <!-- Set document ID -->
    <xsl:template match="/foo:document/header/documentId">
        <xsl:element name="documentId">
            <xsl:value-of select="$documentId" />
        </xsl:element>
    </xsl:template>

    <!-- Set machine ID -->
    <xsl:template match="/foo:document/input/machineId">
        <xsl:element name="machineId">
            <xsl:value-of select="$machineId" />
        </xsl:element>
    </xsl:template>

    <!-- Set lot ID -->
    <xsl:template match="/foo:document/input/lotId">
        <xsl:element name="lotId">
            <xsl:value-of select="$lotId" />
        </xsl:element>
    </xsl:template>

    <!-- Increase every X value with 0.1 -->
    <xsl:template match="/foo:document/measurements/measurement/valueX">
        <xsl:element name="valueX">
            <xsl:variable name="sequence">
                <xsl:number count="/foo:document/measurements/measurement" />
            </xsl:variable>
            <xsl:value-of select="format-number(. + ($sequence * 0.1), '0.00')" />
        </xsl:element>
    </xsl:template>

    <!-- Increase every Y value with 0.1 -->
    <xsl:template match="/foo:document/measurements/measurement/valueY">
        <xsl:element name="valueY">
            <xsl:variable name="sequence">
                <xsl:number count="/foo:document/measurements/measurement" />
            </xsl:variable>
            <xsl:value-of select="format-number(. + ($sequence * 0.1), '0.00')" />
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>
