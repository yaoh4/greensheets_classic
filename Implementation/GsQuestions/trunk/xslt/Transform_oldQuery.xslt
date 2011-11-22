<?xml-stylesheet type="text/xsl"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes"/>
  
    <xsl:param name="paramType"></xsl:param>
 
    <xsl:param name="paramMech"></xsl:param>

        <xsl:param name="paramValidation">true</xsl:param>
 
    <xsl:param name="paramGenerateVelocityStrings">false</xsl:param>
 
    <xsl:variable name="varVelocityDisabledString">$!disabled</xsl:variable>
    <xsl:variable name="varImagePath">../</xsl:variable>
    <!-- Definition of Global Variables - End -->
    <!-- The Root Template Match - Start -->
    <xsl:template match="/">
        
                    <xsl:call-template name="GetGreensheetFormInfo"/>
                
    </xsl:template>
    <!-- The Root Template Match - End -->
    



<!-- Template Name: GetGreensheetFormInfo; Purpose: Display the questions. -->
<xsl:template name="GetGreensheetFormInfo">
    <xsl:apply-templates select="//GreensheetQuestions"/>
</xsl:template>
    <!--
            Template Match: GreensheetQuestions
            Purpose: Matches the parent of the question groups. The <QuestionDef> elements in the root <QuestionGroup /> form the root level questions in the Form.
    -->
    <xsl:template match="GreensheetQuestions">
        <xsl:element name="Greensheets">
       
            <xsl:apply-templates select="QuestionDef[GrantTypeMechs/TypeMech/@type=$paramType and GrantTypeMechs/TypeMech/@mech=$paramMech]">
                <xsl:with-param name="pParentQuestionNumber"/>
            </xsl:apply-templates>
       
        </xsl:element>
    </xsl:template>
    <!--
            Template Match: QuestionDef
            Purpose: Displays a question in the form.
    -->
    <xsl:template match="QuestionDef">
        <xsl:param name="pParentQuestionNumber"/>
        <xsl:variable name="varIsSubQuestion">
            <xsl:choose>
                <xsl:when test="count(ancestor::QuestionDef)>0">YES</xsl:when>
                <xsl:otherwise>NO</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        
        <xsl:variable name="varParentQRespDef">     
            <xsl:choose>
                <xsl:when test="count(ancestor::QuestionDef)>0"><xsl:value-of select="../../../ResponseDef[@type!='FILE' and @type!='COMMENT']/@id"/></xsl:when>
                <xsl:otherwise>VALIDATE_TRUE</xsl:otherwise>
            </xsl:choose>   
        </xsl:variable>
       
        <xsl:variable name="varTriggerSelDefId">    
            <xsl:choose>
                <xsl:when test="count(ancestor::QuestionDef)>0"><xsl:value-of select="../@id"/></xsl:when>
                <xsl:otherwise>VALIDATE_TRUE</xsl:otherwise>
            </xsl:choose>   
        </xsl:variable>       
       
       
      
                    
                    
                        <xsl:call-template name="DisplayQuestion">
                            <xsl:with-param name="pQuestionId" select="@id"/>
                            <xsl:with-param name="pQuestionText" select="QText"/>
                            <xsl:with-param name="pQuestionInstructions" select="QInstructions"/>
                            <xsl:with-param name="pIsSubQuestion" select="$varIsSubQuestion"/>
                            <xsl:with-param name="pCommentId" select="ResponseDefsList/ResponseDef[@type='COMMENT']/@id"/>
                            <xsl:with-param name="pFileId" select="ResponseDefsList/ResponseDef[@type='FILE']/@id"/>
                            <xsl:with-param name="pParentQuestionNumber" select="$pParentQuestionNumber"/>
                            <xsl:with-param name="pParentQRespDef" select="$varParentQRespDef"/>
                            <xsl:with-param name="pTriggerSelDefId" select="$varTriggerSelDefId"/>
                        </xsl:call-template>
                        <xsl:if test="ResponseDefsList/ResponseDef/SelectionDef">
                            
                                    <xsl:call-template name="DisplaySubQuestions">
                                        <xsl:with-param name="pTriggerSelDefId" select="$varTriggerSelDefId"/>
                                        <xsl:with-param name="pParentQRespDef" select="$varParentQRespDef"></xsl:with-param>
                                        <xsl:with-param name="pQuestionId" select="@id"/>
                                        <xsl:with-param name="pParentQuestionNumber">
                                            <xsl:choose>
                                                <xsl:when test="$pParentQuestionNumber = '' ">
                                                    <xsl:value-of select="position()"/>
                                                </xsl:when>
                                                <xsl:when test="$pParentQuestionNumber != '' ">
                                                    <xsl:value-of select="$pParentQuestionNumber"/>.<xsl:value-of select="position()"/>
                                                </xsl:when>
                                            </xsl:choose>
                                    </xsl:with-param>
                                    </xsl:call-template>
                              
                        </xsl:if>
                    
    </xsl:template>
    <!--
            Template Match: DisplaySubQuestions
            Purpose: Displays the sub-questions for a parent question for a given type mech.

            style="responseId.selectionDefId(" "

            KK


    -->
