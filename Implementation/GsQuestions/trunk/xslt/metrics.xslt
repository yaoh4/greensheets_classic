<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	
	
	<xsl:template match="/">
	<metrics>
		<TotalNumberOfQuestions>
			<xsl:value-of select="count(//QuestionDef)"/>
		</TotalNumberOfQuestions>
		<NumberOfQuestions1U10>
			<xsl:value-of select="count(//QuestionDef/GrantTypeMechs/TypeMech[@type='1' and @mech='U10'])"/>
		</NumberOfQuestions1U10>	
				<NumberOfQuestions5U10>
			<xsl:value-of select="count(//QuestionDef/GrantTypeMechs/TypeMech[@type='5' and @mech='U10'])"/>
		</NumberOfQuestions5U10>	
		
			
	</metrics>	
	</xsl:template>
	
	
	
</xsl:stylesheet>
