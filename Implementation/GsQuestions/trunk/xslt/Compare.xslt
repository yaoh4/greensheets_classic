<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="C:\dev\nciprojects\gsquestions\xslt\Compare.xslt"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
<!--	<xsl:output method="text"/>-->
	<xsl:strip-space elements="*"/>
	<xsl:param name="paramType">3</xsl:param> 
	<xsl:param name="paramMech">R35</xsl:param>
	<xsl:template match="/">
		<GF>
			<xsl:for-each select="/GreensheetQuestions/QuestionDef">
				<xsl:if test="count(descendant::QuestionDef/GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech] ) &gt; 0">
					<xsl:element name="QuestionDef">
							<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
<!--							<xsl:attribute name="posn"><xsl:value-of select="position()"/></xsl:attribute>
							<xsl:attribute name="last"><xsl:value-of select="last()"/></xsl:attribute>
-->							<xsl:attribute name="type"><xsl:text>GD</xsl:text></xsl:attribute>
		<!--					<xsl:call-template name="childorparent"/>-->
							<xsl:choose>
								<xsl:when test="GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech]">
									<xsl:attribute name="has"><xsl:text>Y</xsl:text></xsl:attribute>	
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="has"><xsl:text>N</xsl:text></xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:if test="count(descendant::QuestionDef/GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech] ) &gt; 0">
								<xsl:choose>
									<xsl:when test="GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech]">
										<xsl:attribute name="missing"><xsl:text>N</xsl:text></xsl:attribute>	
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="missing"><xsl:text>Y</xsl:text></xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
								
							</xsl:if>
							<xsl:apply-templates/>
						</xsl:element>
				</xsl:if>
			</xsl:for-each>
		</GF>
	</xsl:template>
	<xsl:template match="QuestionDef[ResponseDefsList/ResponseDef/SelectionDef/QuestionDef]">
		<xsl:if test="count(descendant::QuestionDef/GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech] ) &gt; 0">
		<xsl:element name="QuestionDef">
			<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
<!--					<xsl:attribute name="posn"><xsl:value-of select="position()"/></xsl:attribute>
			<xsl:attribute name="last"><xsl:value-of select="last()"/></xsl:attribute>
-->					<xsl:attribute name="type"><xsl:text>P</xsl:text></xsl:attribute>
			<xsl:choose>
				<xsl:when test="GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech]">
					<xsl:attribute name="has"><xsl:text>Y</xsl:text></xsl:attribute>	
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="has"><xsl:text>N</xsl:text></xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>					
			<xsl:if test="count(descendant::QuestionDef/GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech] ) &gt; 0">
				<xsl:choose>
					<xsl:when test="GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech]">
						<xsl:attribute name="missing"><xsl:text>N</xsl:text></xsl:attribute>	
					</xsl:when>
					<xsl:otherwise>
						<xsl:attribute name="missing"><xsl:text>Y</xsl:text></xsl:attribute>
					</xsl:otherwise>
				</xsl:choose>
				
			</xsl:if>
<!--					<xsl:call-template name="childorparent"/>-->

			
			<xsl:apply-templates/>
		</xsl:element>
		</xsl:if>
	</xsl:template>
	<xsl:template match="QuestionDef">
		<xsl:choose>
			<xsl:when test="GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech]">
				<xsl:element name="QuestionDef">
					<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
<!--					<xsl:attribute name="posn"><xsl:value-of select="position()"/></xsl:attribute>
					<xsl:attribute name="last"><xsl:value-of select="last()"/></xsl:attribute>
-->					<xsl:attribute name="type"><xsl:text>L</xsl:text></xsl:attribute>
					
					<xsl:choose>
						<xsl:when test="GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech]">
							<xsl:attribute name="has"><xsl:text>Y</xsl:text></xsl:attribute>	
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="has"><xsl:text>N</xsl:text></xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:apply-templates/>
					<xsl:text>&#xa;&#xa;</xsl:text>
				</xsl:element>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="Value"/>
	
	<xsl:template match="QInstructions"/>
	
	<xsl:template match="QText">
	            <QText>
		<xsl:value-of select="node()"/>
	            </QText>
	</xsl:template>
	<xsl:template name="childorparent">
<!--		<xsl:for-each select="descendant::QuestionDef">
			<xsl:choose>
				<xsl:when test="GrantTypeMechs/TypeMech[@type='3' and @mech='R01']">
					<xsl:attribute name="hasChildren"><xsl:text>Y</xsl:text></xsl:attribute>	
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="hasChildren"><xsl:text>N</xsl:text></xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text>&#xa;&#xa;</xsl:text>
			</xsl:for-each>-->
		<xsl:if test="count(descendant::QuestionDef/GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech] ) &gt; 0 ">
			<xsl:attribute name="hasChildren"><xsl:text>Y</xsl:text></xsl:attribute>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
