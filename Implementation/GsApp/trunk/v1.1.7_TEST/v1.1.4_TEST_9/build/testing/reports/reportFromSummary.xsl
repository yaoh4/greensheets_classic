<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    <!-- make a param for the project name -->
    <xsl:template match="summary">
        <a name="overview"/>
        <xsl:call-template name="header"/>
        <xsl:call-template name="headline"/>
        <h3>Test started at <xsl:value-of select="testresult[1]/@starttime"/>
        </h3>
        <xsl:call-template name="summary-table"/>
        <!-- All individual test results -->
        <xsl:apply-templates/>
        <!-- Footer & fun -->
        <xsl:call-template name="footer"/>
    </xsl:template>
    <xsl:template name="summary-table">
        <table cellpadding="6" border="1">
            <tr>
                <th>No</th>
                <th>Name</th>
            </tr>
            <xsl:apply-templates select="testresult" mode="toc"/>
        </table>
    </xsl:template>
    <xsl:template name="headline">
        <h1>Reporting for Canoo WebTest</h1>
        <H2>Test Result Overview</H2>
    </xsl:template>
    <xsl:template name="styles">
        <xsl:call-template name="report-styles"/>
    </xsl:template>
    <xsl:template name="report-styles">
        <link href="report.css" type="text/css" rel="stylesheet"/>
    </xsl:template>
    <xsl:template name="header">
        <head>
            <!-- ###################################################################### -->
            <!-- tj: reference to javascript -->
            <script src="./showHide.js"/>
            <!-- ###################################################################### -->
            <title>WebTest - Test Result Overview</title>
            <xsl:call-template name="styles"/>
        </head>
    </xsl:template>
    <xsl:template name="footer">
        <table border="0" width="100%">
            <tr>
                <td class="footer">
                    <hr/> Created using <a href="http://webtest.canoo.com/webtest">CanooWebTest</a>
                    and its reporting tools. </td>
            </tr>
        </table>
    </xsl:template>
    <!--
    Create summary table entries choosing the td-class depending on sucsessful yes/no
    and create a link to the appropriate detail section (e.g. #testspec1).
-->
    <xsl:template match="testresult" mode="toc">
        <tr>
            <td valign="top">
                <xsl:attribute name="class">
                    <xsl:if test="@successful='no'">tocred</xsl:if>
                    <xsl:if test="@successful='yes'">tocgreen</xsl:if>
                </xsl:attribute>
                <xsl:number/>
            </td>
            <td>
                <a class="toc">
                    <xsl:attribute name="href">#testspec<xsl:number/>
                    </xsl:attribute>
                    <xsl:value-of select="@testspecname"/>
                </a>
            </td>
        </tr>
    </xsl:template>
    <xsl:template match="testresult">
        <hr/>
        <CENTER>
            <a>
                <xsl:attribute name="name">testspec<xsl:number/>
                </xsl:attribute>
                <h2>Test Summary for<br/> &quot;<xsl:value-of select="@testspecname"/>&quot;</h2>
            </a> (Location: <xsl:value-of select="@location"/>) <H3> Test started at <xsl:value-of select="@starttime"/>
            </H3>
            <table cellpadding="6" border="0">
                <tr>
                    <!-- the success indicator -->
                    <xsl:if test="@successful='no'">
                        <td class="red">Test was not sucessful!</td>
                    </xsl:if>
                    <xsl:if test="@successful='yes'">
                        <td class="green">Test was sucessful!</td>
                    </xsl:if>
                </tr>
            </table>
        </CENTER>
        <!-- ###################################################################### -->
        <!-- tj: show/hide all -->
        <tr>
            <td>
                <!--
                <img onclick="showAllSubsteps()" src="./images/more.gif"/>Show all
                  -->
                </td>
              
        </tr>
        <tr>
            <td>
                 <!--
                <img onclick="hideAllSubsteps()" src="./images/less.gif"/>Hide all
                -->
                </td>
        </tr>
        <!-- ###################################################################### -->
        <xsl:apply-templates select="config"/>
        <xsl:apply-templates select="results"/>
        <a href="#overview">Back to Test Report Overview</a>
    </xsl:template>
    <!-- Individual templates -->
    <xsl:template match="config">
        <h2>Test Parameters</h2>
        <table cellpadding="1" cellspacing="1" border="0">
            <xsl:apply-templates select="parameter">
                <xsl:sort select="@name"/>
            </xsl:apply-templates>
        </table>
    </xsl:template>
    <xsl:template match="parameter">
        <tr class="params">
            <td class="paramname">
                <xsl:value-of select="@name"/>
            </td>
            <td>&quot;<b>
                    <xsl:value-of select="@value"/>
                </b>&quot;</td>
        </tr>
    </xsl:template>
    <xsl:template match="results">
        <xsl:if test="count(step) &gt; 0">
            <h2>Executed Test Steps</h2>
            <xsl:call-template name="renderStepTable"/>
        </xsl:if>
        <xsl:apply-templates select="failure"/>
        <xsl:apply-templates select="error"/>
    </xsl:template>
    <xsl:template match="step">
        <tr>
            <!-- ###################################################################### -->
            <!-- tj: create image to show/hide substeps if row contains substeps and add onclick and groupStep attribute to image -->
            <td class="light" style="border-bottom:1px inset black;">
                <table cellspacing="0" cellpadding="0" align="right">
                    <tr>
                        <td align="right" class="light">
                            <b>&#160;<xsl:number/>
                            </b>
                        </td>
                    </tr>
                    <xsl:if test="parameter[@name='stepType'][@value='group']">
                        <tr>
                            <td align="right">
                                <img>
                                    <xsl:attribute name="onclick">changeLevelOfDetail(this)</xsl:attribute>
                                    <xsl:attribute name="groupStep">true</xsl:attribute>
                                    <xsl:attribute name="src">./images/more.gif</xsl:attribute>
                                </img>
                            </td>
                        </tr>
                    </xsl:if>
                </table>
            </td>
            <!-- ###################################################################### -->
            <td>
                <table cellpadding="1">
                    <tr>
                        <td class="stepname">
                            <xsl:value-of select="parameter[@name='stepType']/@value"/>
                        </td>
                    </tr>
                    <tr>
                        <td> &quot;<b>
                                <xsl:value-of select="parameter[@name='stepId']/@value"/>
                            </b>&quot; </td>
                    </tr>
                </table>
            </td>
            <td>
                <table cellpadding="2">
                    <xsl:apply-templates select="parameter[@name!='stepType' and @name!='stepId' and @name!='resultfilename']">
                        <xsl:sort select="@name"/>
                    </xsl:apply-templates>
                </table>
                <xsl:apply-templates select="htmlparser"/>
                <!-- Write new table for contained steps (if any) -->
                <xsl:if test="count(step) &gt; 0">
                    <xsl:call-template name="renderStepTable"/>
                </xsl:if>
            </td>
            <xsl:apply-templates select="result"/>
        </tr>
    </xsl:template>
    <!-- ============================================================================================ -->
    <!-- Displays the html parsers messages -->
    <xsl:template match="htmlparser">
        <xsl:if test="count(warning) &gt; 0">
            <b>Html parsing: <xsl:value-of select="count(error)"/> errors and <xsl:value-of
                select="count(warning)"/> warnings:</b>
            <br/>
            <xsl:apply-templates mode="htmlparser"/>
        </xsl:if>
    </xsl:template>
    <!-- ============================================================================================ -->
    <!-- Displays a single html parser's message -->
    <xsl:template match="warning" mode="htmlparser">
        <span title="{@url}, line: {@line}, col: {@col}"> [<xsl:value-of select="@line"/>,
                <xsl:value-of select="@col"/>] <xsl:value-of select="text()"/>
        </span>
        <br/>
    </xsl:template>
    <!-- ============================================================================================ -->
    <!-- Displays a single html parser's message -->
    <xsl:template match="error" mode="htmlparser">
        <span style="color: red" title="{@url}, line: {@line}, col: {@col}"> [<xsl:value-of
            select="@line"/>, <xsl:value-of select="@col"/>] <xsl:value-of select="text()"/>
        </span>
        <br/>
    </xsl:template>
    <!-- ============================================================================================ -->
    <xsl:template name="renderStepTable">
        <table cellpadding="4" border="1" width="100%">
            <tr>
                <th width="30">No</th>
                <th width="200">Name</th>
                <th width="*">Parameter</th>
                <th width="50">Duration</th>
                <th width="100">Result</th>
            </tr>
            <xsl:apply-templates select="step"/>
        </table>
    </xsl:template>
    <xsl:template name="renderLinkToResult">
        <!-- called in result context-->
        <xsl:variable name="resultFile" select="../parameter[@name = 'resultfilename']"/>
        <xsl:if test="$resultFile">
            <br/>
            <a class="result" href="{$resultFile/@value}" target="_new">Resulting page</a>
        </xsl:if>
    </xsl:template>
    <xsl:template match="result">
        <xsl:if test="completed">
            <td align="right">
                <xsl:value-of select="completed/@duration"/>
            </td>
            <td align="center">
                <!-- ###################################################################### -->
                <!-- tj: set status of a step of type 'group' to 'failed' if one of its nested steps fails  -->
                <!-- tj: set status to 'ok' if step is not of type 'group' or if all substeps of a step of type 'group' have been executed successfully. -->
                <xsl:if test="preceding-sibling::*[@name='stepType'][@value='group']/following-sibling::step/result/failed">
                    <img src="./images/todo.gif"/>
                </xsl:if>
                <xsl:if test="not(preceding-sibling::*[@name='stepType'][@value='group']/following-sibling::step/result/failed)">
                    <img src="./images/ok.gif"/>
                </xsl:if>
                <!-- ###################################################################### -->
            </td>
            <td class="green"><xsl:call-template name="renderLinkToResult"/>
            </td>
        </xsl:if>
        <xsl:if test="failed">
            <td>NA</td>
            <td align="center">
            <img src="./images/todo.gif"/></td>
            <td class="red" colspan="2">
                <a class="result">
                    <xsl:attribute name="href">#error<xsl:number count="testresult"/>
                    </xsl:attribute> </a>
                <xsl:call-template name="renderLinkToResult"/>
            </td>
        </xsl:if>
        <xsl:if test="notexecuted">
            <td class="normal" colspan="2">n/a</td>
        </xsl:if>
    </xsl:template>
    <xsl:template match="failure">
        <a>
            <xsl:attribute name="name">error<xsl:number count="testresult"/>
            </xsl:attribute>
            <h2>Failure</h2>
        </a>
        <h3>Message</h3>
        <xsl:value-of select="@message"/>
        <br/>
        <br/>
    </xsl:template>
    <xsl:template match="error">
        <a>
            <xsl:attribute name="name">error<xsl:number count="testresult"/>
            </xsl:attribute>
            <h2>Error</h2>
        </a>
        <h3>Exception</h3>
        <xsl:value-of select="@exception"/>
        <h3>Exception</h3>
        <xsl:value-of select="@message"/>
        <h3>Stacktrace</h3>
        <pre>
            <xsl:value-of select="text()" disable-output-escaping="no"/>
        </pre>
    </xsl:template>
</xsl:stylesheet>
<!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios ><scenario default="yes" name="debugSelfTest" userelativepaths="no" url="file://c:\temp\webtest\debugSelfTest.xml" htmlbaseurl="" processortype="internal" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext=""/></scenarios><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
