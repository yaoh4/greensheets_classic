package gov.nih.nci.cbiit.scimgmt.gs.dao;

import java.util.List;

public interface FormUtilDAO {
	
	public boolean delete(Object obj, Integer id);
	public boolean persist(Object obj);
	public <T> List<T> findByUniqueId(Class<T> clazz, String columnName, String uuid);
	public void deleteQuestions();
}
