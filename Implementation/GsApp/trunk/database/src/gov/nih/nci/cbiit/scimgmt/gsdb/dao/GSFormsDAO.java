package gov.nih.nci.cbiit.scimgmt.gsdb.dao;

import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormTemplatesMatrix;
import gov.nih.nci.cbiit.scimgmt.gsdb.model.WorkingTemplates;

import java.util.List;

/**
 * Created by polonskyy on 9/29/16.
 */
public interface GSFormsDAO {
    /**
     * Populate FormTemplatesMatrix with all current templates and with all
     * templates used to create greensheets
     *
     * @return list of WorkingTemplates objects - detached from the session content of FormTemplatesMatrix table
     * @throws Exception
     */
    List<WorkingTemplates> populateFormTemplateMatrix() throws Exception;

    List<WorkingTemplates> retrieveFormTemplateMatrix() throws Exception;

    void populateModules() throws Exception;

    void populateFormsToMatrixLinks() throws Exception;

    int extractTemplateContent(List<WorkingTemplates> templateMatrix, int nextEntry) throws Exception;
}
