/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */
package gov.nih.nci.iscs.numsix.greensheets.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;

/**
 * Database Utilities
 * 
 * @author kpuscas
 */
public class DbUtils {

	private DbUtils() {
	}

	/**
	 * removes duplicate single quotes used by the
	 * commons.lang.StringUtils.escapeSql method.
	 * 
	 * @param s
	 * @return
	 */
	public static String removeDupQuotes(String s) {
		while (StringUtils.contains(s, "''")) {
			s = s.replaceAll("''", "'");
		}
		return s;
	}

	/**
	 * Returns the next unique id from the provided sequence.
	 * 
	 * @param conn
	 * @param seq
	 * @return
	 * @throws SQLException
	 */
	public static int getNewRowId(Connection conn, String seq)
			throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		int id = 0;
		try {

			stmt = conn.createStatement();

			String sql = "select " + seq + " from dual";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				id = rs.getInt(1);
			}

		} catch (SQLException se) {
			throw se;
		} finally {
			try {

				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();

			} catch (SQLException se) {
				throw se;
			}

		}
		return id;
	}

}
