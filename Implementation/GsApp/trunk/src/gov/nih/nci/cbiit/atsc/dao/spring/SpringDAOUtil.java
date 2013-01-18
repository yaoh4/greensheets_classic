package gov.nih.nci.cbiit.atsc.dao.spring;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * @author Anatoli Kouznetsov
 * Utility class, to house utility methods that can be used in "regular" DAO classes 
 * based on Spring JDBC    
 */
public class SpringDAOUtil {

	private static Logger logger = Logger.getLogger(SpringDAOUtil.class);
	/**
	 * Writes to the log the values contained in the Spring JDBC object that holds
	 * parameters for passing to a parameterized SQL statement
	 * @param sqlParams - the object containing SQL parameters 
	 */
    protected static void writeQueryParamsToLog(MapSqlParameterSource sqlParams) {
    	logger.debug(" = - = -  Below are the parameters provided to the SQL statement:");
    	Object key;
    	Iterator i = sqlParams.getValues().keySet().iterator();
    	while (i != null  &&  i.hasNext()) {
    		key = i.next(); 
    		logger.debug(key.toString() + " :  " + sqlParams.getValues().get(key).toString());
    	}
    }


}
