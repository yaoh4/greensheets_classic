package gov.nih.nci.cbiit.scimgmt.gs.service;

import java.util.List;
import java.util.Map;

import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormModulesDraft;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormQuestionsDraft;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;

/**
 * Interface to handle draft modules deployed from GS Form Builder.
 */
public interface DraftModuleService {
    /**
     * Validate and import module deployed by GS Form Builder.
     * @param module - predefined prefix / name of the module.  Currently, it's PC, PNC, SC, or SNC
     * @param role_code - Role code - PGM, SPEC, REV, or other.
     * @param mapQuestions - map of parsed questions - key value is question UUID
     * @return FormModulesDraft obect - parsed module converted to hibernate
     * @throws GreensheetBaseException when file is not found, or XML validation or parsing failed
     *
     * Note: role_code is a temporary parameter until it will be provided as part of GSFB XML file for each form
     */
    FormModulesDraft importModuleFromXML(String module, String role_code, Map<String, FormQuestionsDraft> mapQuestions) throws GreensheetBaseException;
    public <T> List<T> findByUniqueId(Class<T> clazz, String columnName, String uuid);
    void deleteModule(Object obj, Integer id) throws GreensheetBaseException;
	void saveModule(FormModulesDraft fmd, Map<String, FormQuestionsDraft> fqdMap) throws GreensheetBaseException;
}