<xsl:template name="DisplaySubQuestions">
        <xsl:param name="pParentQRespDef"/>
        <xsl:param name="pTriggerSelDefId"/>
        <xsl:param name="pQuestionId"/>
        <xsl:param name="pParentQuestionNumber"/>
        <xsl:for-each select="ResponseDefsList/ResponseDef/SelectionDef">
            
            <xsl:apply-templates select="QuestionDef[GrantTypeMechs/TypeMech/@type=$paramType and GrantTypeMechs/TypeMech/@mech=$paramMech]">
                            <xsl:with-param name="pParentQRespDef" select="$pParentQRespDef"/>
                            <xsl:with-param name="pParentQuestionNumber" select="$pParentQuestionNumber"/>
                            <xsl:with-param name="pTriggerSelDefId" select="$pTriggerSelDefId"/>
                    </xsl:apply-templates>
               
        </xsl:for-each>
    </xsl:template>
    <!--
            Template Name: GetCellBgColor
            Purpose: Determines the color of the table cell.
    -->
   
    <!--
            Template Name: GetQuestionNumber
            Purpose: Determines the sequence number for the Question
    -->
    <xsl:template name="GetQuestionNumber">
        <xsl:param name="pParentQuestionNumber"/>
        <xsl:choose>
            <xsl:when test="$pParentQuestionNumber = '' ">
                <xsl:value-of select="position()"/>.
            </xsl:when>
            <xsl:when test="$pParentQuestionNumber != '' ">
                <xsl:value-of select="$pParentQuestionNumber"/>.<xsl:value-of select="position()"/>.
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!--
            Template Match: DisplayQuestion
            Purpose: Actually displays the question. Also takes care of color-coding the different levels of the questions.
    -->
    <xsl:template name="DisplayQuestion">
        <xsl:param name="pQuestionId"/>
        <xsl:param name="pQuestionText"/>
        <xsl:param name="pQuestionInstructions"/>
        <xsl:param name="pIsSubQuestion"/>
        <xsl:param name="pCommentId"/>
        <xsl:param name="pFileId"/>
        <xsl:param name="pParentQuestionNumber"/>
        <xsl:param name="pParentQRespDef"/>
        <xsl:param name="pTriggerSelDefId"/>
        
        <xsl:variable name="varQuestionNumber">
            <xsl:call-template name="GetQuestionNumber">
                <xsl:with-param name="pParentQuestionNumber" select="$pParentQuestionNumber"/>
            </xsl:call-template>
        </xsl:variable>
       
               
	
				
                <xsl:call-template name="DisplaySubQuestionIcon">
                    <xsl:with-param name="pQuestionId" select="$pQuestionId"/>
                </xsl:call-template>
            
            
                <questionID>
                    <xsl:attribute name="name"><xsl:value-of select="$pQuestionId"/></xsl:attribute>
                </questionID>
                                        
            
            
           
                <xsl:call-template name="DisplayActionIcon">
                    <xsl:with-param name="pQuestionId">
                        <xsl:value-of select="$pQuestionId"/>
                    </xsl:with-param>
                </xsl:call-template>
            
    </xsl:template>
    <!--
            Template Match: DisplayCommentText
            Purpose: Displays the Text of the Comment, if any. Hidden by default. Shown when the user clicks on the Comment Icon.
    -->
 
    

    
    <!--
            Template Match: DisplayActionIcon
            Purpose: Placeholder for the Action Icon
    -->
    <xsl:template name="DisplayActionIcon">
        <xsl:param name="pQuestionId"/>
        
                    <xsl:attribute name="src"><xsl:value-of select="$varImagePath"/>images/Spacer.gif</xsl:attribute>
                    <xsl:attribute name="title"><xsl:value-of select="$pQuestionId"/></xsl:attribute>
                
    </xsl:template>
   
    <!--
            Template Match: InsertTab
            Purpose: Inserts a Tab space into the output document.
    -->
    <xsl:template name="InsertTab">
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
    </xsl:template>
    <!--
            Template Match: InsertTabSpaces
            Purpose: Inserts multiple tab spaces into the output document.
    -->
    <xsl:template name="InsertTabSpaces">
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
    </xsl:template>
    <!--
            Template Match: DisplaySubQuestionIcon
            Purpose: Displays a Subquestion Icon for  a given question and sets up the associated Javascript routines, if applicable.
    -->
    <xsl:template name="DisplaySubQuestionIcon">
        <xsl:param name="pQuestionId"/>
        <xsl:choose>
            <xsl:when test="ResponseDefsList/ResponseDef/SelectionDef/QuestionDef[GrantTypeMechs/TypeMech/@type=$paramType and GrantTypeMechs/TypeMech/@mech=$paramMech]">
              
                       
                   
            </xsl:when>
            <xsl:otherwise>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!--
            Template Match: DisplayResponseDef
            Purpose: Entry template for displaying all the Response Definitions. Prepends the text "DISPLAY_" to the Response Type and invokes the appropriate template.
    -->
    <xsl:template name="DisplayResponseDef">
        <xsl:param name="pQuestionId"/>
        <xsl:param name="pResponseDefType"/>
        <xsl:param name="pIsSubQuestion"/>
        <xsl:param name="pParentQRespDef"/>
        <xsl:param name="pTriggerSelDefId"/>
        <xsl:variable name="varResponseTypeValue">
            <xsl:value-of select="concat('DISPLAY_', $pResponseDefType)"/>
        </xsl:variable>
        <xsl:variable name="varResponseDefId">
            <xsl:value-of select="@id"/>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$varResponseTypeValue = 'DISPLAY_DATE' ">
                
            </xsl:when>
            <xsl:when test="$varResponseTypeValue= 'DISPLAY_TEXT' ">
               
            </xsl:when>
            <xsl:when test="$varResponseTypeValue= 'DISPLAY_NUMBER' ">
                
            </xsl:when>
            <xsl:when test="$varResponseTypeValue= 'DISPLAY_STRING' ">
                
            </xsl:when>
            <xsl:when test="$varResponseTypeValue= 'DISPLAY_RADIO' ">
               
            </xsl:when>
            <xsl:when test="$varResponseTypeValue= 'DISPLAY_DROP_DOWN' ">
               
            </xsl:when>
            <xsl:when test="$varResponseTypeValue= 'DISPLAY_MULTI_SELECT' ">
                
            </xsl:when>
            <xsl:when test="$varResponseTypeValue= 'DISPLAY_CHECK_BOX' ">
               
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    
    <!-- End of Template Match for a ResponseType-->
</xsl:stylesheet>
