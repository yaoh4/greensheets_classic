/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.utils;

import gov.nih.nci.iscs.i2e.oracle.common.data.persistence.util.*;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.*;
import java.sql.*;
import javax.sql.*;
import java.util.*;

import oracle.jdbc.pool.*;
import oracle.jdbc.*;

import org.apache.commons.lang.*;
import org.apache.log4j.*;

/**
 * 
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class DbConnectionHelper {

    private static final String ORACLE_URL = "oracle.url";

    private static final String ORACLE_USER = "oracle.user";

    private static final String ORACLE_PASSWD = "oracle.password";

    private static Logger logger = Logger.getLogger(DbConnectionHelper.class.getName());

    private static DbConnectionHelper instance = new DbConnectionHelper();

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
     * @return DbConnectionHelper
     * 
     * Returns an instance of the DbConnection Singleton.
     */
    public static DbConnectionHelper getInstance() {
        return instance;
    }

    /**
     * Method initOracleConnectionPool.
     * 
     * @param properties
     * @throws GritsException
     * 
     * This method initializes the OracleConnectionPool with the given
     * properties. It only needs to be called once - the properties are then
     * stored and the DbConnectionHelper Singleton maintains them throughout the
     * life of the object.
     */
    public void initOracleConnectionPool(Properties properties) throws GreensheetBaseException {

        if (initPool == false) {

            props = properties;

            try {
                logger.debug("Loading db properties into Connection Pool...");

                ocp = new OracleConnectionPoolDataSource();
                ocp.setURL(props.getProperty(ORACLE_URL));
                ocp.setUser(props.getProperty(ORACLE_USER));
                ocp.setPassword(props.getProperty(ORACLE_PASSWD));
                pc = ocp.getPooledConnection();

                ocacheimpl = new OracleConnectionCacheImpl(ocp);
                ocacheimpl.setMaxLimit(5);
                ocacheimpl.setCacheScheme(OracleConnectionCacheImpl.DYNAMIC_SCHEME);

                // Init GsGrant Query Mechanism
                OracleConnectionFactory.initializeConnectionFactory(ocp);

                initPool = true;

            } catch (SQLException e) {
                throw new GreensheetBaseException("error.dbconnection", e);
            } catch (Exception e) {
                throw new GreensheetBaseException("error.dbconnection", e);
            }

        }
    }

    private Connection getTestConn() throws SQLException {

        Connection conn = DriverManager.getConnection(props.getProperty(ORACLE_URL), props.getProperty(ORACLE_USER), props
                .getProperty(ORACLE_PASSWD));
        return conn;

    }

    /**
     * Method getConnection.
     * 
     * @return Connection
     * @throws GreensheetBaseException
     * 
     * This method returns a Connection from the Pool.
     */
    public Connection getConnection(String userID) throws GreensheetBaseException {

        Connection conn = null;
        try {
            initOracleConnectionPool(props);
            //logger.info("Getting DB Connection...");
            conn = ocacheimpl.getConnection();
            //conn = pc.getConnection();
            for (int i = 0; i < 8; i++) {
                if (conn.isClosed()) {
                    conn = ocacheimpl.getConnection();
                } else {
                    break;
                }
            }

            String callStr = "call dbms_application_info.set_client_info(?)";
            CallableStatement cs = conn.prepareCall(callStr);
            if (userID != null) {
                cs.setString(1, userID);
            } else {
                cs.setNull(1, Types.VARCHAR);
            }
            cs.execute();

            //logger.info("Got DB Connection!");
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
     * 
     * This method returns a Connection from the Pool.
     */
    public Connection getConnection() throws GreensheetBaseException {

        Connection conn = null;
        try {
            initOracleConnectionPool(props);
            //logger.info("Getting DB Connection...");
            conn = ocacheimpl.getConnection();
            //conn = pc.getConnection();
            for (int i = 0; i < 8; i++) {
                if (conn.isClosed()) {
                    conn = ocacheimpl.getConnection();
                } else {
                    break;
                }
            }
            //logger.info("Got DB Connection!");
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
     * 
     * This method serves as a wrapper for Connection.close(). It simply closes
     * the Connection and allows the OracleConnectionPool to return the
     * Connection to the pool.
     */
    public void freeConnection(Connection con) throws GreensheetBaseException {

        try {
            //logger.info("Closing DB Connection...");
            con.close();
            //logger.info("DB Connection closed!");
        } catch (Exception e) {
            throw new GreensheetBaseException("error.dbconnection", e);
        }

    }

    public static String getDbEnvironment() {
        String env = null;
        if (props != null) {
            env = props.getProperty("oracle.environment");
            if (env == null || env.equalsIgnoreCase("")) {
                String s = props.getProperty(ORACLE_URL).toLowerCase();
                if (s.toLowerCase().indexOf("_at".toLowerCase()) > -1) {
                    env = "TEST";
                } else if (s.toLowerCase().indexOf("_ap".toLowerCase()) > -1) {
                    env = "PROD";
                }
            }
        } else {
            env = "U";
        }
        return env;
    }

}