package gov.nih.nci.iscs.numsix.apps.docchecker.util;

import gov.nih.nci.iscs.i2e.oracle.common.data.persistence.util.OracleConnectionFactory;
import gov.nih.nci.iscs.numsix.apps.docchecker.exception.DocCheckerException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

import oracle.jdbc.pool.OracleConnectionCacheImpl;
import oracle.jdbc.pool.OracleConnectionPoolDataSource;

public class DbConnectionHelper {
    private static final String ORACLE_URL = "oracle.url";

	private static final String ORACLE_USER = "oracle.user";

	private static final String ORACLE_PASSWD = "oracle.password";

	private String applicationUser = "";

	private static final Logger logger = Logger.getLogger(DbConnectionHelper.class);

	private static DbConnectionHelper instance = new DbConnectionHelper();

	private OracleConnectionPoolDataSource ocp;

	private OracleConnectionCacheImpl ocacheimpl;

	private static Properties props;

	private boolean initPool = false;
    
    /**
	 * @return Returns the applicationUser.
	 */
    public String getApplicationUser() {
        return applicationUser;
    }
    /**
     * @param applicationUser The applicationUser to set.
     */
    public void setApplicationUser(String applicationUser) {
        this.applicationUser = applicationUser;
    }

    /**
     * Constructor for DbConnectionHelper.
     */
    private DbConnectionHelper() {
    }

    /**
     * Method getInstance.
     * @return DbConnectionHelper
     * 
     * Returns an instance of the DbConnection Singleton.
     */
    public static DbConnectionHelper getInstance() {
        return instance;
    }

    /**
     * Method initOracleConnectionPool.
     * @param properties
     * @throws DocCheckerException
     * 
     * This method initializes the OracleConnectionPool with the given properties.  It only
     * needs to be called once - the properties are then stored and the DbConnectionHelper
     * Singleton maintains them throughout the life of the object.
     */
    public void initOracleConnectionPool(Properties properties)
			throws DocCheckerException {
		if (initPool == false) {
			props = properties;
			try {
				ocp = new OracleConnectionPoolDataSource();
				ocp.setURL(props.getProperty(ORACLE_URL));
				ocp.setUser(props.getProperty(ORACLE_USER));
				ocp.setPassword(props.getProperty(ORACLE_PASSWD));

				ocacheimpl = new OracleConnectionCacheImpl(ocp);
				ocacheimpl.setMaxLimit(50);
				ocacheimpl.setCacheScheme(OracleConnectionCacheImpl.DYNAMIC_SCHEME);
				// This is for listManager use in GrantQuery Module
				OracleConnectionFactory.initializeConnectionFactory(ocp);

				initPool = true;
			} catch (SQLException e) {
				throw new DocCheckerException(this.getClass().getName() + " " + e.getMessage());
			} catch (Exception e) {
				throw new DocCheckerException(this.getClass().getName() + " " + e.getMessage());
			}
		}
	}

    /**
	 * Method getConnection.
	 * 
	 * @return Connection
	 * @throws DocCheckerException
	 * 
	 * This method returns a Connection from the Pool.
	 */
	public Connection getConnection(ResourceBundle dbBundle)
			throws DocCheckerException {
		Connection conn = null;
		try {
			props = new Properties();
			props.setProperty(ORACLE_URL, dbBundle.getString(ORACLE_URL));
			props.setProperty(ORACLE_USER, dbBundle.getString(ORACLE_USER));
			props.setProperty(ORACLE_PASSWD, dbBundle.getString(ORACLE_PASSWD));
			initOracleConnectionPool(props);
			logger.info("Getting DB Connection...");
			conn = ocacheimpl.getConnection();
			logger.info("Got DB Connection!");
			for (int i = 0; i < 8; i++) {
				if (conn.isClosed()) {
					conn = ocacheimpl.getConnection();
				} else {
					break;
				}
			}
		} catch (Exception e) {
			throw new DocCheckerException(this.getClass().getName() + " " + e.getMessage());
		}
		return conn;
	}

    /**
	 * Method freeConnection.
	 * 
	 * @param con
	 * @throws DocCheckerException
	 * 
	 * This method serves as a wrapper for Connection.close(). It simply closes
	 * the Connection and allows the OracleConnectionPool to return the
	 * Connection to the pool.
	 */
    public void freeConnection(Connection con) throws DocCheckerException {
		try {
			logger.info("Closing DB Connection...");
			con.close();
			logger.info("DB Connection closed!");
		} catch (Exception e) {
			throw new DocCheckerException(this.getClass().getName() + " " + e.getMessage());
		}
	}
}
