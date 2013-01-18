package gov.nih.nci.cbiit.atsc.dao.spring;

import gov.nih.nci.cbiit.atsc.dao.MiscDataDAO;

import java.sql.Timestamp;

import javax.sql.DataSource;

import org.springframework.jdbc.object.SqlFunction;

public class MiscDataDAOImpl implements MiscDataDAO {
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public int getFiscalYear() {
		SqlFunction sf = new SqlFunction(dataSource, "SELECT F_GET_FY() FROM DUAL");
		sf.compile();

		return sf.run();
	}
	
	public Timestamp getCurrentTimsestamp() {
		//Method I
//		String timestampQuery = "SELECT SYSTIMESTAMP FROM DUAL"; // Most precise
//		Timestamp currentTimestamp = null;
//		
//		SqlQuery simpleQuery;
//		
//		simpleQuery = new SqlFunction(dataSource, timestampQuery);
//		
//		simpleQuery.setRowsExpected(1);
//		List results = simpleQuery.execute();
//		
//		if (!results.isEmpty()) {
//			currentTimestamp =  (Timestamp) results.get(1);
//		}
//		
//		return currentTimestamp;
		
		//Method II
		
		SqlFunction sf = new SqlFunction(dataSource, "SELECT SYSTIMESTAMP FROM DUAL");
//		sf.setResultType(Timestamp.class); // Necessary?
		sf.compile();

		return (Timestamp) sf.runGeneric();
	}	
}
