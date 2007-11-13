<?xml-stylesheet type="text/xsl"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" indent="yes"/>
    <!-- Define Global Parameters - Start -->
    <!--
        Parameter Name: paramType
        Purpose: Defines the Type of the Form being generated
    -->
    <xsl:param name="paramType"></xsl:param>
    <!--
        Parameter Name: paramMech
        Purpose: Defines the 'mech' of the Form being generated
    -->
    <xsl:param name="paramMech"></xsl:param>
    <!--
        Parameter Name: paramValidation
        Purpose: Sets if validation should be required for questions
    -->
        <xsl:param name="paramValidation">true</xsl:param>
    <!--
        Parameter Name: paramGenerateVelocityStrings
        Purpose: Generates the Velocity-Engine specific strings in the output document.
    -->
    <xsl:param name="paramGenerateVelocityStrings"></xsl:param>
    <!-- Define Global Parameters - End -->
    <!-- Definition of Global Variables - Start -->
    <!--
        Parameter Name: varVelocityDisabledString
        Purpose: Denotes the velocity engine-specific tag for the attribute "disabled".
    -->
    <xsl:variable name="varVelocityDisabledString">$!disabled</xsl:variable>
    <xsl:variable name="varImagePath">../</xsl:variable>
    <!-- Definition of Global Variables - End -->
    <!-- The Root Template Match - Start -->
    <xsl:template match="/">
        <html>
            <xsl:call-template name="GetHeadInfo"/>
            <body>
                <xsl:call-template name="GetBodyInfo"/>
                <xsl:call-template name="PrintGreensheetFormHtml"/>

                <form action="/greensheets/savesubmit.do" method="post" name="GreensheetForm" id="GreensheetForm">
                    <input type="hidden" name="method" id="method" value=""/>
                    <input type="hidden" name="POC" id="POC" value=""/>
                    <input type="hidden" name="FORM_UID" value="$!FORM_UID"/>
                    <xsl:call-template name="GetGreensheetFormInfo"/>
                </form>

                <xsl:call-template name="InsertCalendarElementHtml"/>
            </body>
        </html>
    </xsl:template>
    <!-- The Root Template Match - End -->
    <!--
            Template Name : InsertCalendarElementHtml
            Purpose : Implementation of the new Calendar Control. Requires the following text to be present in the HTML at the bottom of the page.
    -->
    <xsl:template name="InsertCalendarElementHtml">
        <!--  PopCalendar(tag name and id must match) Tags should sit at the page bottom -->        
        <iframe width="152" height="163" name="gToday:outlook:agenda.js" id="gToday:outlook:agenda.js" src="./html/ipopeng.htm" scrolling="no" frameborder="0" style="visibility:visible; z-index:999; position:absolute; left:-500px; top:0px;">
            <LAYER name="gToday:outlook:agenda.js" src="./html/npopeng.htm"></LAYER>
        </iframe>
        
        
    </xsl:template>
    <!--
            Template Name : GetHeadInfo
            Purpose: To put in the tags <meta>, <link> and <script>
    -->
    <xsl:template name="GetHeadInfo">
        <head>
            <title>
                <xsl:text>Greensheets : </xsl:text>
                <xsl:choose>
                    <xsl:when test="$paramGenerateVelocityStrings = 'false' ">Grant Number Goes Here...</xsl:when>
                    <xsl:when test="$paramGenerateVelocityStrings = 'true' ">$!CURRENT_USER_SESSION.getFormSessionGrant($!FORM_UID).getFullGrantNumber() </xsl:when>
                </xsl:choose>
            </title>
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
            <link rel="stylesheet" type="text/css">
                <xsl:attribute name="href"><xsl:value-of select="$varImagePath"/>stylesheets/GreensheetsStyleSheet.css</xsl:attribute>
            </link>
            <script language="javascript">
                var imgSrcIconCollapsed             =   "<xsl:value-of select="$varImagePath"/>images/IconCollapsed.gif";
                var imgSrcIconExpanded              =   "<xsl:value-of select="$varImagePath"/>images/IconExpanded.gif";
                var imgCommentIconChecked           =   "<xsl:value-of select="$varImagePath"/>images/IconCommentChecked.gif";
                var imgCommentIconUnchecked     =   "<xsl:value-of select="$varImagePath"/>images/IconComment.gif";
            </script>
            <script language="javascript">
                <xsl:attribute name="src"><xsl:value-of select="$varImagePath"/>scripts/ClientSideMethods.js</xsl:attribute>
            </script>
            <script language="javascript">
                <xsl:attribute name="src"><xsl:value-of select="$varImagePath"/>scripts/ClientValidation.js</xsl:attribute>
            </script>
            <script language="javascript">
                <xsl:attribute name="src"><xsl:value-of select="$varImagePath"/>scripts/ClientMethods.js</xsl:attribute>
            </script>
        </head>
    </xsl:template>
    <!--
            Template Name: GetBodyInfo
            Purpose: Set up the body headers, tables etc.

    -->
    <!-- Start displaying the Label -->
    <!-- Start Greensheet Header -->
    <xsl:template name="GetBodyInfo">








<!--Start Dialog Header -->
<table cellspacing="0" width="100%" class="header">
    <tr class="topRow">
        <td class="logo"><a href="#"><img src="../images/LogoGreensheets.gif" alt="Greensheets logo" border="0" /></a></td>
        <td class="align1 align4">
                    <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                    <!-- build number -->
                </td>
    </tr>
    <tr class="bottomRow">
        <td class="globalNav">
            <a href="javascript:printGreensheetForm('$!FORM_UID','$!CURRENT_USER_SESSION.getGreensheetFormSession($!FORM_UID).getFormTitle()','$!CURRENT_USER_SESSION.getFormSessionGrant($!FORM_UID).getFullGrantNumber()');">Print</a>
        </td>
        <td class="align1">
            <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
        </td>
    </tr>
</table>
<!--End Dialog Header -->






