<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:attribute-set name="QuestionFont">
        <xsl:attribute name="font-family">serif</xsl:attribute>
        <xsl:attribute name="font-size">8</xsl:attribute>
        <xsl:attribute name="text-align">left</xsl:attribute>
    </xsl:attribute-set>
    <xsl:output method="xml" encoding="UTF-8"/>
    <xsl:variable name="varPath"/>
    <xsl:variable name="varShowComments">
        <xsl:choose>
            <xsl:when test="//AdditionalInfo/CommentOption = 'YES' ">YES</xsl:when>
            <xsl:otherwise>NO</xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    <xsl:variable name="varDisplayCommentsOnSeparatePage">
        <xsl:choose>
            <xsl:when test="//AdditionalInfo/CommentOptionSeperatePage = 'YES' ">YES</xsl:when>
            <xsl:otherwise>NO</xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    <xsl:template match="/">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="FirstPage" page-height="29.7cm" page-width="21cm" margin-top="1cm" margin-bottom="1cm" margin-left="1cm" margin-right="1cm">
                    <fo:region-body margin-top="1cm" margin-bottom="1.2cm"/>
                    <fo:region-before extent="1cm"/>
                    <fo:region-after extent="1cm"/>
                </fo:simple-page-master>
                <fo:page-sequence-master master-name="QuestionPage">
                    <fo:repeatable-page-master-alternatives>
                        <fo:conditional-page-master-reference master-reference="FirstPage" page-position="first"/>
                        <!-- recommended fallback procedure -->
                        <fo:conditional-page-master-reference master-reference="FirstPage"/>
                    </fo:repeatable-page-master-alternatives>
                </fo:page-sequence-master>
                <xsl:if test="$varShowComments = 'YES' and $varDisplayCommentsOnSeparatePage = 'YES' ">
                    <fo:page-sequence-master master-name="CommentsPage">
                        <fo:repeatable-page-master-alternatives>
                            <fo:conditional-page-master-reference master-reference="FirstPage" page-position="first"/>
                            <!-- recommended fallback procedure -->
                            <fo:conditional-page-master-reference master-reference="FirstPage"/>
                        </fo:repeatable-page-master-alternatives>
                    </fo:page-sequence-master>
                </xsl:if>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="QuestionPage">
                <!-- header -->
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block text-align="end" font-size="8pt" font-family="serif">Page <fo:page-number/> of  <fo:page-number-citation ref-id="LastPage"/>
                        <fo:block>
                            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1pt"/>
                        </fo:block>
                    </fo:block>
                </fo:static-content>
                <!-- Footer -->
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block text-align="end" font-size="8pt" font-family="serif">
                        <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1pt"/>
                        <fo:block>Page <fo:page-number/> of  <fo:page-number-citation ref-id="LastPage"/>
                        </fo:block>
                    </fo:block>
                </fo:static-content>
                <fo:flow flow-name="xsl-region-body">
                    <!-- defines text title level 1-->
                    <fo:block font-size="min(18pt,20pt)" font-family="sans-serif" line-height="max(24pt,18pt)" space-after.optimum="5 mod 3 * 7.5pt" background-color="green" color="rgb(255,255,255)" text-align="center" padding-top="0pt">
                        <xsl:value-of select="//AdditionalInfo/FormTitle"/>
                    </fo:block>
                    <xsl:call-template name="DisplayFormHeaderInfo"/>
                    <xsl:for-each select="//GreensheetForm/GreensheetQuestions">
                        <xsl:call-template name="ProcessGrouping">
                            <xsl:with-param name="IsQuestionPage">YES</xsl:with-param>
                        </xsl:call-template>
                    </xsl:for-each>
                    <!--
                    <xsl:apply-templates select="//Form/QuestionGrouping[Header = 'REMARKS']"/>
                    -->
                    <xsl:if test="$varDisplayCommentsOnSeparatePage != 'YES' or $varShowComments != 'YES' ">
                        <fo:block id="LastPage"/>
                    </xsl:if>
                </fo:flow>
            </fo:page-sequence>
            <xsl:if test="$varShowComments = 'YES' and $varDisplayCommentsOnSeparatePage = 'YES' ">
                <fo:page-sequence master-reference="CommentsPage">
                    <!-- header -->
                    <fo:static-content flow-name="xsl-region-before">  
                        <fo:block text-align="end" font-size="8pt" font-family="serif">Grant <xsl:value-of select="//AdditionalInfo/GrantNumber"/> </fo:block> 
                        <fo:block text-align="end" font-size="8pt" font-family="serif">Page <fo:page-number/> of  <fo:page-number-citation ref-id="LastPage"/>  
                            <fo:block>
                                <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1pt"/>
                            </fo:block>
                        </fo:block>                      
                    </fo:static-content>
                    <!-- Footer -->
                    <fo:static-content flow-name="xsl-region-after">
                        <fo:block text-align="end" font-size="8pt" font-family="serif">
                            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1pt"/>
                            <fo:block>Page <fo:page-number/> of  <fo:page-number-citation ref-id="LastPage"/>
                            </fo:block>
                        </fo:block>
                    </fo:static-content>
                    <fo:flow flow-name="xsl-region-body">
                        <!-- defines text title level 1-->
                        <fo:block font-size="min(18pt,20pt)" font-family="sans-serif" line-height="max(24pt,18pt)" space-after.optimum="5 mod 3 * 7.5pt" background-color="green" color="rgb(255,255,255)" text-align="center" padding-top="0pt">
                            <xsl:value-of select="//AdditionalInfo/FormTitle"/> - COMMENTS
                        </fo:block>
                        <xsl:for-each select="//GreensheetForm/GreensheetQuestions">
                            <xsl:call-template name="ProcessGrouping">
                                <xsl:with-param name="IsQuestionPage">NO</xsl:with-param>
                            </xsl:call-template>
                        </xsl:for-each>
                        <fo:block id="LastPage"/>
                    </fo:flow>
                </fo:page-sequence>
            </xsl:if>
        </fo:root>
    </xsl:template>
    <xsl:template name="DisplayFormHeaderInfo">
        <xsl:variable name="GrantNumber">
            <xsl:value-of select="//AdditionalInfo/GrantNumber"/>
        </xsl:variable>
        <xsl:variable name="PrincipalInvestigator">
            <xsl:value-of select="//AdditionalInfo/PI"/>
        </xsl:variable>
        <xsl:variable name="FormType">
            <xsl:value-of select="//AdditionalInfo/FormType"/>
        </xsl:variable>
        <xsl:variable name="Submitter">
            <xsl:value-of select="//AdditionalInfo/Submitter"/>
        </xsl:variable>
        <xsl:variable name="LastChangedBy">
            <xsl:value-of select="//AdditionalInfo/LastChangedBy"/>
        </xsl:variable>
        <xsl:variable name="Institution">
            <xsl:value-of select="//AdditionalInfo/Institution"/>
        </xsl:variable>
        <xsl:variable name="ProgramDirector">
            <xsl:value-of select="//AdditionalInfo/ProgramDirector"/>
        </xsl:variable>
        <xsl:variable name="PrimarySpecialist">
            <xsl:value-of select="//AdditionalInfo/PrimarySpecialist"/>
        </xsl:variable>
        <xsl:variable name="BackupSpecialist">
            <xsl:value-of select="//AdditionalInfo/BackUpSpecialist"/>
        </xsl:variable>
        <xsl:variable name="SpecialistCode">
            <xsl:value-of select="//AdditionalInfo/SpecialistCode"/>
        </xsl:variable>
        <xsl:variable name="ProgramDirectorCode">
            <xsl:value-of select="//AdditionalInfo/ProgramDirectorCode"/>
        </xsl:variable>
        <xsl:variable name="Status">
            <xsl:value-of select="//AdditionalInfo/Status"/>
        </xsl:variable>
        <xsl:variable name="PointOfContact">
            <xsl:value-of select="//AdditionalInfo/POC"/>
        </xsl:variable>
        
         <xsl:variable name="SubmittedDate">
            <xsl:value-of select="//AdditionalInfo/SubmittedDate"/>
        </xsl:variable>        
        <fo:block space-after.optimum="5 mod 3 * 7.5pt">
            <fo:table border="0.5pt solid black" text-align="left" width="19cm" table-layout="fixed">
                <fo:table-column column-width="9.5cm"/>
                <fo:table-column column-width="9.5cm"/>
                <fo:table-body>
                    <xsl:call-template name="DisplayHeaderInfoInColumns">
                        <xsl:with-param name="FirstColumnText">Grant Number</xsl:with-param>
                        <xsl:with-param name="FirstColumnValue">
                            <xsl:value-of select="$GrantNumber"/>
                        </xsl:with-param>
                        <xsl:with-param name="SecondColumnText">PI</xsl:with-param>
                        <xsl:with-param name="SecondColumnValue">
                            <xsl:value-of select="$PrincipalInvestigator"/>
                        </xsl:with-param>
                    </xsl:call-template>
                    <xsl:call-template name="DisplayHeaderInfoInColumns">
                        <xsl:with-param name="FirstColumnText">Submitter</xsl:with-param>
                        <xsl:with-param name="FirstColumnValue">
                            <xsl:value-of select="$Submitter"/>
                        </xsl:with-param>
                        <xsl:with-param name="SecondColumnText">Last Changed By</xsl:with-param>
                        <xsl:with-param name="SecondColumnValue">
                            <xsl:value-of select="$LastChangedBy"/>
                        </xsl:with-param>
                    </xsl:call-template>
                    <xsl:call-template name="DisplayHeaderInfoInColumns">
                        <xsl:with-param name="FirstColumnText">Institution</xsl:with-param>
                        <xsl:with-param name="FirstColumnValue">
                            <xsl:value-of select="$Institution"/>
                        </xsl:with-param>
                        <xsl:with-param name="SecondColumnText">Program Director</xsl:with-param>
                        <xsl:with-param name="SecondColumnValue">
                            <xsl:value-of select="$ProgramDirector"/>
                        </xsl:with-param>
                    </xsl:call-template>
                    <xsl:call-template name="DisplayHeaderInfoInColumns">
                        <xsl:with-param name="FirstColumnText">Primary Specialist</xsl:with-param>
                        <xsl:with-param name="FirstColumnValue">
                            <xsl:value-of select="$PrimarySpecialist"/>
                        </xsl:with-param>
                        <xsl:with-param name="SecondColumnText">Backup Specialist</xsl:with-param>
                        <xsl:with-param name="SecondColumnValue">
                            <xsl:value-of select="$BackupSpecialist"/>
                        </xsl:with-param>
                    </xsl:call-template>
                    <xsl:if test="$FormType = 'SPECIALIST' ">
                        <xsl:call-template name="DisplayHeaderInfoInColumns">
                            <xsl:with-param name="FirstColumnText">Specialist Code</xsl:with-param>
                            <xsl:with-param name="FirstColumnValue">
                                <xsl:value-of select="$SpecialistCode"/>
                            </xsl:with-param>
                            <xsl:with-param name="SecondColumnText">Program Director Code</xsl:with-param>
                            <xsl:with-param name="SecondColumnValue">
                                <xsl:value-of select="$ProgramDirectorCode"/>
                            </xsl:with-param>
                        </xsl:call-template>
                    </xsl:if>
                    <xsl:call-template name="DisplayHeaderInfoInColumns">
                        <xsl:with-param name="FirstColumnText">Status</xsl:with-param>
                        <xsl:with-param name="FirstColumnValue">
                            <xsl:value-of select="$Status"/>
                        </xsl:with-param>

                         <xsl:with-param name="SecondColumnText">Date Submitted</xsl:with-param>
                        <xsl:with-param name="SecondColumnValue">
                            <xsl:value-of select="$SubmittedDate"/>
                        </xsl:with-param> 
                                            
                    </xsl:call-template>
                    
                    <xsl:call-template name="DisplayHeaderInfoInColumns">
                    	 <xsl:with-param name="FirstColumnText">
                            <xsl:choose>
                                <xsl:when test="$FormType = 'PROGRAM' ">POC</xsl:when>
                                <xsl:otherwise/>
                            </xsl:choose>
                        </xsl:with-param>
                        <xsl:with-param name="FirstColumnValue">
                            <xsl:choose>
                                <xsl:when test="$FormType = 'PROGRAM' ">
                                    <xsl:value-of select="$PointOfContact"/>
                                </xsl:when>
                                <xsl:otherwise/>
                            </xsl:choose>
                        </xsl:with-param>
                        <xsl:with-param name="SecondColumnText"></xsl:with-param>
                        <xsl:with-param name="SecondColumnValue"/>
                    </xsl:call-template>                    
                    
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>
    <xsl:template name="DisplayHeaderInfoInColumns">
        <xsl:param name="FirstColumnText"/>
        <xsl:param name="FirstColumnValue"/>
        <xsl:param name="SecondColumnText"/>
        <xsl:param name="SecondColumnValue"/>
        <xsl:variable name="varFirstColumnText">
            <xsl:if test="$FirstColumnText != '' ">
                <xsl:value-of select="$FirstColumnText"/>:
            </xsl:if>
        </xsl:variable>
        <xsl:variable name="varSecondColumnText">
            <xsl:if test="$SecondColumnText != '' ">
                <xsl:value-of select="$SecondColumnText"/>:
            </xsl:if>
        </xsl:variable>
        <fo:table-row>
            <fo:table-cell padding="0pt" border="0.5pt solid black">
                <fo:block xsl:use-attribute-sets="QuestionFont" padding="2pt">
                    <fo:inline font-weight="bold" padding="2pt">
                        <xsl:value-of select="$varFirstColumnText"/>
                    </fo:inline>
                    <fo:inline>
                        <xsl:value-of select="$FirstColumnValue"/>
                    </fo:inline>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell padding="0pt" border="0.5pt solid black">
                <fo:block xsl:use-attribute-sets="QuestionFont" padding="2pt">
                    <fo:inline font-weight="bold" padding="2pt">
                        <xsl:value-of select="$varSecondColumnText"/>
                    </fo:inline>
                    <fo:inline>
                        <xsl:value-of select="$SecondColumnValue"/>
                    </fo:inline>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>
    <xsl:template name="ProcessGrouping">
        <xsl:param name="IsQuestionPage"/>
        <fo:block space-after.optimum="5 mod 3 * 7.5pt">
            <fo:table border="0.5pt solid black" text-align="left" width="19cm" table-layout="fixed">
                <fo:table-column>
                    <xsl:attribute name="column-width"><xsl:choose><xsl:when test="$IsQuestionPage = 'YES' ">14cm</xsl:when><xsl:otherwise>11cm</xsl:otherwise></xsl:choose></xsl:attribute>
                </fo:table-column>
                <fo:table-column>
                    <xsl:attribute name="column-width"><xsl:choose><xsl:when test="$IsQuestionPage = 'YES' ">5cm</xsl:when><xsl:otherwise>8cm</xsl:otherwise></xsl:choose></xsl:attribute>
                </fo:table-column>
                <fo:table-header>
                    <fo:table-row>
                        <fo:table-cell padding="6pt" border="1pt solid green" background-color="green" number-columns-spanned="2">
                            <fo:block text-align="left" font-weight="bold">
                                <xsl:apply-templates select="Header"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-header>
                <fo:table-body>
                    <xsl:for-each select="QuestionDef">
                        <xsl:call-template name="ProcessQuestion">
                            <xsl:with-param name="IsQuestionPage">
                                <xsl:value-of select="$IsQuestionPage"/>
                            </xsl:with-param>
                        </xsl:call-template>
                    </xsl:for-each>
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>
    <xsl:template name="ProcessQuestion">
        <xsl:param name="IsQuestionPage"/>
        <xsl:variable name="NodeDepth" select="count(ancestor::*)"/>
        <xsl:variable name="IndentValue">
            <xsl:value-of select="format-number(($NodeDepth div 2 - 1) * 0.4, '#.##')"/>
        </xsl:variable>
        <xsl:variable name="IndentBy">
            <xsl:choose>
                <xsl:when test="$IndentValue != ''">
                    <xsl:value-of select="$IndentValue"/>cm</xsl:when>
                <xsl:otherwise>0cm</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <fo:table-row>
            <fo:table-cell padding="1pt" border="0.5pt solid black">
                <fo:block xsl:use-attribute-sets="QuestionFont" padding="2pt">
                    <fo:block padding="0pt">
                        <xsl:attribute name="start-indent"><xsl:value-of select="$IndentBy"/></xsl:attribute>
                        <xsl:number level="multiple"/>) <xsl:value-of select="QText"/>
                    </fo:block>
                    <xsl:if test="Instructions != '' ">
                        <fo:block font-style="italic" padding="0pt" start-indent="0.5cm">(<xsl:value-of select="QInstructions"/>)</fo:block>
                    </xsl:if>
                </fo:block>
                <xsl:if test="$varShowComments = 'YES' ">
                    <xsl:if test="$varDisplayCommentsOnSeparatePage= 'NO' ">
                        <fo:block xsl:use-attribute-sets="QuestionFont" padding="0pt" wrap-option="wrap">
                            <fo:inline font-weight="bold" font-style="italic">Comments: </fo:inline>
                            <xsl:value-of select="ResponseDefsList/ResponseDef[@type='COMMENT']/UserInput"/>
                        </fo:block>
                    </xsl:if>
                </xsl:if>
            </fo:table-cell>
            <fo:table-cell padding="1pt" border="0.5pt solid black">
                <xsl:choose>
                    <xsl:when test="$IsQuestionPage = 'YES' ">
                        <fo:block xsl:use-attribute-sets="QuestionFont" start-indent="0.5cm">
                            <xsl:call-template name="ShowResponseTypes"/>
                        </fo:block>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:if test="$varShowComments = 'YES' and $varDisplayCommentsOnSeparatePage = 'YES' ">
                            <fo:block xsl:use-attribute-sets="QuestionFont" padding="0pt" wrap-option="wrap">
                                <xsl:value-of select="ResponseDefsList/ResponseDef[@type='COMMENT']/UserInput"/>
                            </fo:block>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>
            </fo:table-cell>
        </fo:table-row>
        <xsl:choose>
            <xsl:when test="//DisplayAllQuestions='YES'">
                <xsl:for-each select="ResponseDefsList/ResponseDef/SelectionDef/QuestionDef">
                    <xsl:call-template name="ProcessQuestion">
                        <xsl:with-param name="IsQuestionPage">
                            <xsl:value-of select="$IsQuestionPage"/>
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:for-each>
            </xsl:when>
            <xsl:when test="//DisplayAllQuestions='NO'">
                <xsl:for-each select="ResponseDefsList/ResponseDef/SelectionDef/QuestionDef">

                    
                    
                    
                    <xsl:choose>
                    
                        <xsl:when test="ResponseDefsList/ResponseDef[@type='RADIO' or @type='DROP_DOWN' or @type='CHECK_BOX']/SelectionDef/UserSelected='YES'">
                            <xsl:call-template name="ProcessQuestion">      
                                <xsl:with-param name="IsQuestionPage">
                                    <xsl:value-of select="$IsQuestionPage"/>
                                </xsl:with-param>
                            </xsl:call-template>
                        </xsl:when>
                        
                        <xsl:when test="ResponseDefsList/ResponseDef[@type='NUMBER' or @type='TEXT' or @type='STRING' or @type='DATE']/UserInput !=''">
                            <xsl:call-template name="ProcessQuestion">      
                                <xsl:with-param name="IsQuestionPage">
                                    <xsl:value-of select="$IsQuestionPage"/>
                                </xsl:with-param>
                            </xsl:call-template>                        
                        
                        
                        </xsl:when>
                        
                        
                        
                    </xsl:choose>
                    
                    
                </xsl:for-each>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <xsl:template name="ShowResponseTypes">
        <xsl:for-each select="ResponseDefsList[ResponseDef/@type != 'COMMENTS' and ResponseDef/@type != 'FILE' and ResponseDef/@type != 'REMARKS']">
            <xsl:call-template name="DisplayResponseType">
                <xsl:with-param name="ResponseDef" select="ResponseDef/@type"/>
            </xsl:call-template>
        </xsl:for-each>
    </xsl:template>
    <!-- Display the question response types -->
    <xsl:template name="DisplayResponseType">
        <xsl:param name="ResponseDef"/>
        <xsl:variable name="varResponseDefId">
            <xsl:value-of select="ReponseDef/@id"/>
        </xsl:variable>
        <xsl:variable name="ResponseTypeValue">
            <xsl:choose>
                <xsl:when test="$ResponseDef = 'RADIO' ">DisplayMultipleValues</xsl:when>
                <xsl:when test="$ResponseDef = 'CHECK_BOX' ">DisplayMultipleValues</xsl:when>
                <xsl:when test="$ResponseDef = 'DROP_DOWN' ">DisplayMultipleValues</xsl:when>
                <xsl:when test="$ResponseDef = 'CHECKBOX' ">DisplayMultipleValues</xsl:when>
                <xsl:when test="$ResponseDef = 'DATE' ">DisplaySingleValue</xsl:when>
                <xsl:when test="$ResponseDef = 'TEXT' ">DisplaySingleValue</xsl:when>
                <xsl:when test="$ResponseDef = 'NUMBER' ">DisplaySingleValue</xsl:when>
                <xsl:when test="$ResponseDef = 'STRING' ">DisplaySingleValue</xsl:when>
            </xsl:choose>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$ResponseTypeValue = 'DisplayMultipleValues' ">
                <xsl:call-template name="DisplayMultipleValues"/>
            </xsl:when>
            <xsl:when test="$ResponseTypeValue = 'DisplaySingleValue' ">
                <xsl:call-template name="DisplaySingleValue"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <xsl:template name="DisplayMultipleValues">
        <xsl:for-each select="ResponseDef/SelectionDef">
            <xsl:variable name="SelectionDefValue">
                <xsl:value-of select="Value"/>
            </xsl:variable>
            <xsl:variable name="UserSelected">
                <xsl:value-of select="./UserSelected"/>
            </xsl:variable>
            <fo:block padding="1pt">
                <xsl:choose>
                    <xsl:when test="$UserSelected = 'YES'">[<fo:inline>x</fo:inline>]
                            <fo:inline padding="1pt">
                            <xsl:value-of select="$SelectionDefValue"/>
                        </fo:inline>
                    </xsl:when>
                    <xsl:otherwise>
                        <fo:block padding="1pt">
                                    [<fo:inline color="white"/>]
                                    <fo:inline padding="1pt">
                                <xsl:value-of select="$SelectionDefValue"/>
                            </fo:inline>
                        </fo:block>
                    </xsl:otherwise>
                </xsl:choose>
            </fo:block>
        </xsl:for-each>
    </xsl:template>
    <xsl:template name="DisplaySingleValue">
        <xsl:variable name="UserResponseValue">
            <xsl:value-of select="ResponseDef[@type!='COMMENT']/UserInput"/>
        </xsl:variable>
        <fo:block wrap-option="wrap">
            <xsl:value-of select="$UserResponseValue"/>
        </fo:block>
    </xsl:template>
</xsl:stylesheet>
