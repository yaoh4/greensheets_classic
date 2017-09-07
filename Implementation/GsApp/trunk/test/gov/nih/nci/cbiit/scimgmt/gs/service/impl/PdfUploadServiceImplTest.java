package gov.nih.nci.cbiit.scimgmt.gs.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import gov.nih.nci.cbiit.atsc.dao.GrantDAO;
import gov.nih.nci.cbiit.atsc.dao.GreensheetForm;
import gov.nih.nci.cbiit.atsc.dao.GreensheetFormDAO;
import gov.nih.nci.cbiit.scimgmt.gs.dao.PdfUploadDAO;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetFormService;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetFormServiceImpl;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;

@RunWith(MockitoJUnitRunner.class)
public class PdfUploadServiceImplTest {

	@Autowired
	private PdfUploadServiceImpl pdfUploadServiceImpl;
	private GreensheetFormServiceImpl greensheetFormServiceImpl = new GreensheetFormServiceImpl();
	
	@Mock
	@Autowired
	private GreensheetFormService greensheetFormService;
	
	@Autowired
	private GrantDAO grantDAO;
	@Autowired
	private PdfUploadDAO pdfUploadDAO;
	@Autowired
	private GreensheetFormDAO greensheetFormDAO;
	private Map<String, String> applGroupTypeMap;
	private FormGrantProxy grantProxy = new FormGrantProxy(null);
	private GreensheetForm greensheetForm = new GreensheetForm();
	private GreensheetFormProxy greensheetFormProxy = new GreensheetFormProxy();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		applGroupTypeMap = new HashMap<String, String>();
    	applGroupTypeMap.put("6050928\tPGM","1");

		grantDAO = mock(GrantDAO.class);
		pdfUploadDAO = mock(PdfUploadDAO.class);
		greensheetFormDAO = mock(GreensheetFormDAO.class);
		
		pdfUploadServiceImpl = new PdfUploadServiceImpl();		
		greensheetFormService = new GreensheetFormServiceImpl();
		greensheetFormServiceImpl.setGreensheetFormDAO(greensheetFormDAO);
	}
	
	@Test
	public void testPdfUpload() throws Exception {
	    
		when(pdfUploadDAO.getPfdUploadCandidates()).thenReturn(applGroupTypeMap);
		grantProxy.setApplId(6050928);
		greensheetForm.setApplId(6050928);
		when(grantDAO.findGSGrantInfo(null, new Long(6050928), "PGM", null)).thenReturn(grantProxy);
		when(greensheetFormDAO.getGreensheetForm(grantProxy, "PGM")).thenReturn(greensheetFormProxy);
	
		Integer result = pdfUploadServiceImpl.generatePdfUploads();
		assertEquals(result,new Integer(1));
	
	}

}
