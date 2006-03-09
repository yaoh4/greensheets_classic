<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html"/>

	<!-- Variable passed from ant with the creation time -->
	<xsl:param name="reporttime"/>
	<xsl:template match="/">
	
		<xsl:variable name="steps" select="summary/results/step"/>
		<xsl:variable name="failed" select="summary/results/step/failed"/>
		<xsl:variable name="passed.count" select="count($steps)-count($failed)"/>
		<xsl:variable name="failed.percent" select="count($failed) * 100 div count($steps)"/>
		<xsl:variable name="testspec.name" select="summary/@testspecname"/>
		
		<HTML>
			<HEAD>
				<LINK href="report.css" type="text/css" rel="stylesheet"/>
				<TITLE>Test Summary - <xsl:value-of select="$testspec.name"/>
				</TITLE>
			</HEAD>
			<BODY>
				<table border="0" width="100%"><!-- outerHolderTable, contains headerTable, configTable and bodyTable -->
					<tr><td>
					<table border="0" align="center"> <!-- headerTable, contains greenBarTable -->
					<tr>							
						<td>Test Summary for</td><td>&quot;<xsl:value-of select="$testspec.name"/>&quot;</td>
					</tr><tr>							
						<td>Test started at</td><td> <xsl:value-of select="summary/@starttime"/></td>
					</tr><tr>							
						<td>Report created at</td><td> <xsl:value-of select="$reporttime"/></td>
					</tr>	<tr>							
						<td><b>Steps :</b></td><td> <xsl:value-of select="count($steps)"/></td>
					</tr><tr>							
						<td><b>Passed:</b></td><td> <xsl:value-of select="$passed.count"/></td>										</tr><tr>							
						<td><b>Failed:</b></td><td> <xsl:value-of select="count($failed)"/></td>
					</tr>	<tr> <td colspan="2">
						<table width="100% " bgcolor="white" border="0"><!-- greenBarTable --><tr>
					
							<xsl:if test="count($failed) > 0">
								<td bgcolor="#FF0000">
								<xsl:attribute name="width"><xsl:value-of select="concat($failed.percent, '%')"/>										</xsl:attribute>&#160;	</td>
							</xsl:if>
							
							<xsl:if test="$passed.count > 0">
								<td bgcolor="#00FF00">&#160;</td>
							</xsl:if>
							
						</tr></table></td>					<!-- end grennbarTable -->
					</tr>	</table> 							<!-- end headerTable -->
					
					</td>
						<td valign="top" rowspan="2"> 	<!-- configTable -->
							<xsl:apply-templates select="summary/config"/>
						</td>
					</tr><tr>
						<td>										<!-- bodyTable -->
							<xsl:apply-templates select="summary/results"/>
						</td>
					</tr><tr>
						<td class="footer" colspan="2">	<!-- Footer & fun -->
							<hr/>
							Created using 
        					<a href="http://webtest.canoo.com/webtest">CanooWebTest</a>
							and its reporting tools.
						</td>
					</tr>
				</table> 										<!-- end outerHolderTable -->
			</BODY>
		</HTML>
	</xsl:template>											<!-- end Root Template -->

	<xsl:template match="config"> 						<!-- config template -->
		<h2>Test Parameters</h2>
		<table cellpadding="4" border="1">
			<tr>
				<th>Name</th>
				<th>Value</th>
			</tr>
			<xsl:apply-templates/>							<!-- inserts the config parameter template -->
		</table>
	</xsl:template>											<!-- end config template -->
	
	<xsl:template match="parameter[@name='resultfilename']">
		<tr>
			<td colspan="2">
				<a target="_new">
					<xsl:attribute name="href"><xsl:value-of select="@value"/></xsl:attribute>
					Resulting page</a>
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template match="parameter">					<!-- config parameter template, called in config context -->
		<tr><td><b>												<!-- every parameter makes a row -->
			<xsl:value-of select="@name"/>
		</b></td><td>
			&quot;<xsl:value-of select="@value"/>&quot;
		</td></tr>
	</xsl:template>											<!-- end config parameter template -->
	
	<xsl:template match="results">						<!-- bodyTable -->
		<xsl:variable name="step.list" select="step"/>
		<xsl:variable name="step.list.count" select="count($step.list)"/>
		<xsl:if test="$step.list.count > 0">			<!-- suppressed on no steps, does this make sense? -->
			<h2>Executed Test Steps</h2>
			<table cellpadding="4" border="1">
				<tr>
					<th>No</th>
					<th>Name</th>
					<th>Parameter</th>
					<th>Duration</th>
					<th>Result</th>
				</tr>
				<xsl:apply-templates select="step"/>	<!-- apply the step row template-->
			</table>
		</xsl:if>
		<xsl:apply-templates select="failure"/>
		<xsl:apply-templates select="error"/>
	</xsl:template>											<!-- end bodyTable -->
	
	<xsl:template match="step">							<!-- step row template-->
		<tr>
			<td><xsl:number/>	</td>
			<td>
				<table cellpadding="1">
					<tr>
						<td nowrap="true"><b>
								<xsl:value-of select="parameter[@name='stepType']/@value"/>
						</b></td>
					</tr><tr>
						<td nowrap="true">
						&quot;<xsl:value-of select="parameter[@name='stepId']/@value"/>&quot;</td>
					</tr>
				</table>
			</td><td>
				<table cellpadding="2">
					<font size="-">
						<xsl:apply-templates select="parameter[@name!='stepType' and @name!='stepId']"/>
					</font>
				</table>
			</td>
			<xsl:if test="completed">
				<td><xsl:value-of select="completed/@duration"/></td>
				<td class="green">Passed</td>
			</xsl:if>
			<xsl:if test="failed">
				<td class="red" colspan="2">Failed</td>
			</xsl:if>
		</tr>
	</xsl:template>											<!-- end step row template-->

	<xsl:template match="failure">
		<h2>Failure</h2>
		<h3>Message</h3>
		<xsl:value-of select="@message"/>
	</xsl:template>
	
	<xsl:template match="error">
		<h2>Error</h2>
		<h3>Exception</h3>
		<xsl:value-of select="@exception"/>
		<h3>Message</h3>
		<xsl:value-of select="@message"/>
		<h3>Stacktrace</h3>
		<PRE>
			<xsl:value-of select="text()" disable-output-escaping="no"/>
		</PRE>
	</xsl:template>
	
</xsl:stylesheet>
