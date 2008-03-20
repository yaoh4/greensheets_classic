<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="C:\dev\nciprojects\gsquestions\xslt\Compare.xslt"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
<!--	<xsl:output method="text"/>-->
	<xsl:strip-space elements="*"/>
	<xsl:param name="paramType">3</xsl:param> 
	<xsl:param name="paramMech">R01</xsl:param>
	<xsl:template match="/">
		
		<xsl:element name="GF">
			<xsl:for-each select="/GreensheetQuestions/QuestionDef">
				<xsl:if test="count(descendant::QuestionDef/GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech] ) &gt; 0 ">
					<xsl:choose>
						<xsl:when test="GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech]">
<!--							<xsl:attribute name="ChildMissing"><xsl:text>N</xsl:text></xsl:attribute>-->
						</xsl:when>
						<xsl:otherwise>
							<xsl:element name="QuestionDef">
								<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
								<xsl:attribute name="type"><xsl:text>GD</xsl:text></xsl:attribute>
								<xsl:attribute name="ChildMissing"><xsl:text>Y</xsl:text></xsl:attribute>
								<xsl:attribute name="QuestionsMissing"><xsl:value-of select="count(descendant::QuestionDef/GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech] )"/></xsl:attribute>
								<xsl:attribute name="QText"><xsl:value-of select="QText"/></xsl:attribute>
							</xsl:element>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
				<xsl:apply-templates/>
			</xsl:for-each>
		</xsl:element>
		</xsl:template>
<!--	<xsl:template match="//QuestionDef[ResponseDefsList/ResponseDef/SelectionDef]">
		<xsl:if test="count(descendant::QuestionDef/GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech] ) &gt; 0">
			
			<xsl:choose>
				<xsl:when test="GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech]">
--><!--						<xsl:attribute name="ChildMissing"><xsl:text>N</xsl:text></xsl:attribute>	-->
<!--				</xsl:when>
				<xsl:otherwise>
					<xsl:element name="QuestionDef">
						<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
						<xsl:attribute name="type"><xsl:text>P</xsl:text></xsl:attribute>
--><!--							<xsl:attribute name="level"><xsl:value-of select="count(ancestor::node())"/></xsl:attribute>-->
<!--						<xsl:attribute name="ChildMissing"><xsl:text>Y</xsl:text></xsl:attribute>
						<xsl:attribute name="QuestionsMissing"><xsl:value-of select="count(ResponseDefsList/ResponseDef/SelectionDef/QuestionDef/GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech] )"/></xsl:attribute>
						<xsl:attribute name="QText"><xsl:value-of select="QText"/></xsl:attribute>
						<xsl:attribute name="parentQuestion"><xsl:value-of select="../../../../QText"/></xsl:attribute>
						<xsl:attribute name="parentQuestionId"><xsl:value-of select="../../../../@id"/></xsl:attribute>
						
					</xsl:element>
					
				</xsl:otherwise>
			</xsl:choose>
			
		</xsl:if>
		<xsl:apply-templates/>
	</xsl:template>
-->	
	<xsl:template match="//QuestionDef">
<!--		<xsl:text><xsl:value-of select="count(../../../../GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech] )"/></xsl:text>-->
		<xsl:choose>
			<xsl:when test="GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech] and count(../../../../GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech] ) = 0">
					<xsl:element name="QuestionDef">
						<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
<!--						<xsl:attribute name="type"><xsl:text>L</xsl:text></xsl:attribute>
						<xsl:attribute name="level"><xsl:value-of select="count(../../../../GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech] )"/></xsl:attribute>
						<xsl:attribute name="Orphaned"><xsl:text>Y</xsl:text></xsl:attribute>
-->						<xsl:attribute name="QText"><xsl:value-of select="QText"/></xsl:attribute>
						<xsl:attribute name="parentQuestion"><xsl:value-of select="../../../../QText"/></xsl:attribute>
						<xsl:attribute name="parentQuestionId"><xsl:value-of select="../../../../@id"/></xsl:attribute>
					</xsl:element>
			</xsl:when>
			<!--<xsl:otherwise>
				<xsl:element name="QuestionDef">
					<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
					<xsl:attribute name="type"><xsl:text>L</xsl:text></xsl:attribute>
					<xsl:attribute name="level"><xsl:value-of select="count(../../../../GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech] )"/></xsl:attribute>
					<xsl:attribute name="Orphaned"><xsl:text>Y</xsl:text></xsl:attribute>
					<xsl:attribute name="QText"><xsl:value-of select="QText"/></xsl:attribute>
					<xsl:attribute name="parentQuestion"><xsl:value-of select="../../../../QText"/></xsl:attribute>
					<xsl:attribute name="parentQuestionId"><xsl:value-of select="../../../../@id"/></xsl:attribute>
				</xsl:element>
			</xsl:otherwise>-->
		</xsl:choose>
		
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="Value"/>
	
	<xsl:template match="QInstructions"/>
	
	<xsl:template match="QText">
		<!--<QText>
			<xsl:value-of select="node()"/>
			</QText>-->
	</xsl:template>
	
</xsl:stylesheet>
