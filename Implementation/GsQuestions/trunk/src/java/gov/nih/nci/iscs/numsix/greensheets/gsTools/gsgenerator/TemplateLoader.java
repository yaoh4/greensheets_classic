package gov.nih.nci.iscs.numsix.greensheets.gsTools.gsgenerator;

import java.util.*;
import java.sql.*;

import oracle.jdbc.*;
import oracle.sql.*;
import java.io.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import org.apache.log4j.Logger;

import gov.nih.nci.iscs.numsix.greensheets.gsTools.gsgenerator.FilesysUtils;

public class TemplateLoader {

	private String indivTemplateFileName;
	private String allTypeMechsQsrcFileName;
	private String type;
	private String mech;
	private String group;
	private String url;
	private String userName;
	private String password;

	private static Logger logger = Logger.getLogger(TemplateLoader.class);

	/**
	 * Inserts into the database one record with a greensheet form template, reading data from 
	 * text files
	 * @param group  What kind of Question Source file it is - PC, SNC, etc.
	 */
	public TemplateLoader(String indivTemplateFileName, String allTypeMechsQSrcFileName, String applType,
			String mech, String group, String dbProperties) throws Exception {

		/*
		 * File f = new File(fileName); if(f.length() <=0.0 ){ throw new
		 * IllegalStateException("Error with VM File. Size is" + f.length()); }
		 * 
		 * f = new File(qSrcFileName); logger.info(f.exists() + "  " +
		 * (f.length()<= 0));
		 * 
		 * if(f.length() <= 0){ throw new
		 * IllegalStateException("Error with question XML File." +
		 * f.getCanonicalPath() + "  Size is " + f.length()); }
		 */

		this.indivTemplateFileName = indivTemplateFileName;
		this.allTypeMechsQsrcFileName = allTypeMechsQSrcFileName;
		this.type = applType;
		this.mech = mech;

		if (group.equals("PC") || group.equals("PNC") || group.equals("TEST")) {
			this.group = "PGM";
		} else if (group.equals("SNC") || group.equals("SC")) {
			this.group = "SPEC";
		} else if (group.equals("DC") || group.equals("DNC")) {
			this.group = "DM";
		}

		// logger.debug(dbProperties);
		StringTokenizer st = new StringTokenizer(dbProperties, ",");
		url = (String) st.nextToken();
		userName = (String) st.nextToken();
		password = (String) st.nextToken();

		// logger.debug("filename " + fileName + "  type " + type + "  mech " +
		// mech + "  group " + this.group); // Abdul Latheef: Uncommented for
		// debugging

	}
	
	/**
	 * A constructor that creates an essentially blank "loader" object, but takes
	 * the database properties string as a parameter so it connect to the database. 
	 * @param dbProperties
	 */
	public TemplateLoader(String dbProperties) {
		StringTokenizer st = new StringTokenizer(dbProperties, ",");
		url = (String) st.nextToken();
		userName = (String) st.nextToken();
		password = (String) st.nextToken();		
	}

