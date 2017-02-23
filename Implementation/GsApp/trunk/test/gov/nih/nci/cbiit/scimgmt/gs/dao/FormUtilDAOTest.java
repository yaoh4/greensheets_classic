package gov.nih.nci.cbiit.scimgmt.gs.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormModulesDraft;


@RunWith(MockitoJUnitRunner.class)
public class FormUtilDAOTest {
	@Mock
	private FormUtilDAO formUtilDAO;
	private Object obj;
	private List<FormModulesDraft> formModulesDrafts = new ArrayList<FormModulesDraft>();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		obj = new Object();
		FormModulesDraft fmd = new FormModulesDraft();
		fmd.setModuleUuid("123");
		formModulesDrafts.add(fmd);
	}
	@Test
	public void testPersistSuccess() {
		when(formUtilDAO.persist(obj)).thenReturn(true);
		
		boolean result = formUtilDAO.persist(obj);
		assertTrue(result);
		
		verify(formUtilDAO).persist(obj);
	}

	@Test
	public void testPersistFailure() {
		when(formUtilDAO.persist(obj)).thenReturn(false);
		
		boolean result = formUtilDAO.persist(obj);
		assertFalse(result);
		
		verify(formUtilDAO).persist(obj);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=RuntimeException.class)
	public void testPersistForExcepstion() {
		when(formUtilDAO.persist(obj)).thenThrow(RuntimeException.class);
		formUtilDAO.persist(obj);
	}
	
	@Test
	public void testDelete() {
		when(formUtilDAO.delete(obj, 0)).thenReturn(true);
		boolean result = formUtilDAO.delete(obj, 0);
		assertTrue(result);
		
		verify(formUtilDAO).delete(obj, 0);
	}
	
	@Test
	public void testfindByUniqueId() {		
		when(formUtilDAO.findByUniqueId(FormModulesDraft.class, "module_uuid", "123")).thenReturn(formModulesDrafts);
		FormModulesDraft result = formUtilDAO.findByUniqueId(FormModulesDraft.class, "module_uuid", "123").get(0);
		assertNotNull(result);
		verify(formUtilDAO).findByUniqueId(FormModulesDraft.class, "module_uuid", "123");
	}

}
