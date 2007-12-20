<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    
 <xsl:output method="xml" indent="yes"/>
 <xsl:strip-space elements="*"/>
<xsl:param name="paramType">3</xsl:param> 
 <xsl:param name="paramMech">R01</xsl:param>
 <xsl:template match="/" >
    <xsl:apply-templates/>
     
 </xsl:template> 
 <xsl:template match="GreensheetQuestions">
  <GreensheetQuestions>
   <xsl:copy-of select="node|@*"/>
   <xsl:for-each select="QuestionDef">
    <QuestionDef>
     <xsl:copy-of select="node|@*"/>
       <xsl:apply-templates/>  
     </QuestionDef>
   </xsl:for-each>
  </GreensheetQuestions>
 </xsl:template>
 
 
 <xsl:template match="QuestionDef">
  <QuestionDef>
   <xsl:apply-templates/> 
 
  </QuestionDef>
<!--      <xsl:value-of select="position()"/>-->
 </xsl:template>
 
 <xsl:template match="ResponseDefsList">
  <ResponseDefsList>
  <xsl:for-each select="ResponseDef">
   <ResponseDef>
    <xsl:copy-of select="node|@*"/>
       <xsl:apply-templates/> 
   </ResponseDef>
  </xsl:for-each>
  </ResponseDefsList>
 </xsl:template>
 
 <xsl:template match="SelectionDef">
    <SelectionDef>
     <xsl:copy-of select="node|@*"/>
         <xsl:for-each select="Value">
          <Value>
          <xsl:apply-templates/> 
          </Value>
         </xsl:for-each>
        <xsl:for-each select="QuestionDef">
         
          <xsl:if test="GrantTypeMechs/TypeMech[@type=$paramType and @mech=$paramMech]">
           <QuestionDef>
            <xsl:copy-of select="node|@*"/>
           <xsl:apply-templates/>
           </QuestionDef>
          </xsl:if>
         
        </xsl:for-each>
    </SelectionDef>
 </xsl:template>
 
 <xsl:template match="Value">
   <xsl:value-of select="node()"/>
 </xsl:template> 
 <xsl:template match="QText">
  <QText>
  <xsl:value-of select="node()"/>
  </QText>
 </xsl:template>

 <xsl:template match="GrantTypeMechs">
  <GrantTypeMechs>
  <xsl:apply-templates/> 
  </GrantTypeMechs>
 </xsl:template>
 
<xsl:template match="QInstructions">
 <QInstructions>
  <xsl:apply-templates/> 
 </QInstructions>
</xsl:template>
 
 <xsl:template match="TypeMech">
  <xsl:apply-templates/>
  <xsl:choose>
   <xsl:when test="@type=$paramType and @mech=$paramMech">
    <xsl:element name="TypeMech">
     <xsl:attribute name="type"><xsl:value-of select="$paramType"/></xsl:attribute>
     <xsl:attribute name="mech"><xsl:value-of select="$paramMech"/></xsl:attribute>
    </xsl:element>
   </xsl:when>
   
  </xsl:choose>
  
 </xsl:template>
 
 <xsl:template match="ResponseDef">
   
    <xsl:for-each select="SelectionDef">
         <xsl:apply-templates/>  
   </xsl:for-each>

 </xsl:template>
 
</xsl:stylesheet>