<!-- start info -->
<form action="" name="frmGrantInfo" id="frmGrantInfo">
<table width="100%" cellspacing="0" id="GreensheetLabel">
    <tr>
        <td colspan="3" height="5">
            <img width="10" height="5" alt="">
                <xsl:attribute name="src"><xsl:value-of select="$varImagePath"/>images/Spacer.gif</xsl:attribute>
            </img>
        </td>
    </tr>
    <tr>
        <td colspan="3">
            <span class="TitlePrimary">Form Name</span>
            <br/>
        </td>
    </tr>
    <tr>
        <td colspan="3" height="10">
            <xsl:call-template name="InsertTab"/>
        </td>
    </tr>
    <tr>
        <td width="50%"><strong>Grant Number:</strong>
        <a href="javascript:getGrantDetail('$!CURRENT_USER_SESSION.getFormSessionGrant($!FORM_UID).getFullGrantNumber()','$!CURRENT_USER_SESSION.getFormSessionGrant($!FORM_UID).getApplId()','$!grantDetailUrl');">Grant Number </a>
        </td>
        <td>
            <img width="20" height="10" alt="On Control::+ $!CURRENT_USER_SESSION.getFormSessionGrant($!FORM_UID).isOnControl()">
                <xsl:attribute name="src"><xsl:value-of select="$varImagePath"/>images/Spacer.gif</xsl:attribute>
            </img>
        </td>
        <td width="50%"><strong>PI:</strong> PI</td>
    </tr>
    <tr>
        <td width="50%"><strong>Submitter:</strong> Submitted By</td>
        <td>
            <xsl:call-template name="InsertTab"/>
        </td>
        <td width="50%"><strong>Last Changed By:</strong> Changed By</td>
    </tr>
    <tr>
        <td width="50%"><strong>Institution:</strong>Institution Name</td>
        <td>
            <xsl:call-template name="InsertTab"/>
        </td>
        <td width="50%"><strong>Program Director:</strong> PD Name</td>
    </tr>
    <tr>
        <td width="50%"><strong>Primary Specialist:</strong> Prime Specialist</td>
        <td>
            <xsl:call-template name="InsertTab"/>
        </td>
        <td width="50%"><strong>Backup Specialist:</strong> Backup Specialist</td>
    </tr>
    <xsl:if test="$paramGenerateVelocityStrings = 'true' ">
        <xsl:text>#if($displayGmsAndPdCode)</xsl:text>
    </xsl:if>
    <tr>
        <td width="50%"><strong>Specialist Code:</strong>SpecialistCode </td>
        <td>
            <xsl:call-template name="InsertTab"/>
        </td>
        <td width="50%"><strong>Program Director Code:</strong> Pd Code</td>
    </tr>
    <xsl:if test="$paramGenerateVelocityStrings = 'true' ">
        <xsl:text>#end</xsl:text>
    </xsl:if>
    
    <tr>
        <td width="50%"><strong>Status:</strong>  Status </td>
        <td>
            <xsl:call-template name="InsertTab"/>
        </td>
        
        <td width="50%"><strong>Submitted Date:</strong> Submitted Date</td>
        
    </tr>
    
    <xsl:if test="$paramGenerateVelocityStrings = 'true' ">
        <xsl:text>#if ($displayPoc)</xsl:text>
    </xsl:if>
    <tr>
        <td width="50%"><strong>POC:</strong><xsl:text disable-output-escaping="yes">Point of Contact</xsl:text>
        </td>
        <td>
            <xsl:call-template name="InsertTab"/>
        </td>
        <td width="50%">
            <xsl:call-template name="InsertTab"/>
        </td>
    </tr>
    <xsl:if test="$paramGenerateVelocityStrings = 'true' ">
        <xsl:text>#end</xsl:text>
    </xsl:if>
</table>
</form>
</xsl:template>
<!-- end info -->









    <!--        Template Name: PrintGreensheetFormHtml
            Purpose: Print GreensheetForm menu options
    -->
    <xsl:template name="PrintGreensheetFormHtml">
        <form name="PrintGreensheet" action="/greensheets/pdfgeneration.do" method="post">
            <input type="hidden" name="FORM_UID" value="$!FORM_UID"/>
        </form>
    </xsl:template>








<!-- Template Name: GetGreensheetFormInfo; Purpose: Display the questions. -->
<xsl:template name="GetGreensheetFormInfo">
    <p class="align1">
        <!--
        <xsl:call-template name="ShowSubQuestionsIcon"/>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
        -->
        <xsl:call-template name="ShowAllCommentsIcon"/>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
    <!--
    </p>
    <p class="align1">
    -->
        <xsl:call-template name="DisplayClearAllButton"/>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
        <xsl:call-template name="DisplaySaveButton"/>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
        <xsl:call-template name="DisplaySubmitButton"/>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
        <xsl:call-template name="DisplayCloseButton"/>
    </p>    
    
    
    <!-- start questions -->
    <xsl:apply-templates select="//GreensheetQuestions"/>
    <!-- end questions -->
    <p class="align1">
        <xsl:call-template name="DisplayClearAllButton"/>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
        <xsl:call-template name="DisplaySaveButton"/>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
        <xsl:call-template name="DisplaySubmitButton"/>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
        <xsl:call-template name="DisplayCloseButton"/>
    </p>
        <br />
        <br />
        <br />
        <table width="100%" cellspacing="0" class="footer">
            <tr>
                <td class="align1">
                    <img src="./images/LogoNCI.gif" alt="National Cancer Institute logo" />
                </td>
            </tr>
        </table>
</xsl:template>












<!-- Template name: ShowSubQuestionsIcon; Purpose: Display the "View/Hide All SubQuestions" Icon at the top of the page. -->
<xsl:template name="ShowSubQuestionsIcon">
    <a id="a_onViewHideAllSubQuestions" name="a_onViewHideAllSubQuestions" class="button" href="javascript:onViewAllSubQuestions()">View All Subquestions</a>
</xsl:template>

<!-- Template name: ShowAllCommentsIcon; Purpose: Display the "View/Hide All Comments" Icon at the top of the page. -->
<xsl:template name="ShowAllCommentsIcon">
    <a id="a_onViewHideAllComments" name="a_onViewHideAllComments" class="button" href="javascript:onViewAllComments()">View All Comments</a>
</xsl:template>

<!-- Template name: DisplayClearAllButton; Purpose: Display the "Clear" Icon at the bottom of the page. -->
<xsl:template name="DisplayClearAllButton">
    <a class="button" href="javascript:OnClickClearAll()">Clear All Answers</a>
</xsl:template>

<!-- Template name: DisplaySaveButton; Purpose: Display the "Save" Icon at the bottom of the page. -->
<xsl:template name="DisplaySaveButton">
    <a class="button" href="javascript:saveSubmitClose('SAVE')">Save</a>
</xsl:template>

<!-- Template name: DisplaySubmitButton; Purpose: Display the "Submit" Icon at the bottom of the page. -->
<xsl:template name="DisplaySubmitButton">
    <a class="button" href="javascript:saveSubmitClose('SUBMIT')">Submit</a>
</xsl:template>

<!-- Template name: DisplayCloseButton; Purpose: Display the "Close" Icon at the bottom of the page. -->
<xsl:template name="DisplayCloseButton">
        <a class="button" href="javascript:saveSubmitClose('CLOSE')">Close</a>
