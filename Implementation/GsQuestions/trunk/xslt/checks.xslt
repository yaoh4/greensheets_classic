<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<checks>
			<xsl:for-each select="//QuestionDef">
				<xsl:variable name="Qid">
					<xsl:value-of select="@id"/>
				</xsl:variable>
				<xsl:for-each select="ResponseDefsList/ResponseDef">
					<xsl:variable name="Rid">
						<xsl:value-of select="@id"/>
					</xsl:variable>
					<xsl:variable name="RDtype">
						<xsl:value-of select="@type"/>
					</xsl:variable>
					<xsl:call-template name="checkQuestionIdMismatch">
						<xsl:with-param name="Qid" select="$Qid"/>
						<xsl:with-param name="Rid" select="$Rid"/>
						<xsl:with-param name="RDtype" select="$RDtype"/>
					</xsl:call-template>
					<xsl:call-template name="checkResponseTypeMismatch">
						<xsl:with-param name="Qid" select="$Qid"/>
						<xsl:with-param name="Rid" select="$Rid"/>
						<xsl:with-param name="RDtype" select="$RDtype"/>
					</xsl:call-template>
				</xsl:for-each>
			</xsl:for-each>
		</checks>
	</xsl:template>
	<!--
	
	-->
	<xsl:template name="checkResponseTypeMismatch">
		<xsl:param name="Qid"/>
		<xsl:param name="Rid"/>
		<xsl:param name="RDtype"/>
		<xsl:if test="(($RDtype='NUMBER') and not(contains($Rid,'NU'))) or (($RDtype='TEXT') and not(contains($Rid,'TX'))) or (($RDtype='STRING') and not(contains($Rid,'ST'))) or (($RDtype='DATE') and not(contains($Rid,'DT'))) or (($RDtype='RADIO') and not(contains($Rid,'RB'))) or (($RDtype='CHECK_BOX') and not(contains($Rid,'CB'))) or (($RDtype='DROP_DOWN') and not(contains($Rid,'DD')))">
			<xsl:call-template name="createTypeMismatchError">
				<xsl:with-param name="Qid" select="$Qid"/>
				<xsl:with-param name="Rid" select="$Rid"/>
				<xsl:with-param name="RDtype" select="$RDtype"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<xsl:template name="createTypeMismatchError">
		<xsl:param name="Qid"/>
		<xsl:param name="Rid"/>
		<xsl:param name="RDtype"/>
		<Error>
			<xsl:attribute name="ErrorType">TYPE_MISMATCH</xsl:attribute>
			<xsl:attribute name="QuestionId"><xsl:value-of select="$Qid"/></xsl:attribute>
			<xsl:attribute name="ResponseDefId"><xsl:value-of select="$Rid"/></xsl:attribute>
			<xsl:attribute name="Type"><xsl:value-of select="$RDtype"/></xsl:attribute>
		</Error>
	</xsl:template>
	<!--
	
	-->
	<xsl:template name="checkQuestionIdMismatch">
		<xsl:param name="Qid"/>
		<xsl:param name="Rid"/>
		<xsl:param name="RDtype"/>
		<xsl:if test="not(starts-with($Rid,$Qid))">
			<Error>
				<xsl:attribute name="ErrorType">ID_MISMATCH</xsl:attribute>
				<xsl:attribute name="QuestionId"><xsl:value-of select="$Qid"/></xsl:attribute>
				<xsl:attribute name="ResponseDefId"><xsl:value-of select="$Rid"/></xsl:attribute>
				<xsl:attribute name="Type"><xsl:value-of select="$RDtype"/></xsl:attribute>
			</Error>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
