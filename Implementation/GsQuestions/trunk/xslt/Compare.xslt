<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="C:\dev\nciprojects\gsquestions\xslt\Compare.xslt"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
<!--	<xsl:output method="text"/>-->
	<xsl:strip-space elements="*"/>
	<xsl:template match="/">
		<GF>
			<xsl:for-each select="/GreensheetQuestions/QuestionDef">
				<xsl:element name="QuestionDef">
					<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
					<xsl:attribute name="posn"><xsl:value-of select="position()"/></xsl:attribute>
					<xsl:attribute name="last"><xsl:value-of select="last()"/></xsl:attribute>
					<xsl:attribute name="type"><xsl:text>GD</xsl:text></xsl:attribute>
<!--					<xsl:call-template name="childorparent"/>-->
					<xsl:apply-templates/>
				</xsl:element>
			</xsl:for-each>
		</GF>
	</xsl:template>
	<xsl:template match="QuestionDef[ResponseDefsList/ResponseDef/SelectionDef/QuestionDef]">
		<xsl:element name="QuestionDef">
			<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
			<xsl:attribute name="posn"><xsl:value-of select="position()"/></xsl:attribute>
			<xsl:attribute name="last"><xsl:value-of select="last()"/></xsl:attribute>
			<xsl:attribute name="type"><xsl:text>P</xsl:text></xsl:attribute>
<!--			<xsl:call-template name="childorparent"/>-->
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="QuestionDef">
		<xsl:element name="QuestionDef">
			<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
			<xsl:attribute name="posn"><xsl:value-of select="position()"/></xsl:attribute>
			<xsl:attribute name="last"><xsl:value-of select="last()"/></xsl:attribute>
			<xsl:attribute name="type"><xsl:text>L</xsl:text></xsl:attribute>
			<xsl:apply-templates/>
			<xsl:text>&#xa;&#xa;</xsl:text>
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="Value"/>
	
	<xsl:template match="QInstructions"/>
	
	<xsl:template match="QText">
	            <QText>
		<xsl:value-of select="node()"/>
	            </QText>
	</xsl:template>
	<xsl:template name="childorparent">
		<xsl:for-each select="*">
			<xsl:choose>
				<xsl:when test="position() != last() -1 and last() > 2">
					<xsl:attribute name="Children"><xsl:value-of select="node()"/></xsl:attribute>
				</xsl:when>
				<xsl:when test="position() = last()  and last() > 1">
					<xsl:attribute name="Children"><xsl:value-of select="@id"/></xsl:attribute>
				</xsl:when>
				<xsl:when test="last() =1">
					<xsl:attribute name="Children">NC</xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="@*"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text>&#xa;&#xa;</xsl:text>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
