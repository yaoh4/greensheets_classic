package gov.nih.nci.cbiit.scimgmt.gs.dao;

import java.util.Map;

public interface PdfUploadDAO {

    public Map<String, String> getPfdUploadCandidates();
    public int updateUploadStatus(int frmId);
}
