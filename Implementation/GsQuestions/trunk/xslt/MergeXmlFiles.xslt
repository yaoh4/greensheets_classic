<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:msxsl="urn:schemas-microsoft-com:xslt" xmlns:gsExtn="javascript:code">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<xsl:apply-templates select="//GreensheetQuestions"/>
	</xsl:template>
	<xsl:template match="GreensheetQuestions">
		<GreensheetQuestions id="PGM_NC" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="..\schema\GsForm.xsd">
			<!-- from the parent doc PC_Questions.xml -->
			<xsl:apply-templates select="QuestionDef"/>
			<!-- from PNC_Questions.xml -->
			<xsl:apply-templates select="document('../xml/PNC_Questions.xml')/GreensheetQuestions/QuestionDef"/>
			<!-- from SC_Questions.xml -->
			<xsl:apply-templates select="document('../xml/SC_Questions.xml')/GreensheetQuestions/QuestionDef"/>
			<!-- from SNC_Questions.xml -->
			<xsl:apply-templates select="document('../xml/SNC_Questions.xml')/GreensheetQuestions/QuestionDef"/>
		</GreensheetQuestions>
	</xsl:template>
	<!--<xsl:template match="@*|node()"> -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
