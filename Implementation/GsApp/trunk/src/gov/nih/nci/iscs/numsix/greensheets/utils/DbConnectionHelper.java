/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.utils;

import gov.nih.nci.iscs.i2e.oracle.common.data.persistence.util.OracleConnectionFactory;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.sql.PooledConnection;

import oracle.jdbc.pool.OracleConnectionCacheImpl;
import oracle.jdbc.pool.OracleConnectionPoolDataSource;

import org.apache.log4j.Logger;

/**
 * @author kpuscas, Number Six Software
 */
public class DbConnectionHelper {

    private static final String DB_URL = "db.url";

    private static final String DB_USER = "db.user";

    private static final String DB_PASSWD = "db.password";

    private static Logger logger = Logger.getLogger(DbConnectionHelper.class
            .getName());

    private static DbConnectionHelper instance = new DbConnectionHelper();
    private DataSource dc; 
    private OracleConnectionPoolDataSource ocp;

    private OracleConnectionCacheImpl ocacheimpl;

    private PooledConnection pc;

    private static Properties props;

    private boolean initPool = false;

    /**
     * Constructor for DbConnectionHelper.
     */
    private DbConnectionHelper() {
    }

    /**
     * Method getInstance.
     * 
     * @return DbConnectionHelper Returns an instance of the DbConnection Singleton.
     */
    public static DbConnectionHelper getInstance() {
        return instance;
    }

   
    private Connection getTestConn() throws SQLException {

        Connection conn = DriverManager.getConnection(props
                .getProperty(DB_URL), props.getProperty(DB_USER), props
                .getProperty(DB_PASSWD));
        return conn;

    }

    /**
     * Method getConnection.
     * 
     * @return Connection
     * @throws GreensheetBaseException
     *             This method returns a Connection from the Pool.
     */
    public Connection getConnection(String userID)
            throws GreensheetBaseException {

        Connection conn = null;
        try {
           // initOracleConnectionPool(props);
            // logger.info("Getting DB Connection...");
            Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			dc = (DataSource) envCtx.lookup("jdbc/OracleDataSource");
			conn = dc.getConnection();
            // conn = pc.getConnection();
            for (int i = 0; i < 8; i++) {
                if (conn.isClosed()) {
                    conn = dc.getConnection();
                } else {
                    break;
                }
            }
        } catch (SQLException sqle) {
            String exceptionMessage = sqle.getMessage();
            if (exceptionMessage!=null) {
            	exceptionMessage = exceptionMessage.toLowerCase();
            }
            else  { exceptionMessage = ""; }
            if (exceptionMessage.contains("connection reset") || exceptionMessage.contains("connection refused") 
            			|| exceptionMessage.contains("connection closed") 
            			|| exceptionMessage.contains("closed connection")) {
                try {
                    conn = dc.getConnection();
                } catch (SQLException sqle2) {
                    throw new GreensheetBaseException("error.dbconnection", sqle2);
                }
            } else {
                throw new GreensheetBaseException("error.dbconnection", sqle);
            }
        } catch (Exception e) {
            throw new GreensheetBaseException("error.dbconnection", e);
        }

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

        Connection conn = null;
        try {
           // initOracleConnectionPool(props);
            // logger.info("Getting DB Connection...");
        	Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			dc = (DataSource) envCtx.lookup("jdbc/OracleDataSource");
			conn = dc.getConnection();
            // conn = pc.getConnection();
            for (int i = 0; i < 8; i++) {
                if (conn.isClosed()) {
                    conn = dc.getConnection();
                } else {
                    break;
                }
            }
            // logger.info("Got DB Connection!");
        } catch (SQLException sqle) {
            String exceptionMessage = sqle.getMessage();
            if (exceptionMessage!=null) {
            	exceptionMessage = exceptionMessage.toLowerCase();
            }
            else  { exceptionMessage = ""; }
            if (exceptionMessage.contains("connection reset") || exceptionMessage.contains("connection refused") 
            			|| exceptionMessage.contains("connection closed") 
            			|| exceptionMessage.contains("closed connection")) {
                try {
                    conn = dc.getConnection();
                } catch (SQLException sqle2) {
                    throw new GreensheetBaseException("error.dbconnection", sqle2);
                }
            } else {
                throw new GreensheetBaseException("error.dbconnection", sqle);
            }
        } catch (Exception e) {
            throw new GreensheetBaseException("error.dbconnection", e);
        }

        return conn;
    }

    /**
     * Method freeConnection.
     * 
     * @param con
     * @throws GritsException
     *             This method serves as a wrapper for Connection.close(). It simply closes the Connection and allows the OracleConnectionPool to
     *             return the Connection to the pool.
     */
    public void freeConnection(Connection con) throws GreensheetBaseException {

        try {
            // logger.info("Closing DB Connection...");
            if (con != null && !con.isClosed())
                con.close();
            // logger.info("DB Connection closed!");
        } catch (Exception e) {
            throw new GreensheetBaseException("error.dbconnection", e);
        }

    }

    public static String getDbEnvironment() {
        String env = null;
        if (props != null) {
            // env = props.getProperty("oracle.environment");  // This really should be coming not from the free-standing
        		// String in the properties file, but derived from the connection String, always.
            if (env == null || env.equalsIgnoreCase("")) {
                String s = props.getProperty(DB_URL).toLowerCase();
                // Abdul Latheef: Changed the DB service name
                if (s.toLowerCase().indexOf("i2esgd".toLowerCase()) > -1 || s.toLowerCase().indexOf("i2ed".toLowerCase()) > -1) {
                    env = "DEV";
                } else if (s.toLowerCase().indexOf("i2esgt".toLowerCase()) > -1 || s.toLowerCase().indexOf("i2et".toLowerCase()) > -1) {
                    env = "TEST";
                } else if (s.toLowerCase().indexOf("i2esgp".toLowerCase()) > -1 || s.toLowerCase().indexOf("i2ep".toLowerCase()) > -1) {
                    env = "PROD";
                
				} else if (s.toLowerCase().indexOf("I2ESGS".toLowerCase()) > -1 || s.toLowerCase().indexOf("i2es".toLowerCase()) > -1) {
                    env = "STAGE";
                }
            }
        } else {
            env = "U";
        }
        return env;
    }

}