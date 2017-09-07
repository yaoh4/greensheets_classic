package gov.nih.nci.cbiit.scimgmt.gs.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import gov.nih.nci.cbiit.atsc.dao.ProcessNewQuestionDefsDAO;
import gov.nih.nci.cbiit.scimgmt.gs.dao.PromoteModuleDAO;
import gov.nih.nci.cbiit.scimgmt.gs.service.PromoteModuleService;
import gov.nih.nci.iscs.numsix.greensheets.services.ProcessNewQuestionDefsService;
import gov.nih.nci.iscs.numsix.greensheets.services.ProcessNewQuestionDefsServicesImpl;

@RunWith(MockitoJUnitRunner.class)
public class PromoteModuleServiceImplTest {
	
	private PromoteModuleDAO promoteModuleDAO;
	private ProcessNewQuestionDefsDAO processNewQuestionDefsDAO;
	
	private HashSet<String> inActiveMechTypeList = new HashSet<String>();
	private String moduleName;
	private String modName;

	@InjectMocks
	private static PromoteModuleService promoteModuleService = new PromoteModuleServiceImpl();
	
	private ProcessNewQuestionDefsServicesImpl processNewQuestionDefsServiceImpl = new ProcessNewQuestionDefsServicesImpl();
	private PromoteModuleServiceImpl promoteModuleServiceImpl = new PromoteModuleServiceImpl();
	
	@Mock
	private static ProcessNewQuestionDefsService processNewQuestionDefsService;
	
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		promoteModuleDAO = mock(PromoteModuleDAO.class);
		processNewQuestionDefsDAO = mock(ProcessNewQuestionDefsDAO.class);
		
		promoteModuleServiceImpl.setPromoteModuleDAO(promoteModuleDAO);
		processNewQuestionDefsServiceImpl.setProcessNewQuestionDefsDAO(processNewQuestionDefsDAO);
		
		inActiveMechTypeList.add("R01,1");
		inActiveMechTypeList.add("R02,2");
		
		moduleName = "Program Non Competing";
		modName = "PNC";
	}
	
	@Test
	public void testPromoteDraftGreensheets() throws Exception {
		
		when(promoteModuleDAO.promoteDraftGreensheets(moduleName)).thenReturn(true);		
		when(processNewQuestionDefsService.getAdditionList(inActiveMechTypeList, moduleName)).thenReturn(new HashSet<String>());
		when(processNewQuestionDefsService.getDeletionList(inActiveMechTypeList, moduleName)).thenReturn(new HashSet<String>());
		when(processNewQuestionDefsService.getInActiveMechTypeByModule(modName)).thenReturn(inActiveMechTypeList);
        
		boolean result = promoteModuleService.promoteDraftGreensheets(moduleName);
		
		Assert.assertTrue(result);
	}

}
