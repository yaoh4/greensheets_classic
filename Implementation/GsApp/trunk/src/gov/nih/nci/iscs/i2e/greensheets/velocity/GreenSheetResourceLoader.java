package gov.nih.nci.iscs.i2e.greensheets.velocity;

import gov.nih.nci.iscs.i2e.greensheets.GreensheetResourceManager;
import gov.nih.nci.iscs.i2e.greensheets.GreensheetsResourceException;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetresourcemgr.GreensheetResourceManagerImpl;
//import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetMgrFactory;	//Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;

/**
 * Created by IntelliJ IDEA. User: Jed Date: Sep 7, 2003 Time: 12:53:26 PM To
 * change this template use Options | File Templates.
 */
public class GreenSheetResourceLoader extends
		org.apache.velocity.runtime.resource.loader.ResourceLoader {

//	private GreensheetResourceManager gsMgr = GreensheetMgrFactory	//Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
//			.createGreenSheetResourceMgr(GreensheetMgrFactory.PROD);
	private GreensheetResourceManager gsMgr = new GreensheetResourceManagerImpl(); // For time being -- Abdul Latheef

	public void init(ExtendedProperties extendedProperties) {
		;
	}

	public InputStream getResourceStream(String s)
			throws ResourceNotFoundException {
		String tmplStr = null;

		try {
			tmplStr = gsMgr.getResource(s,
					GreensheetResourceManager.VMTEMPLATE_RESOURCE);
		} catch (GreensheetsResourceException e) {
			throw new ResourceNotFoundException(e.getMessage());

		}

		return (new ByteArrayInputStream(tmplStr.getBytes()));
	}

	public boolean isSourceModified(Resource resource) {
		return false;
	}

	public long getLastModified(Resource resource) {
		return 0;
	}
}
