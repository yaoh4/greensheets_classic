<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:msxsl="urn:schemas-microsoft-com:xslt" xmlns:gsExtn="javascript:code">
	<msxsl:script implements-prefix="gsExtn" language="javascript">  
		var QuestionNumber = 0;
		var ResponseIdNumber = 0;
		var SelectionIdNumber = 0;
		
		var strQuestionDefType = "PNCQ";
		var strResponseDefType = "RD";
		var strSelectionDefType = "SEL";
		
		function getNextSequenceNumber(strType)
		{
			var NextSequenceNumber = 0;
			switch (strType)
			{
				case strQuestionDefType 		:	QuestionNumber += 1;
														NextSequenceNumber = QuestionNumber;
														break;
									
				case strResponseDefType 		:	ResponseIdNumber += 1;
														NextSequenceNumber = ResponseIdNumber;
														break;
									
				case strSelectionDefType 		:	SelectionIdNumber += 1;
														NextSequenceNumber = SelectionIdNumber ;
														break;
			}
			
			return NextSequenceNumber;
		}
		
		function getQuestionDefIdNumber()
		{
			var strQuestionDefId = strQuestionDefType + "_" + getNextSequenceNumber(strQuestionDefType);
			return strQuestionDefId ;
		}
		
		function getResponseDefIdNumber(strType)
		{
			var strResponseDefId = strQuestionDefType + "_" + QuestionNumber + "_" + strResponseDefType + "_" + strType + "_" + getNextSequenceNumber(strResponseDefType);
			return strResponseDefId ;
		}
		
		function getSelectionDefIdNumber()
		{
			var stSelectionDefId = strSelectionDefType + "_" + getNextSequenceNumber(strSelectionDefType );
			return stSelectionDefId ;
		}

	</msxsl:script>
	
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"></xsl:output>
	<xsl:variable name="varGSId">PGM_NC</xsl:variable>
	<xsl:variable name="varType">1</xsl:variable>
	<xsl:variable name="varMech">R01</xsl:variable>
	
	<xsl:template match="/">
		<GreensheetQuestions id="{$varGSId}" current="true">
			<xsl:apply-templates select="//QuestionGrouping"></xsl:apply-templates>
		</GreensheetQuestions>
	</xsl:template>
	<xsl:template match="QuestionGrouping">
			<xsl:apply-templates select="Question"></xsl:apply-templates>
	</xsl:template>
	<xsl:template match="Question">
		<QuestionDef>
			<xsl:attribute name="id"><xsl:value-of select="gsExtn:getQuestionDefIdNumber()"></xsl:value-of></xsl:attribute>
			<GrantTypeMechs>
				<TypeMech type="{$varType}" mech="{$varMech}"></TypeMech>
			</GrantTypeMechs>
			<QText>
				<xsl:value-of select="QText"></xsl:value-of>
			</QText>
			<QInstructions>
				<xsl:value-of select="Instructions"></xsl:value-of>
			</QInstructions>
			<ResponseDefsList>
				<xsl:apply-templates select="ResponseTypes"></xsl:apply-templates>
			</ResponseDefsList>
		</QuestionDef>
	</xsl:template>
	<xsl:template match="ResponseTypes">
		<xsl:variable name="varResponseType">
			<xsl:choose>
				<xsl:when test="UserResponse/@type = 'SELECT' ">RADIO</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="UserResponse/@type"></xsl:value-of>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="varResponseTypeDefId">
			<xsl:choose>
				<xsl:when test="$varResponseType = 'TEXT' "><xsl:value-of select="gsExtn:getResponseDefIdNumber('TX')"></xsl:value-of></xsl:when>
				<xsl:when test="$varResponseType = 'NUMBER' "><xsl:value-of select="gsExtn:getResponseDefIdNumber('NU')"></xsl:value-of></xsl:when>
				<xsl:when test="$varResponseType = 'STRING' "><xsl:value-of select="gsExtn:getResponseDefIdNumber('ST')"></xsl:value-of></xsl:when>
				<xsl:when test="$varResponseType = 'DATE' "><xsl:value-of select="gsExtn:getResponseDefIdNumber('DT')"></xsl:value-of></xsl:when>
				<xsl:when test="$varResponseType = 'RADIO' "><xsl:value-of select="gsExtn:getResponseDefIdNumber('RB')"></xsl:value-of></xsl:when>
				<xsl:when test="$varResponseType = 'DROP_DOWN' "><xsl:value-of select="gsExtn:getResponseDefIdNumber('DD')"></xsl:value-of></xsl:when>
				<xsl:when test="$varResponseType = 'CHECK_BOX' "><xsl:value-of select="gsExtn:getResponseDefIdNumber('CB')"></xsl:value-of></xsl:when>
				<xsl:when test="$varResponseType = 'FILE' "><xsl:value-of select="gsExtn:getResponseDefIdNumber('FL')"></xsl:value-of></xsl:when>
				<xsl:when test="$varResponseType = 'COMMENT' "><xsl:value-of select="gsExtn:getResponseDefIdNumber('CM')"></xsl:value-of></xsl:when>
			</xsl:choose>
		</xsl:variable>
		<ResponseDef id="{$varResponseTypeDefId}" type="{$varResponseType}">
			<xsl:apply-templates select="Selection"></xsl:apply-templates>
		</ResponseDef >
	</xsl:template>
	<xsl:template match="Selection">
		<SelectionDef>
			<xsl:attribute name="id"><xsl:value-of select="gsExtn:getSelectionDefIdNumber()"></xsl:value-of></xsl:attribute>
			<Value>
				<xsl:value-of select="value"></xsl:value-of>
			</Value>
			<xsl:if test="target_subquestions/id">
					<xsl:for-each select="target_subquestions/id">
						<xsl:variable name="varTargetId"><xsl:value-of select="."></xsl:value-of></xsl:variable>
						<xsl:apply-templates select="../../../../SubQuestions/Question[@id = $varTargetId]"></xsl:apply-templates>
					</xsl:for-each>
			</xsl:if>
		</SelectionDef>
	</xsl:template>
</xsl:stylesheet>