	public void replaceTemplate() throws Exception {
		Connection conn = null;
		try {
			logger.info("REPLACE TEMPLATE");

			conn = getConnection();
			conn.setAutoCommit(false);
			
			String templateID = this.insertTemplate(conn);
			
			// Change the FTM_ID on the Form Grant Matrix
			String sql = "UPDATE form_grant_matrix_t set ftm_id =" + templateID
					+ " WHERE form_role_code='" + group + "' AND "
					+ "appl_type_code='" + type + "' AND activity_code='"
					+ mech + "'";

			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);

			conn.commit();

		} catch (Exception e) {
			logger.error(e);
		} finally {
			closeConnection(conn);
		}
		checkClob();
	}

	public void loadNewTemplate() throws Exception {
		Connection conn = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);
			
			String templateID = this.insertTemplate(conn);

			String insertSql2 = "INSERT INTO FORM_GRANT_MATRIX_T "
					+ "(FTM_ID,FORM_ROLE_CODE,APPL_TYPE_CODE,MAJOR_ACTIVITY_CODE,ACTIVITY_CODE) "
					+ "VALUES(?,?,?,?,?)";
			OraclePreparedStatement ops = (OraclePreparedStatement) conn
					.prepareStatement(insertSql2);
			ops.setString(1, templateID);
			ops.setString(2, group);
			ops.setString(3, type);
			ops.setString(4, mech.substring(0, 1));
			ops.setString(5, mech);

			ops.executeUpdate();

			conn.commit();

			logger.info("CLOB LOADING COMPLETE");

		} catch (Exception e) {
			logger.error(e);
		} finally {
			closeConnection(conn);
		}

	}

	private String insertTemplate(Connection conn) throws Exception {

		String template = readFile(this.indivTemplateFileName);
		String xmlTemplate = readFile(this.allTypeMechsQsrcFileName);

		String id = null;
		Statement stmt = conn.createStatement();

		// Get a new id
		ResultSet res = stmt.executeQuery("select frm_seq.nextval from dual");
		if (res.next()) {
			id = res.getString(1);
			logger.info(" >> The new template will have ID number " + id);
		}
		res.close();

		logger.info("  >>>  LOADING CLOB ...");

		String insertSql = "INSERT INTO FORM_TEMPLATES_T (ID,TEMPLATE_HTML,TEMPLATE_XML,REVISION_NUM) VALUES(?,EMPTY_CLOB(),EMPTY_CLOB(),?)";

		OraclePreparedStatement ops = (OraclePreparedStatement) conn
				.prepareStatement(insertSql);
		ops.setString(1, id);
		ops.setString(2, "1");
		ops.executeUpdate();

		String selectSql = "SELECT TEMPLATE_HTML, TEMPLATE_XML FROM FORM_TEMPLATES_T  WHERE ID="
				+ id;
		ops = (OraclePreparedStatement) conn.prepareStatement(selectSql);
		res = ops.executeQuery();

		CLOB clobHtml = null;
		CLOB clobXml = null;
		if (res.next()) {
			clobHtml = (oracle.sql.CLOB) res.getClob("TEMPLATE_HTML");
			clobXml = (oracle.sql.CLOB) res.getClob("TEMPLATE_XML");
		}
		java.io.Writer writer = ((oracle.sql.CLOB) clobHtml)
				.getCharacterOutputStream();
		writer.write(template);
		writer.flush();
		writer.close();
		logger.info("        ++ Finished loading the Velocity template");
		writer = ((oracle.sql.CLOB) clobXml).getCharacterOutputStream();
		writer.write(xmlTemplate);
		writer.flush();
		writer.close();
		logger.info("        ++ Finished loading the Question source file (XML) for the template");
		stmt.close();
		ops.close();

		return id;
	}

	private String readFile(String fileName) throws Exception {

		File f = new File(fileName);
		// long length = f.length();
		FileReader fr = new FileReader(f);

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		int i = 0;

		while (i > -1) {
			i = fr.read();
			out.write(i);
		}

		fr.close();

		String template = out.toString();
		out.close();
		return template;

	}

	private Connection getConnection() throws Exception {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		return DriverManager.getConnection(url, userName, password);
	}

	private void closeConnection(Connection conn) throws Exception {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				logger.error(e);
			}
		}

	}

	public void checkClob() throws Exception {

		Connection conn = null;

		try {

			conn = getConnection();
			Statement stmt = conn.createStatement();

			String sql = "SELECT ft.template_html FROM form_templates_t ft,form_grant_matrix_t fg "
					+ " WHERE ft.id=fg.ftm_id AND fg.form_role_code='"
					+ group
					+ "' AND "
					+ "fg.appl_type_code='"
					+ type
					+ "' AND fg.activity_code='" + mech + "'";
			;

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				java.sql.Clob clob = (java.sql.Clob) rs.getObject(1);

				ByteArrayOutputStream out = new ByteArrayOutputStream();

				Reader reader = clob.getCharacterStream();
				int i = 0;

				while (i > -1) {
					i = reader.read();
					out.write(i);
				}

				reader.close();

				String template = out.toString();
				out.close();

				String checkFileDir = FilesysUtils
						.getBaseDirectory(this.getClass().getName())
						.getParentFile().getPath();
				File f = new File(checkFileDir + "/vm/checkfile.vm");

				FileOutputStream fos = new FileOutputStream(f);
				fos.write(template.getBytes());
				fos.flush();
				fos.close();
				logger.debug(">> CHECK FILE WRITTEN: " + f.toString() + '\n');
			}

		} catch (Exception e) {
			logger.error(e);
		} finally {
			closeConnection(conn);
		}
	}

	public Document getXMLFromDB() throws Exception {
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		Document doc = null;
		try {
			conn = getConnection();
			String sqlSelect = null;

			sqlSelect = "SELECT ft.template_xml FROM form_templates_t ft,form_grant_matrix_t fg "
					+ "WHERE ft.id=fg.ftm_id AND fg.form_role_code='"
					+ group
					+ "' AND "
					+ "fg.appl_type_code='"
					+ type
					+ "' AND fg.activity_code='" + mech + "'";
			;
			// logger.debug("Getting source from db " + sqlSelect);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sqlSelect);
			while (rs.next()) {
				// int tempId = rs.getInt(1);
				java.sql.Clob clob = (java.sql.Clob) rs.getObject(1);

				if (clob == null) {
					throw new Exception("Error finding questions source");
				}

				ByteArrayOutputStream out = new ByteArrayOutputStream();

				Reader reader = clob.getCharacterStream();
				int i = 0;

				while (i > -1) {
					i = reader.read();
					out.write(i);
				}
				reader.close();
				String template = out.toString();
				template = template.substring(0,
						template.lastIndexOf("</GreensheetForm>")
								+ "</GreensheetForm>".length());
				template.trim();
				out.close();
				doc = DocumentHelper.parseText(template);
			}
		} catch (DocumentException de) {
			throw new Exception("Error parsing questions source", de);
		} catch (IOException ie) {
			throw new Exception("Error retrieving question source", ie);
		} catch (SQLException se) {
			throw new Exception("Error retrieving question source", se);

		} finally {
			try {
				if (rs != null)
					rs.close();

				if (stmt != null)
					stmt.close();

			} catch (SQLException se) {
				throw new Exception("Error generating PDF", se);
			}

			closeConnection(conn);
		}

		return doc;
	}
	
	/**
	 * @param group  What kind of Question Source file it is - PC, SNC, etc.
	 * @return  For each Type/Mech combo, the ID of is template (for the kind of form 
	 * specified by <code>group</code> - e.g., Program Competing, Specialist Non-competing, 
	 * etc.) in the database. 
	 */
	public Map<String, Long> getExistingTemplateIDs(String group) {
		Map<String, Long> existingTemplates = new HashMap<String, Long>();
		
		// Determining "form role code"
		String formRoleCode = "";
		if (group.equals("PC") || group.equals("PNC") || group.equals("TEST")) {
			formRoleCode = "PGM";
		} else if (group.equals("SNC") || group.equals("SC")) {
			formRoleCode = "SPEC";
		} else if (group.equals("DC") || group.equals("DNC")) {
			formRoleCode = "DM";
		}
		
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = getConnection();
			StringBuilder sql = new StringBuilder();
			
			sql.append("select APPL_TYPE_CODE, ACTIVITY_CODE, FTM_ID from FORM_GRANT_MATRIX_T ");
			sql.append("where FORM_ROLE_CODE = '").append(formRoleCode)
				.append("'  order by activity_code, appl_type_code");
			
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			if ( rs != null ) {
				while (rs.next()) {
					String typeMech = rs.getString("APPL_TYPE_CODE") + rs.getString("ACTIVITY_CODE");
					existingTemplates.put(typeMech, new Long(rs.getLong("FTM_ID")));
				}
			}
		}
		catch (Exception e) {
			logger.error(" !! * There was a problem with getting the list of type/mechanism combinations \n\t" 
					+ "that already have templates defined in the database: \n\t"
					+ e);
		}
		finally {
			try {
				if (rs!=null)    { rs.close(); rs = null; }
				if (stmt!=null)  { stmt.close(); stmt = null; }
				if (conn!=null)  {closeConnection(conn); }
			}
			catch (Exception e) {
				logger.warn("The following error was encountered when trying to wrap up the database operation: \n" + e);
			}
		}
		
		return existingTemplates;
	}
	
}