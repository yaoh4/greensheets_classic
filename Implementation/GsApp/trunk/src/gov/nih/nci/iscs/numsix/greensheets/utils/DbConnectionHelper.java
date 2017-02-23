/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceUtils;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;

/**
 * @author kpuscas, Number Six Software
 */
public class DbConnectionHelper {

	private static final String DB_URL = "db.url";

	private static Logger logger = Logger.getLogger(DbConnectionHelper.class);

	private static DbConnectionHelper instance = new DbConnectionHelper();
	private DataSource dc;
	

	// private OracleConnectionCacheImpl ocacheimpl;

	private static Properties props;

	private boolean initPool = false;

	/**
	 * Constructor for DbConnectionHelper.
	 */
	private DbConnectionHelper() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		dc = (DataSource) context.getBean("dataSource");
	}

	/**
	 * Method getInstance.
	 * 
	 * @return DbConnectionHelper Returns an instance of the DbConnection
	 *         Singleton.
	 */
	public static DbConnectionHelper getInstance() {
		return instance;
	}
	
	private Connection internalGetConnection() {
		Connection conn = DataSourceUtils.getConnection(dc);
		logger.debug("Get connection: " + conn);
		
		return conn;
	}

	/**
	 * Method initConfigFile.
	 * 
	 * @param properties
	 * @throws GritsException
	 *             This method initializes the OracleConnectionPool with the
	 *             given properties. It only needs to be called once - the
	 *             properties are then stored and the DbConnectionHelper
	 *             Singleton maintains them throughout the life of the object.
	 */
	public void initConfigFile(Properties properties) throws GreensheetBaseException {

		if (initPool == false) {
			props = properties;
		}
	}

	/**
	 * Method getConnection.
	 * 
	 * @return Connection
	 * @throws GreensheetBaseException
	 *             This method returns a Connection from the Pool.
	 */
	public Connection getConnection(String userID) throws GreensheetBaseException {

		Connection conn = internalGetConnection();

		try {
			String callStr = "call dbms_application_info.set_client_info(?)";
			CallableStatement cs = conn.prepareCall(callStr);
			if (userID != null) {
				cs.setString(1, userID);
			} else {
				cs.setNull(1, Types.VARCHAR);
			}
			cs.execute();

			// logger.info("Got DB Connection!");
		} catch (Exception e) {
			throw new GreensheetBaseException("error.dbconnection", e);
		}
		return conn;
	}


	/**
	 * Method getConnection.
	 * 
	 * @return Connection
	 * @throws GreensheetBaseException
	 *             This method returns a Connection from the Pool.
	 */
	public Connection getConnection() throws GreensheetBaseException {		
		return internalGetConnection();
	}

	/**
	 * Method freeConnection.
	 * 
	 * @param con
	 * @throws GritsException
	 *             This method serves as a wrapper for Connection.close(). It
	 *             simply closes the Connection and allows the
	 *             OracleConnectionPool to return the Connection to the pool.
	 */
	public void freeConnection(Connection conn) throws GreensheetBaseException {
		logger.debug("Free connection: " + conn);
		DataSourceUtils.releaseConnection(conn, dc);
	}

	public static String getDbEnvironment() {
		String env = null;
		if (props != null) {
			// env = props.getProperty("oracle.environment"); // This really
			// should be coming not from the free-standing
			// String in the properties file, but derived from the connection
			// String, always.
			if (env == null || env.equalsIgnoreCase("")) {
				String s = props.getProperty(DB_URL).toLowerCase();
				// logger.info("############# the props != null and s is " + s);
				// Abdul Latheef: Changed the DB service name
				if (s.toLowerCase().indexOf("i2esgd".toLowerCase()) > -1
						|| s.toLowerCase().indexOf("i2ed".toLowerCase()) > -1) {
					env = "DEV";
				} else if (s.toLowerCase().indexOf("i2esgt".toLowerCase()) > -1
						|| s.toLowerCase().indexOf("i2et".toLowerCase()) > -1) {
					env = "TEST";
				} else if (s.toLowerCase().indexOf("i2esgp".toLowerCase()) > -1
						|| s.toLowerCase().indexOf("i2ep".toLowerCase()) > -1) {
					env = "PROD";

				} else if (s.toLowerCase().indexOf("I2ESGS".toLowerCase()) > -1
						|| s.toLowerCase().indexOf("i2es".toLowerCase()) > -1) {
					env = "STAGE";
				}
			}
		} else {
			logger.info("############# props is null ");
			env = "U";
		}
		return env;
	}

}