<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	
	<xsl:variable name="varType">1</xsl:variable>
	<xsl:variable name="varMech">R01</xsl:variable>


	<xsl:template match="/">
		<GreensheetForm>
			<xsl:apply-templates/>
		</GreensheetForm>
	</xsl:template>
	
	<xsl:template match="*">
		<xsl:variable name="varTagName">
			<xsl:value-of select="name(.)"/>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$varTagName = 'QuestionGroup' ">
				<xsl:if test="GrantTypeMechs/TypeMech[@type=$varType and @mech=$varMech]">
					<xsl:call-template name="copyElement"/>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="copyElement"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="copyElement">
		<xsl:element name="{name(.)}">
			<xsl:for-each select="@*">
				<xsl:attribute name="{name(.)}"><xsl:value-of select="."/></xsl:attribute>
			</xsl:for-each>
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
