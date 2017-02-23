package gov.nih.nci.cbiit.scimgmt.gs.dao;

import java.util.HashSet;

public interface PromoteModuleDAO {

	public boolean promoteDraftGreensheets(String module) throws Exception;
	public void reActivedMechType(String module, String typeMech);
    public void loadInactiveMechType(String module, HashSet<String> inActiveList);
}
