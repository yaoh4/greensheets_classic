<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:param name="appltype">1</xsl:param>
	<xsl:param name="fundingmech">R01</xsl:param>
	<xsl:output method="xml" indent="yes"/>

<xsl:template match="node()"/> <!-- Overrides the default of copying text content, to do nothing, i.e. not to copy -->
  	
<xsl:template match="/">
<xsl:text>
</xsl:text>
	<xsl:copy>
		<xsl:apply-templates/>
	</xsl:copy>
</xsl:template>

<xsl:template match="GreensheetQuestions">
	<xsl:copy>
		<xsl:apply-templates/>
	</xsl:copy>
</xsl:template>

<xsl:template match="QuestionDef[GrantTypeMechs/TypeMech[@type=$appltype and @mech=$fundingmech]]">
	<xsl:element name="QuestionDef">
		<xsl:attribute name="id">
			<xsl:value-of select="@id"/>
		</xsl:attribute>
		<xsl:apply-templates/>
	</xsl:element>
</xsl:template>

<!-- 
<xsl:template match="QuestionDef[not(GrantTypeMechs/TypeMech[@type=$appltype and @mech=$fundingmech])]"/>
<xsl:template match="GrantTypeMechs"/>
<xsl:template match="TypeMech"/>
-->
 
<xsl:template match="QText">
	<xsl:copy-of select="."/>
</xsl:template>
<xsl:template match="QInstructions">
	<xsl:copy-of select="."/>
</xsl:template>
<xsl:template match="ResponseDefsList">
	<xsl:copy>
		<xsl:apply-templates/>
	</xsl:copy>
</xsl:template>

<xsl:template match="ResponseDef">
	<xsl:copy>
		<xsl:attribute name="id"><xsl:value-of select="@id"></xsl:value-of></xsl:attribute>
		<xsl:attribute name="type"><xsl:value-of select="@type"></xsl:value-of></xsl:attribute>
		<xsl:apply-templates/>
	</xsl:copy>
</xsl:template>

<xsl:template match="SelectionDef">
	<xsl:copy>
		<xsl:attribute name="id"><xsl:value-of select="@id"></xsl:value-of></xsl:attribute>
		<xsl:apply-templates/>
	</xsl:copy>
</xsl:template>

<xsl:template match="Value">
	<xsl:copy-of select="."/>
</xsl:template>

</xsl:stylesheet>