</xsl:template>



    <!--
            Template Match: GreensheetQuestions
            Purpose: Matches the parent of the question groups. The <QuestionDef> elements in the root <QuestionGroup /> form the root level questions in the Form.
    -->
    <xsl:template match="GreensheetQuestions">
        <table border="0" cellpadding="2" width="100%" align="center" cellspacing="0" class="Greensheet">
            <tr valign="top">
                <td class="GreensheetCell1" nowrap="1" width="21">
                    <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                </td>
                <td colspan="5" class="GreensheetCell2">
                    <table border="0" cellspacing="0" cellpadding="0">
                        <tr align="left" valign="top">
                            <td class="TitleSecondary">
                                Greensheet Type: <xsl:value-of select="$paramType"/> &amp; Mech: <xsl:value-of select="$paramMech"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <xsl:apply-templates select="QuestionDef[GrantTypeMechs/TypeMech/@type=$paramType and GrantTypeMechs/TypeMech/@mech=$paramMech]">
                <xsl:with-param name="pParentQuestionNumber"/>
            </xsl:apply-templates>
        </table>
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
       
       
        <tr valign="top">
            <td colspan="6">
                <div div_type="question">
                    <xsl:attribute name="name">div_<xsl:value-of select="@id"/></xsl:attribute>
                    <xsl:attribute name="id">div_<xsl:value-of select="@id"/></xsl:attribute>
                    <xsl:attribute name="style">display:block</xsl:attribute>
                    <xsl:attribute name="ques_resp_def"><xsl:value-of select="ResponseDefsList/ResponseDef[@type!='FILE' and @type!='COMMENT']/@id"/></xsl:attribute>
                    <table border="0" cellspacing="0" cellpadding="0" width="100%">
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
                            <tr>
                                <td colspan="6">
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
                                </td>
                            </tr>
                        </xsl:if>
                    </table>
                </div>
            </td>
        </tr>
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
            <div div_type="subquestion">
                <xsl:attribute name="style"><xsl:choose><xsl:when test="$paramGenerateVelocityStrings = 'false' ">display:none</xsl:when><xsl:when test="$paramGenerateVelocityStrings = 'true' "><xsl:text>#if ($</xsl:text><xsl:value-of select="../@id"/><xsl:text disable-output-escaping="yes">.selectionDefId.equalsIgnoreCase('</xsl:text><xsl:value-of select="@id"/><xsl:text>')) display:block #else display:none  #end</xsl:text></xsl:when></xsl:choose></xsl:attribute>
                <xsl:attribute name="div_parent_id">div_<xsl:value-of select="$pQuestionId"/></xsl:attribute>
                <xsl:attribute name="name">div_<xsl:value-of select="@id"/></xsl:attribute>
                <xsl:attribute name="id">div_<xsl:value-of select="@id"/></xsl:attribute>
                <table border="0" cellspacing="0" cellpadding="0" width="100%">
                    <xsl:apply-templates select="QuestionDef[GrantTypeMechs/TypeMech/@type=$paramType and GrantTypeMechs/TypeMech/@mech = $paramMech]">
                            <xsl:with-param name="pParentQRespDef" select="$pParentQRespDef"/>
                            <xsl:with-param name="pParentQuestionNumber" select="$pParentQuestionNumber"/>
                            <xsl:with-param name="pTriggerSelDefId" select="$pTriggerSelDefId"/>
                    </xsl:apply-templates>
                </table>
            </div>
        </xsl:for-each>
    </xsl:template>
    <!--
            Template Name: GetCellBgColor
            Purpose: Determines the color of the table cell.
    -->
    <xsl:template name="GetCellBgColor">
        <xsl:variable name="varLevel">
            <xsl:number level="multiple"/>.</xsl:variable>
        <xsl:variable name="varOne">
            <xsl:value-of select="substring-after($varLevel, '.')"/>
        </xsl:variable>
        <xsl:variable name="varTwo">
            <xsl:value-of select="substring-after($varOne, '.')"/>
        </xsl:variable>
        <xsl:variable name="varThree">
            <xsl:value-of select="substring-after($varTwo, '.')"/>
        </xsl:variable>
        <xsl:variable name="varFour">
            <xsl:value-of select="substring-after($varThree, '.')"/>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="contains($varFour, '.')">GreensheetLevel4</xsl:when>
            <xsl:when test="contains($varThree, '.')">GreensheetLevel3</xsl:when>
            <xsl:when test="contains($varTwo, '.')">GreensheetLevel2</xsl:when>
            <xsl:when test="contains($varOne, '.')">GreensheetLevel1</xsl:when>
            <!--    <xsl:otherwise>#FFCC99</xsl:otherwise> -->
        </xsl:choose>
    </xsl:template>
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
        <xsl:variable name="varCellBgColor">
            <xsl:call-template name="GetCellBgColor"/>
        </xsl:variable>
        <xsl:variable name="varQuestionNumber">
            <xsl:call-template name="GetQuestionNumber">
                <xsl:with-param name="pParentQuestionNumber" select="$pParentQuestionNumber"/>
            </xsl:call-template>
        </xsl:variable>
        <tr class="{$varCellBgColor}">
            <td class="GreensheetCell1" nowrap="nowrap" width="21" valign="top">
                <span style="display:none">
                    <xsl:attribute name="id">span_div_error_<xsl:value-of select="ResponseDefsList/ResponseDef[@type!='FILE' and @type!='COMMENT']/@id"/></xsl:attribute>
                    <img border="0">
                        <xsl:attribute name="name">img_div_error_<xsl:value-of select="ResponseDefsList/ResponseDef[@type!='FILE' and @type!='COMMENT']/@id"/></xsl:attribute>
                        <xsl:attribute name="id">img_div_error_<xsl:value-of select="ResponseDefsList/ResponseDef[@type!='FILE' and @type!='COMMENT']/@id"/></xsl:attribute>
                        <xsl:attribute name="src"><xsl:value-of select="$varImagePath"/>images/IconError.gif</xsl:attribute>
                        <xsl:attribute name="title">Some Text</xsl:attribute>
                    </img>					
                </span>
				<span style="display:none">
                    <xsl:attribute name="id">span_div_error_<xsl:value-of select="ResponseDefsList/ResponseDef[@type ='COMMENT']/@id"/></xsl:attribute>
                    <img border="0">
                        <xsl:attribute name="name">img_div_error_<xsl:value-of select="ResponseDefsList/ResponseDef[@type='COMMENT']/@id"/></xsl:attribute>
                        <xsl:attribute name="id">img_div_error_<xsl:value-of select="ResponseDefsList/ResponseDef[@type='COMMENT']/@id"/></xsl:attribute>
                        <xsl:attribute name="src"><xsl:value-of select="$varImagePath"/>images/IconError.gif</xsl:attribute>
                        <xsl:attribute name="title">Some Text</xsl:attribute>
                    </img>				
                </span>
				
                <xsl:call-template name="DisplaySubQuestionIcon">
                    <xsl:with-param name="pQuestionId" select="$pQuestionId"/>
                </xsl:call-template>
            </td>
            <td class="GreensheetCell1" width="60%">
                <table border="0" cellspacing="0" cellpadding="0" width="100%">
                    <tr align="left" valign="top">
                        <td align="left" nowrap="nowrap" valign="top">
                            <xsl:if test="$pIsSubQuestion= 'YES' ">
                                <xsl:call-template name="InsertTabSpaces"/>
                            </xsl:if>
                            <xsl:value-of select="$varQuestionNumber"/>
                        </td>
                        <td width="10" align="right">
                            <xsl:call-template name="InsertTab"/>
                        </td>
                        <td align="left" valign="top" width="100%">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td align="left" valign="top" width="100%">
                                        <a>
                                            <xsl:attribute name="name">q<xsl:value-of select="$pQuestionId"/></xsl:attribute>
                                            <xsl:value-of select="$pQuestionText"/>
                                        </a>
                                        <xsl:if test="$pQuestionInstructions != '' ">
                                            <br/>
                                            <i>
                                                <xsl:value-of select="$pQuestionInstructions"/>
                                            </i>
                                        </xsl:if>
                                    </td>
                                    <td align="right" valign="top">
                                        <xsl:call-template name="DisplayCommentText">
                                            <xsl:with-param name="pCommentId" select="$pCommentId"/>
                                        </xsl:call-template>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td nowrap="nowrap" class="GreensheetCell1" width="16" valign="top">
                <xsl:call-template name="DisplayCommentIcon">
                    <xsl:with-param name="pCommentId" select="$pCommentId"/>
                </xsl:call-template>
            </td>
            <td nowrap="nowrap" class="GreensheetCell1" width="16" valign="top">
                <xsl:call-template name="DisplayActionIcon">
                    <xsl:with-param name="pQuestionId">
                        <xsl:value-of select="$pQuestionId"/>
                    </xsl:with-param>
                </xsl:call-template>
            </td>
            <td nowrap="nowrap" class="GreensheetCell1" width="16" valign="top">
                <xsl:call-template name="DisplayFileIcon">
                    <xsl:with-param name="pFileId" select="$pFileId"/>
                </xsl:call-template>
            </td>
            <td width="100%" align="left" nowrap="nowrap" class="GreensheetCell2" valign="top">
                <xsl:for-each select="ResponseDefsList/ResponseDef[@type != 'COMMENT' and @type != 'FILE' ]">
                    <xsl:call-template name="DisplayResponseDef">
                        <xsl:with-param name="pQuestionId" select="$pQuestionId"/>
                        <xsl:with-param name="pResponseDefType" select="@type"/>
                        <xsl:with-param name="pIsSubQuestion" select="$pIsSubQuestion"/>
                        <xsl:with-param name="pParentQRespDef" select="$pParentQRespDef"/>
                        <xsl:with-param name="pTriggerSelDefId" select="$pTriggerSelDefId"/>
                    </xsl:call-template>
                </xsl:for-each>
            </td>
        </tr>
    </xsl:template>
    <!--
            Template Match: DisplayCommentText
            Purpose: Displays the Text of the Comment, if any. Hidden by default. Shown when the user clicks on the Comment Icon.
    -->
 
    <xsl:template name="DisplayCommentText">
        <xsl:param name="pCommentId"/>
        <xsl:variable name="varVelocityString">$!<xsl:value-of select="$pCommentId"/>.answerValue</xsl:variable>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
        <div div_type="comment" style="display:none">
            <xsl:attribute name="name">div_<xsl:value-of select="$pCommentId"/></xsl:attribute>
            <xsl:attribute name="id">div_<xsl:value-of select="$pCommentId"/></xsl:attribute>

        <xsl:text disable-output-escaping="yes">&lt;textarea rows="3" cols="40" wrap="virtual" id="</xsl:text>
        <xsl:value-of select="$pCommentId"/>
        
        <xsl:text>" onblur="javascript:displayCommentIcon('</xsl:text>
        <xsl:value-of select="$pCommentId"/>
        <xsl:text>')"</xsl:text>
        <xsl:text> name="</xsl:text>
        <xsl:value-of select="$pCommentId"/>
        <xsl:text>" </xsl:text>
        <!-- removed to allow uses to scroll comments on read only forms
        <xsl:value-of select="$varVelocityDisabledString"/>
        -->
        <xsl:text>onChange="Validate(document.GreensheetForm,this,true)" </xsl:text>
        <xsl:text> performValidation="</xsl:text><xsl:value-of select="$paramValidation"/> <xsl:text>" valType="typeString" displayName="Comment" valMaxLength="4000" valMandatory="false"</xsl:text>
        <xsl:text disable-output-escaping="yes">&gt;</xsl:text>
        <xsl:value-of select="$varVelocityString"/>
        <xsl:text disable-output-escaping="yes">&lt;/textarea&gt; </xsl:text>
        </div> 
    </xsl:template>  
       

    <!--
            Template Match: DisplayCommentIcon
            Purpose: Displays an icon for the comment.
    -->
    <xsl:template name="DisplayCommentIcon">
        <xsl:param name="pCommentId"/>
        <a>
            <xsl:attribute name="href">javascript:onClickComment('div_<xsl:value-of select="$pCommentId"/>')</xsl:attribute>
            <img width="16" height="16" border="0">             
                <xsl:attribute name="src"><xsl:value-of select="$varImagePath"/>images/IconComment.gif</xsl:attribute>
                <xsl:attribute name="name">img_div_<xsl:value-of select="$pCommentId"/></xsl:attribute>
                <xsl:attribute name="id">img_div_<xsl:value-of select="$pCommentId"/></xsl:attribute>
            </img>
        </a>
    </xsl:template>
    <!--
            Template Match: DisplayActionIcon
            Purpose: Placeholder for the Action Icon
    -->
    <xsl:template name="DisplayActionIcon">
        <xsl:param name="pQuestionId"/>
        <span valign="middle">
            <a>
                <xsl:attribute name="href">javascript:DoNothing()</xsl:attribute>
                <img width="16" height="16" border="0">
                    <xsl:attribute name="src"><xsl:value-of select="$varImagePath"/>images/Spacer.gif</xsl:attribute>
                    <xsl:attribute name="title"><xsl:value-of select="$pQuestionId"/></xsl:attribute>
                </img>
            </a>
        </span>
    </xsl:template>
    <xsl:template name="DisplayFileIcon">
        <xsl:param name="pFileId"/>
        <xsl:variable name="varResponseDefId">
            <xsl:value-of select="ResponseDefsList/ResponseDef[@type = 'FILE' ]/@id"/>
        </xsl:variable>
        <span valign="middle">
            <a>
                <xsl:attribute name="href">javascript:fileAttachments('$!FORM_UID','<xsl:value-of select="$varResponseDefId"/>')</xsl:attribute>
                <img width="16" height="16" border="0"> 
                    <xsl:attribute name="src"><xsl:value-of select="$varImagePath"/>images/IconAttachment.gif</xsl:attribute>
                    
                    <xsl:attribute name="name">img_div_<xsl:value-of select="$pFileId"/></xsl:attribute>
                    <xsl:attribute name="id">img_div_<xsl:value-of select="$pFileId"/></xsl:attribute>
                    
                
                </img>
            </a>
        </span>
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
                <a>
                    <xsl:attribute name="name">a_div_<xsl:value-of select="$pQuestionId"/></xsl:attribute>
                    <xsl:attribute name="id">a_div_<xsl:value-of select="$pQuestionId"/></xsl:attribute>
                    <xsl:attribute name="href">javascript:onViewSubQuestions('div_<xsl:value-of select="$pQuestionId"/>')</xsl:attribute>
                    <img border="0">
                        <xsl:attribute name="name">img_div_<xsl:value-of select="$pQuestionId"/></xsl:attribute>
                        <xsl:attribute name="id">img_div_<xsl:value-of select="$pQuestionId"/></xsl:attribute>
                        <xsl:attribute name="src"><xsl:value-of select="$varImagePath"/>images/IconCollapsed.gif</xsl:attribute>
                        <xsl:attribute name="alt">Expand Sub Questions</xsl:attribute>
                    </img>
                </a>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
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
                <xsl:call-template name="DisplayResponseDef_TypeDate">
                    <xsl:with-param name="pResponseDefId" select="$varResponseDefId"/>
                    <xsl:with-param name="pIsSubQuestion" select="$pIsSubQuestion"/>
                    <xsl:with-param name="pParentQRespDef" select="$pParentQRespDef"/>
                    <xsl:with-param name="pTriggerSelDefId" select="$pTriggerSelDefId"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$varResponseTypeValue= 'DISPLAY_TEXT' ">
                <xsl:call-template name="DisplayResponseDef_TypeText">
                    <xsl:with-param name="pResponseDefId" select="$varResponseDefId"/>
                    <xsl:with-param name="pIsSubQuestion" select="$pIsSubQuestion"/>
                    <xsl:with-param name="pParentQRespDef" select="$pParentQRespDef"/>
                    <xsl:with-param name="pTriggerSelDefId" select="$pTriggerSelDefId"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$varResponseTypeValue= 'DISPLAY_NUMBER' ">
                <xsl:call-template name="DisplayResponseDef_TypeNumber">
                    <xsl:with-param name="pResponseDefId" select="$varResponseDefId"/>
                    <xsl:with-param name="pIsSubQuestion" select="$pIsSubQuestion"/>
                    <xsl:with-param name="pParentQRespDef" select="$pParentQRespDef"/>
                    <xsl:with-param name="pTriggerSelDefId" select="$pTriggerSelDefId"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$varResponseTypeValue= 'DISPLAY_STRING' ">
                <xsl:call-template name="DisplayResponseDef_TypeString">
                    <xsl:with-param name="pResponseDefId" select="$varResponseDefId"/>
                    <xsl:with-param name="pIsSubQuestion" select="$pIsSubQuestion"/>
                    <xsl:with-param name="pParentQRespDef" select="$pParentQRespDef"/>
                    <xsl:with-param name="pTriggerSelDefId" select="$pTriggerSelDefId"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$varResponseTypeValue= 'DISPLAY_RADIO' ">
                <xsl:call-template name="DisplayResponseDef_TypeRadio">
                    <xsl:with-param name="pQuestionId" select="$pQuestionId"/>
                    <xsl:with-param name="pResponseDefId" select="$varResponseDefId"/>
                    <xsl:with-param name="pIsSubQuestion" select="$pIsSubQuestion"/>
                    <xsl:with-param name="pParentQRespDef" select="$pParentQRespDef"/>
                    <xsl:with-param name="pTriggerSelDefId" select="$pTriggerSelDefId"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$varResponseTypeValue= 'DISPLAY_DROP_DOWN' ">
                <xsl:call-template name="DisplayResponseDef_TypeDropDown">
                    <xsl:with-param name="pQuestionId" select="$pQuestionId"/>
                    <xsl:with-param name="pResponseDefId" select="$varResponseDefId"/>
                    <xsl:with-param name="pIsSubQuestion" select="$pIsSubQuestion"/>
                    <xsl:with-param name="pParentQRespDef" select="$pParentQRespDef"/>
                    <xsl:with-param name="pTriggerSelDefId" select="$pTriggerSelDefId"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$varResponseTypeValue= 'DISPLAY_MULTI_SELECT' ">
                <xsl:call-template name="DisplayResponseDef_TypeMultiSelect">
                    <xsl:with-param name="pQuestionId" select="$pQuestionId"/>
                    <xsl:with-param name="pResponseDefId" select="$varResponseDefId"/>
                    <xsl:with-param name="pIsSubQuestion" select="$pIsSubQuestion"/>
                    <xsl:with-param name="pTriggerSelDefId" select="$pTriggerSelDefId"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$varResponseTypeValue= 'DISPLAY_CHECK_BOX' ">
                <xsl:call-template name="DisplayResponseDef_TypeCheckBox">
                    <xsl:with-param name="pResponseDefId" select="$varResponseDefId"/>
                    <xsl:with-param name="pIsSubQuestion" select="$pIsSubQuestion"/>
                    <xsl:with-param name="pParentQRespDef" select="$pParentQRespDef"/>
                    <xsl:with-param name="pTriggerSelDefId" select="$pTriggerSelDefId"/>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!--
            Template Match: GetHtmlInputString
            Purpose: Responsible for generating the HTML INPUT element string.
    -->
    <xsl:template name="GetHtmlInputString">
        <xsl:param name="pResponseDefId"/>
        <xsl:param name="pIsMandatory"/>
        <xsl:param name="pParentQRespDef"/>
        <xsl:param name="pTriggerSelDefId"/>
        <xsl:param name="pValType"/>
        <xsl:variable name="varVelocityString">$!<xsl:value-of select="$pResponseDefId"/>.answerValue</xsl:variable>
        <xsl:text>&lt;input type="text" id="</xsl:text>
        <xsl:value-of select="$pResponseDefId"/>
        <xsl:text>" name="</xsl:text>
        <xsl:value-of select="$pResponseDefId"/>
        <xsl:text>" value="</xsl:text>
        <xsl:value-of select="$varVelocityString"/>
        <xsl:text>" </xsl:text>
        <xsl:text>onChange="Validate(document.GreensheetForm,this,true)" </xsl:text>
        <xsl:value-of select="$varVelocityDisabledString"/>
        <xsl:text> performValidation="</xsl:text><xsl:value-of select="$paramValidation"/> <xsl:text>" valType="</xsl:text><xsl:value-of select='$pValType'/><xsl:text>" displayName="Question" valMandatory="</xsl:text>#if($<xsl:value-of select="$pParentQRespDef"/>.selectionDefId.equalsIgnoreCase('<xsl:value-of select="$pTriggerSelDefId"/>'))true#end#if(!$<xsl:value-of select="$pParentQRespDef"/>)false#end<xsl:text>"</xsl:text><xsl:text>/&gt;</xsl:text>
    </xsl:template>
    <!--
            Template Match: GetHtmlTextareaString
            Purpose: Responsible for generating the HTML TEXTAREA element string.
    -->
    <xsl:template name="GetHtmlTextareaString">
        <xsl:param name="pResponseDefId"/>
        <xsl:param name="pIsMandatory"/>
        <xsl:param name="pParentQRespDef"/>
        <xsl:param name="pTriggerSelDefId"/>
        <xsl:variable name="varVelocityString">$!<xsl:value-of select="$pResponseDefId"/>.answerValue</xsl:variable>
        <xsl:text>&lt;textarea rows="3" cols="40" wrap="virtual" id="</xsl:text>
        <xsl:value-of select="$pResponseDefId"/>
        <xsl:text>" name="</xsl:text>
        <xsl:value-of select="$pResponseDefId"/>
        <xsl:text>" </xsl:text>
        <xsl:text>onChange="Validate(document.GreensheetForm,this,true)" </xsl:text>
        <!--removed to allow uses to scroll comments on read only forms
        <xsl:value-of select="$varVelocityDisabledString"/>
        -->
        <xsl:text> performValidation="</xsl:text><xsl:value-of select="$paramValidation"/> <xsl:text>" valType="typeString" displayName="Question" valMaxLength="2000" valMandatory="</xsl:text>#if($<xsl:value-of select="$pParentQRespDef"/>.selectionDefId.equalsIgnoreCase('<xsl:value-of select="$pTriggerSelDefId"/>'))true#end#if(!$<xsl:value-of select="$pParentQRespDef"/>)false#end<xsl:text>"</xsl:text><xsl:text>/&gt;</xsl:text>
        <xsl:value-of select="$varVelocityString"/>
        <xsl:text>&lt;/textarea&gt; </xsl:text>
    </xsl:template>
    <!--
            Template Match: DisplayHtmlElement
            Purpose: Responsible for displaying the HTML Element String in the browser as HTML (and not text)
    -->
    <xsl:template name="DisplayHtmlElement">
        <xsl:param name="HtmlElementString"/>
        <xsl:value-of select="$HtmlElementString" disable-output-escaping="yes"/>
    </xsl:template>
    <!--
            Template Match: GetHtmlDateString
            Purpose: Responsible for generating the DATE  element string.
    -->
    <xsl:template name="GetHtmlDateString">
        <xsl:param name="pResponseDefId"/>
        <xsl:param name="pIsMandatory"/>
        <xsl:param name="pParentQRespDef"/>
        <xsl:param name="pTriggerSelDefId"/>
        <xsl:variable name="varVelocityString">$!<xsl:value-of select="$pResponseDefId"/>.answerValue</xsl:variable>
        <xsl:text>&lt;input type="text" id="</xsl:text>
        <xsl:value-of select="$pResponseDefId"/>
        <xsl:text>" name="</xsl:text>
        <xsl:value-of select="$pResponseDefId"/>
        <xsl:text>" value="</xsl:text>
        <xsl:value-of select="$varVelocityString"/>
        <xsl:text>" size="12" </xsl:text>
        <xsl:text>onBlur="Validate(document.GreensheetForm,this,true)" </xsl:text>
        <xsl:value-of select="$varVelocityDisabledString"/>
        <xsl:text> performValidation="</xsl:text><xsl:value-of select="$paramValidation"/> <xsl:text>"  valType="typeDate" displayName="Question" valMandatory="</xsl:text>#if($<xsl:value-of select="$pParentQRespDef"/>.selectionDefId.equalsIgnoreCase('<xsl:value-of select="$pTriggerSelDefId"/>'))true#end#if(!$<xsl:value-of select="$pParentQRespDef"/>)false#end<xsl:text>"</xsl:text><xsl:text>/&gt;</xsl:text>
    </xsl:template>
    <!--
            Template Match: DisplayResponseDef_TypeDate
            Purpose: Displays a Date field in the HTML.
    -->
    <xsl:template name="DisplayResponseDef_TypeDate">
        <xsl:param name="pResponseDefId"/>
        <xsl:param name="pIsSubQuestion"/>
        <xsl:param name="pParentQRespDef"/>
        <xsl:param name="pTriggerSelDefId"/>
        <xsl:variable name="isMandatory">
            <xsl:choose>
                <xsl:when test="$pIsSubQuestion='YES'">false</xsl:when>
                <xsl:when test="$pIsSubQuestion='NO'">true</xsl:when>
            </xsl:choose>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$paramGenerateVelocityStrings = 'false' ">
                <input type="text" id="{$pResponseDefId}" name="{$pResponseDefId}" value="" size="10"/>
                <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
            </xsl:when>
            <xsl:when test="$paramGenerateVelocityStrings = 'true' ">
                <xsl:variable name="varHtmlString">
                    <xsl:call-template name="GetHtmlDateString">
                        <xsl:with-param name="pResponseDefId">
                            <xsl:value-of select="$pResponseDefId"/>
                        </xsl:with-param>
                        <xsl:with-param name="pIsMandatory" select="$isMandatory"/>
                        <xsl:with-param name="pParentQRespDef" select="$pParentQRespDef"/>
                        <xsl:with-param name="pTriggerSelDefId" select="$pTriggerSelDefId"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:call-template name="DisplayHtmlElement">
                    <xsl:with-param name="HtmlElementString">
                        <xsl:value-of select="$varHtmlString"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
        <a href="javascript:void(0)">
            <xsl:attribute name="OnClick">gfPop.fPopCalendar(document.GreensheetForm.<xsl:value-of select="$pResponseDefId"/>);document.GreensheetForm.<xsl:value-of select="$pResponseDefId"/>.focus();return false;</xsl:attribute>
            <img width="16" height="15" border="0" alt="">
                <xsl:attribute name="src"><xsl:value-of select="$varImagePath"/>images/IconCalendar.gif</xsl:attribute>
            </img>
        </a>
        <span class="CalloutText">(mm/dd/yyyy)</span>
    </xsl:template>
    <!--
            Template Match: DisplayResponseDef_TypeText
            Purpose: Displays a Textarea element in the HTML.
    -->
    <xsl:template name="DisplayResponseDef_TypeText">
        <xsl:param name="pResponseDefId"/>
        <xsl:param name="pIsSubQuestion"/>
        <xsl:param name="pParentQRespDef"/>
        <xsl:param name="pTriggerSelDefId"/>
        <xsl:variable name="isMandatory">
            <xsl:choose>
                <xsl:when test="$pIsSubQuestion='YES'">false</xsl:when>
                <xsl:when test="$pIsSubQuestion='NO'">true</xsl:when>
            </xsl:choose>
        </xsl:variable>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
        <xsl:choose>
            <xsl:when test="$paramGenerateVelocityStrings = 'false' ">
                <textarea id="{$pResponseDefId}" name="{$pResponseDefId}" rows="3" cols="40" wrap="virtual"/>
            </xsl:when>
            <xsl:when test="$paramGenerateVelocityStrings = 'true' ">
                <xsl:variable name="varHtmlString">
                    <xsl:call-template name="GetHtmlTextareaString">
                        <xsl:with-param name="pResponseDefId">
                            <xsl:value-of select="$pResponseDefId"/>
                        </xsl:with-param>
                        <xsl:with-param name="pIsMandatory" select="$isMandatory"/>
                        <xsl:with-param name="pParentQRespDef" select="$pParentQRespDef"/>
                        <xsl:with-param name="pTriggerSelDefId" select="$pTriggerSelDefId"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:call-template name="DisplayHtmlElement">
                    <xsl:with-param name="HtmlElementString">
                        <xsl:value-of select="$varHtmlString"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!--
            Template Match: DisplayResponseDef_TypeNumber
            Purpose: Displays a textbox in the HTML that will be validated to accept only numbers.
    -->
    <xsl:template name="DisplayResponseDef_TypeNumber">
        <xsl:param name="pResponseDefId"/>
        <xsl:param name="pIsSubQuestion"/>
        <xsl:param name="pParentQRespDef"/>
        <xsl:param name="pTriggerSelDefId"/>
        <xsl:variable name="isMandatory">
            <xsl:choose>
                <xsl:when test="$pIsSubQuestion='YES'">false</xsl:when>
                <xsl:when test="$pIsSubQuestion='NO'">true</xsl:when>
            </xsl:choose>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$paramGenerateVelocityStrings = 'false' ">
                <input type="text" id="{$pResponseDefId}" name="{$pResponseDefId}" value=""/>
            </xsl:when>
            <xsl:when test="$paramGenerateVelocityStrings = 'true' ">
                <xsl:variable name="varHtmlString">
                    <xsl:call-template name="GetHtmlInputString">
                        <xsl:with-param name="pResponseDefId">
                            <xsl:value-of select="$pResponseDefId"/>
                        </xsl:with-param>
                        <xsl:with-param name="pIsMandatory" select="$isMandatory"/>
                        <xsl:with-param name="pParentQRespDef" select="$pParentQRespDef"/>
                        <xsl:with-param name="pTriggerSelDefId" select="$pTriggerSelDefId"/>
                        <xsl:with-param name="pValType">typeNumber</xsl:with-param>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:call-template name="DisplayHtmlElement">
                    <xsl:with-param name="HtmlElementString">
                        <xsl:value-of select="$varHtmlString"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!--
            Template Match: DisplayResponseDef_TypeString
            Purpose: Displays a text field in the HTML.
    -->
    <xsl:template name="DisplayResponseDef_TypeString">
        <xsl:param name="pResponseDefId"/>
        <xsl:param name="pIsSubQuestion"/>
        <xsl:param name="pParentQRespDef"/>
        <xsl:param name="pTriggerSelDefId"/>
        <xsl:variable name="isMandatory">
            <xsl:choose>
                <xsl:when test="$pIsSubQuestion='YES'">false</xsl:when>
                <xsl:when test="$pIsSubQuestion='NO'">true</xsl:when>
            </xsl:choose>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$paramGenerateVelocityStrings = 'false' ">
                <input type="text" id="{$pResponseDefId}" name="{$pResponseDefId}" value=""/>
            </xsl:when>
            <xsl:when test="$paramGenerateVelocityStrings = 'true' ">
                <xsl:variable name="varHtmlString">
                    <xsl:call-template name="GetHtmlInputString">
                        <xsl:with-param name="pResponseDefId">
                            <xsl:value-of select="$pResponseDefId"/>
                        </xsl:with-param>
                        <xsl:with-param name="pIsMandatory" select="$isMandatory"/>
                        <xsl:with-param name="pParentQRespDef" select="$pParentQRespDef"/>
                        <xsl:with-param name="pTriggerSelDefId" select="$pTriggerSelDefId"/>
                        <xsl:with-param name="pValType">typeString</xsl:with-param>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:call-template name="DisplayHtmlElement">
                    <xsl:with-param name="HtmlElementString">
                        <xsl:value-of select="$varHtmlString"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!--
            Template Match: GetVelocityString
            Purpose: Generates the Velocity strings for RADIO, CHECKBOX, DROPDOWN and MULTISELECT
    -->
    <xsl:template name="GetVelocityString">
        <xsl:param name="pResponseDefId"/>
        <xsl:param name="pSelectionDefId"/>
        #if ($<xsl:value-of select="$pResponseDefId"/>.selectionDefId.equalsIgnoreCase("<xsl:value-of select="$pSelectionDefId"/>")) $!<xsl:value-of select="$pResponseDefId"/>.answerValue #end
    </xsl:template>
    <!--
            Template Match: GetVelocityString
            Purpose: Generates the Velocity strings for RADIO, CHECKBOX, DROPDOWN and MULTISELECT
    -->
    <xsl:template name="GetVelocityStringCheckBox">
        <xsl:param name="pResponseDefId"/>
        <xsl:param name="pSelectionDefId"/>
        #if ($<xsl:value-of select="$pResponseDefId"/>.selectionDefId.indexOf(",<xsl:value-of select="$pSelectionDefId"/>,") > -1) $!<xsl:value-of select="$pResponseDefId"/>.answerValue #end
    </xsl:template>
    <!--
            Template Match: GetHtmlRadioString
            Purpose: Responsible for generating the HTML INPUT RADIO element string.
    -->
    <xsl:template name="GetHtmlRadioString">
        <xsl:param name="pQuestionDefId"/>
        <xsl:param name="pResponseDefId"/>
        <xsl:param name="pSelectionDefId"/>
        <xsl:param name="pIsMandatory"/>
        <xsl:param name="pParentQRespDef"/>
        <xsl:param name="pTriggerSelDefId"/>
        <xsl:variable name="varVelocityString">
            <xsl:call-template name="GetVelocityString">
                <xsl:with-param name="pResponseDefId">
                    <xsl:value-of select="$pResponseDefId"/>
                </xsl:with-param>
                <xsl:with-param name="pSelectionDefId">
                    <xsl:value-of select="$pSelectionDefId"/>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:variable>
        <xsl:text>&lt;input type="radio" id="</xsl:text>
        <xsl:value-of select="$pResponseDefId"/>
        <xsl:text>" name="</xsl:text>
        <xsl:value-of select="$pResponseDefId"/>
        <xsl:text>" value="</xsl:text>
        <xsl:value-of select="$pSelectionDefId"/>
        <xsl:text>"  </xsl:text>
        <xsl:value-of select="$varVelocityDisabledString"/>
        <xsl:text/>
        <xsl:value-of select="$varVelocityString"/>
        <xsl:text/> OnClick="javascript:onViewSelectSubQuestions('div_<xsl:value-of select="$pQuestionDefId"/>', 'div_<xsl:value-of select="$pSelectionDefId"/>')"
        <xsl:text> performValidation="</xsl:text><xsl:value-of select="$paramValidation"/> <xsl:text>" valType="typeRadio" displayName="Question" valMandatory="</xsl:text>#if($<xsl:value-of select="$pParentQRespDef"/>.selectionDefId.equalsIgnoreCase('<xsl:value-of select="$pTriggerSelDefId"/>'))true#end#if(!$<xsl:value-of select="$pParentQRespDef"/>)false#end<xsl:text>"</xsl:text><xsl:text>/&gt;</xsl:text>
    </xsl:template>
    <!--
            Template Match: DisplayResponseDef_TypeRadio
            Purpose: Displays a set of Radio buttons in the output document. The id and name of the radio elements corresponds to the "ResponseDefId", whereas the value of each radio corresponds to the Selection Id.
    -->
    <xsl:template name="DisplayResponseDef_TypeRadio">
        <xsl:param name="pQuestionId"/>
        <xsl:param name="pResponseDefId"/>
        <xsl:param name="pIsSubQuestion"/>
        <xsl:param name="pParentQRespDef"/>
        <xsl:param name="pTriggerSelDefId"/>
        <xsl:variable name="isMandatory">
            <xsl:choose>
                <xsl:when test="$pIsSubQuestion='YES'">false</xsl:when>
                <xsl:when test="$pIsSubQuestion='NO'">true</xsl:when>
            </xsl:choose>
        </xsl:variable>
        <xsl:for-each select="SelectionDef">
            <xsl:value-of select="Value"/>
            <xsl:choose>
                <xsl:when test="$paramGenerateVelocityStrings = 'false' ">
                    <input type="radio" id="{$pResponseDefId}" name="{$pResponseDefId}" value="{@id}">
                        <xsl:attribute name="OnClick">javascript:onViewSelectSubQuestions('div_<xsl:value-of select="$pQuestionId"/>', 'div_<xsl:value-of select="@id"/>')</xsl:attribute>
                    </input>
                </xsl:when>
                <xsl:when test="$paramGenerateVelocityStrings = 'true' ">
                    <xsl:variable name="varHtmlString">
                        <xsl:call-template name="GetHtmlRadioString">
                            <xsl:with-param name="pQuestionDefId">
                                <xsl:value-of select="$pQuestionId"/>
                            </xsl:with-param>
                            <xsl:with-param name="pResponseDefId">
                                <xsl:value-of select="$pResponseDefId"/>
                            </xsl:with-param>
                            <xsl:with-param name="pSelectionDefId">
                                <xsl:value-of select="@id"/>
                            </xsl:with-param>
                            <xsl:with-param name="pIsMandatory" select="$isMandatory"/>
                            <xsl:with-param name="pParentQRespDef" select="$pParentQRespDef"/>
                            <xsl:with-param name="pTriggerSelDefId" select="$pTriggerSelDefId"/>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:call-template name="DisplayHtmlElement">
                        <xsl:with-param name="HtmlElementString">
                            <xsl:value-of select="$varHtmlString"/>
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:when>
            </xsl:choose>
        </xsl:for-each>
    </xsl:template>
    <!--
            Template Match: GetHtmlCheckboxString
            Purpose: Responsible for generating the HTML INPUT CHECKBOX element string.
    -->
    <xsl:template name="GetHtmlCheckboxString">
        <xsl:param name="pResponseDefId"/>
        <xsl:param name="pSelectionDefId"/>
        <xsl:param name="pIsMandatory"/>
        <xsl:param name="pParentQRespDef"/>
        <xsl:param name="pTriggerSelDefId"/>
        <xsl:variable name="varVelocityString">
            <xsl:call-template name="GetVelocityStringCheckBox">
                <xsl:with-param name="pResponseDefId">
                    <xsl:value-of select="$pResponseDefId"/>
                </xsl:with-param>
                <xsl:with-param name="pSelectionDefId">
                    <xsl:value-of select="$pSelectionDefId"/>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:variable>
        <xsl:text>&lt;input type="checkbox" id="</xsl:text>
        <xsl:value-of select="$pResponseDefId"/>
        <xsl:text>" name="</xsl:text>
        <xsl:value-of select="$pResponseDefId"/>
        <xsl:text>" value="</xsl:text>
        <xsl:value-of select="$pSelectionDefId"/>
        <xsl:text>"  </xsl:text>
        <xsl:value-of select="$varVelocityDisabledString"/>
        <xsl:text/>
        <xsl:value-of select="$varVelocityString"/>
        <xsl:text> performValidation="</xsl:text><xsl:value-of select="$paramValidation"/> <xsl:text>" valType="typeCheckBox" displayName="Question" valMandatory="</xsl:text>#if($<xsl:value-of select="$pParentQRespDef"/>.selectionDefId.equalsIgnoreCase('<xsl:value-of select="$pTriggerSelDefId"/>'))true#end#if(!$<xsl:value-of select="$pParentQRespDef"/>)false#end<xsl:text>"</xsl:text><xsl:text>/&gt;</xsl:text>
    </xsl:template>
    <!--
            Template Match: DisplayResponseDef_TypeCheckBox
            Purpose: Displays a set of CheckBoxes in the output document. The id and name of the CheckBoxes corresponds to the "ResponseDefId", whereas the value of each check box corresponds to the
                        Selection Id.
    -->
    <xsl:template name="DisplayResponseDef_TypeCheckBox">
        <xsl:param name="pResponseDefId"/>
        <xsl:param name="pIsSubQuestion"/>
        <xsl:param name="pParentQRespDef"/>
        <xsl:param name="pTriggerSelDefId"/>
        <xsl:variable name="isMandatory">
            <xsl:choose>
                <xsl:when test="$pIsSubQuestion='YES'">false</xsl:when>
                <xsl:when test="$pIsSubQuestion='NO'">true</xsl:when>
            </xsl:choose>
        </xsl:variable>
        <xsl:for-each select="SelectionDef">
            <xsl:choose>
                <xsl:when test="$paramGenerateVelocityStrings = 'false' ">
                    <input type="checkbox" id="{$pResponseDefId}" name="{$pResponseDefId}" value="{@id}"/>
                </xsl:when>
                <xsl:when test="$paramGenerateVelocityStrings = 'true' ">
                    <xsl:variable name="varHtmlString">
                        <xsl:call-template name="GetHtmlCheckboxString">
                            <xsl:with-param name="pResponseDefId">
                                <xsl:value-of select="$pResponseDefId"/>
                            </xsl:with-param>
                            <xsl:with-param name="pSelectionDefId">
                                <xsl:value-of select="@id"/>
                            </xsl:with-param>
                            <xsl:with-param name="pIsMandatory" select="$isMandatory"/>
                            <xsl:with-param name="pParentQRespDef" select="$pParentQRespDef"/>
                            <xsl:with-param name="pTriggerSelDefId" select="$pTriggerSelDefId"/>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:call-template name="DisplayHtmlElement">
                        <xsl:with-param name="HtmlElementString">
                            <xsl:value-of select="$varHtmlString"/>
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:when>
            </xsl:choose>
            <xsl:value-of select="Value"/>
            <br/>
        </xsl:for-each>
    </xsl:template>
    <!--
            Template Match: GetHtmlSelectString
            Purpose: Responsible for generating the HTML SELECT element string.
    -->
    <xsl:template name="GetHtmlSelectString">
        <xsl:param name="pQuestionDefId"/>
        <xsl:param name="pResponseDefId"/>
        <xsl:param name="pIsMandatory"/>
        <xsl:param name="pParentQRespDef"/>
        <xsl:param name="pTriggerSelDefId"/>
        <xsl:text>&lt;select id="</xsl:text>
        <xsl:value-of select="$pResponseDefId"/>
        <xsl:text>" name="</xsl:text>
        <xsl:value-of select="$pResponseDefId"/>
        <xsl:text>" </xsl:text>
        <xsl:value-of select="$varVelocityDisabledString"/>
        <xsl:text> OnChange="javascript:onSelectDropdownOption('div_</xsl:text>
        <xsl:value-of select="$pQuestionDefId"/>', '<xsl:value-of select="$pResponseDefId"/>
        <xsl:text>')"</xsl:text>
        <xsl:text> performValidation="</xsl:text><xsl:value-of select="$paramValidation"/> <xsl:text>" valType="typeString" displayName="Question" valMandatory="</xsl:text>#if($<xsl:value-of select="$pParentQRespDef"/>.selectionDefId.equalsIgnoreCase('<xsl:value-of select="$pTriggerSelDefId"/>'))true#end#if(!$<xsl:value-of select="$pParentQRespDef"/>)false#end<xsl:text>"</xsl:text><xsl:text>/&gt;</xsl:text>
        <xsl:for-each select="SelectionDef">
            <xsl:variable name="varVelocityString">
                <xsl:call-template name="GetVelocityString">
                    <xsl:with-param name="pResponseDefId">
                        <xsl:value-of select="$pResponseDefId"/>
                    </xsl:with-param>
                    <xsl:with-param name="pSelectionDefId">
                        <xsl:value-of select="@id"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:variable>
            <xsl:text>&lt;option value="</xsl:text>
            <xsl:value-of select="@id"/>
            <xsl:text>" target_selection_id="div_</xsl:text>
            <xsl:value-of select="@id"/>
            <xsl:text>" </xsl:text>
            <xsl:value-of select="$varVelocityString"/>
            <xsl:text> &gt;</xsl:text>
            <xsl:value-of select="Value"/>
            <xsl:text>&lt;/option&gt;</xsl:text>
        </xsl:for-each>
        <xsl:text>&lt;/select&gt;</xsl:text>
    </xsl:template>
    <!--
            Template Match: DisplayResponseDef_TypeDropDown
            Purpose: Displays a Dropdown box in the output document. The id and name of the dropdown corresponds to the "ResponseDefId", whereas each distinct option in the Dropdown corresponds to the
                        Selection Id.
    -->
    <xsl:template name="DisplayResponseDef_TypeDropDown">
        <xsl:param name="pQuestionId"/>
        <xsl:param name="pResponseDefId"/>
        <xsl:param name="pIsSubQuestion"/>
        <xsl:param name="pParentQRespDef"/>
        <xsl:param name="pTriggerSelDefId"/>
        <xsl:variable name="isMandatory">
            <xsl:choose>
                <xsl:when test="$pIsSubQuestion='YES'">false</xsl:when>
                <xsl:when test="$pIsSubQuestion='NO'">true</xsl:when>
            </xsl:choose>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$paramGenerateVelocityStrings = 'false' ">
                <select id="{$pResponseDefId}" name="{$pResponseDefId}">
                    <xsl:attribute name="OnChange">javascript:onSelectDropdownOption('div_<xsl:value-of select="$pQuestionId"/>', '<xsl:value-of select="$pResponseDefId"/>')</xsl:attribute>
                    <xsl:for-each select="SelectionDef">
                        <option value="{@id}">
                            <xsl:attribute name="target_selection_id">div_<xsl:value-of select="@id"/></xsl:attribute>
                            <xsl:value-of select="Value"/>
                        </option>
                    </xsl:for-each>
                </select>
            </xsl:when>
            <xsl:when test="$paramGenerateVelocityStrings = 'true' ">
                <xsl:variable name="varHtmlString">
                    <xsl:call-template name="GetHtmlSelectString">
                        <xsl:with-param name="pQuestionDefId">
                            <xsl:value-of select="$pQuestionId"/>
                        </xsl:with-param>
                        <xsl:with-param name="pResponseDefId">
                            <xsl:value-of select="$pResponseDefId"/>
                        </xsl:with-param>
                        <xsl:with-param name="pIsMandatory">
                            <xsl:value-of select="$isMandatory"/>
                        </xsl:with-param>
                        <xsl:with-param name="pParentQRespDef" select="$pParentQRespDef"/>
                        <xsl:with-param name="pTriggerSelDefId" select="$pTriggerSelDefId"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:call-template name="DisplayHtmlElement">
                    <xsl:with-param name="HtmlElementString">
                        <xsl:value-of select="$varHtmlString"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!--
            Template Match: DisplayResponseDef_TypeMultiSelect
            Purpose: Displays a List box in the output document. The id and name of the dropdown corresponds to the "ResponseDefId", whereas each distinct option in the Listbox corresponds to the
                        Selection Id.
    -->
    <xsl:template name="DisplayResponseDef_TypeMultiSelect">
        <xsl:param name="pQuestionId"/>
        <xsl:param name="pResponseDefId"/>
        <select size="5" multiple="multiple" id="{$pResponseDefId}" name="{$pResponseDefId}">
            <xsl:for-each select="SelectionDef">
                <option value="{@id}">
                    <xsl:value-of select="Value"/>
                </option>
            </xsl:for-each>
        </select>
    </xsl:template>
    <!-- End of Template Match for a ResponseType-->
</xsl:stylesheet>
