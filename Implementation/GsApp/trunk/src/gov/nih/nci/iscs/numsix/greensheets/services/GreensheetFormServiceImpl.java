package gov.nih.nci.iscs.numsix.greensheets.services;

import org.apache.log4j.Logger;

import gov.nih.nci.cbiit.atsc.dao.FormGrant;
import gov.nih.nci.cbiit.atsc.dao.GreensheetForm;
import gov.nih.nci.cbiit.atsc.dao.GreensheetFormDAO;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormDataHelper;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetGroupType;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetStatus;

public class GreensheetFormServiceImpl implements GreensheetFormService {
	private GreensheetFormDAO greensheetFormDAO;
	private static final Logger log = Logger.getLogger(GreensheetFormServiceImpl.class);

	public void setGreensheetFormDAO(GreensheetFormDAO greensheetFormDAO) {
		this.greensheetFormDAO = greensheetFormDAO;
	}

	public GreensheetFormProxy getGreensheetForm(FormGrantProxy formGrantProxy, String formRoleCode) 
			throws GreensheetBaseException {
		log.debug("getGreensheetForm() start");
		FormGrant formGrant = null;
		String formStatus = "";
		GreensheetForm greensheetForm = null;
		GreensheetFormProxy greensheetFormProxy = null;
		
		if (formGrantProxy != null) {
			formGrant = formGrantProxy;
		} else {
			log.debug("returning null");
			return null;
		}

		
		greensheetForm = greensheetFormDAO.getGreensheetForm(formGrant, formRoleCode);
		log.debug("greensheetForm: " + greensheetForm);
		if (greensheetForm != null) {
			greensheetFormProxy = new GreensheetFormProxy();
			
			greensheetFormProxy.setId(greensheetForm.getId());
			greensheetFormProxy.setFtmId(greensheetForm.getFtmId());
			greensheetFormProxy.setFormRoleCode(greensheetForm.getFormRoleCode());
			greensheetFormProxy.setFormStatus(greensheetForm.getFormStatus());
			greensheetFormProxy.setPoc(greensheetForm.getPoc());
			greensheetFormProxy.setSubmittedUserId(greensheetForm.getSubmittedUserId());
			greensheetFormProxy.setCreateUserId(greensheetForm.getCreateUserId());
			greensheetFormProxy.setCreateDate(greensheetForm.getCreateDate());
			greensheetFormProxy.setLastChangeUserId(greensheetForm.getLastChangeUserId());
			greensheetFormProxy.setLastChangeDate(greensheetForm.getLastChangeDate());
			greensheetFormProxy.setUpdateStamp(greensheetForm.getUpdateStamp());
			greensheetFormProxy.setSubmittedDate(greensheetForm.getSubmittedDate());
			greensheetFormProxy.setApplId(new Long(formGrant.getApplId()).intValue());
			
			//Set the form Status
			formStatus = greensheetForm.getFormStatus();
			if (formStatus.equalsIgnoreCase(GreensheetStatus.SAVED.getName())) {
				greensheetFormProxy.setStatus(GreensheetStatus.SAVED);
			} else if (formStatus.equalsIgnoreCase(GreensheetStatus.SUBMITTED.getName())) {
				greensheetFormProxy.setStatus(GreensheetStatus.SUBMITTED);
			} else if (formStatus.equalsIgnoreCase(GreensheetStatus.UNSUBMITTED.getName())) {
				greensheetFormProxy.setStatus(GreensheetStatus.UNSUBMITTED);
			} else if (formStatus.equalsIgnoreCase(GreensheetStatus.FROZEN.getName())) {
				greensheetFormProxy.setStatus(GreensheetStatus.FROZEN);
			} else if (formStatus.equalsIgnoreCase(GreensheetStatus.NOT_STARTED.getName())) {
				greensheetFormProxy.setStatus(GreensheetStatus.NOT_STARTED);
			}
			
			//Set the form role code
			formRoleCode = greensheetForm.getFormRoleCode();
			if (formRoleCode.equalsIgnoreCase(GreensheetGroupType.PGM.getName())) {
				greensheetFormProxy.setGroupType(GreensheetGroupType.PGM);
			} else if (formRoleCode.equalsIgnoreCase(GreensheetGroupType.SPEC.getName())) {
				greensheetFormProxy.setGroupType(GreensheetGroupType.SPEC);
			} else if (formRoleCode.equalsIgnoreCase(GreensheetGroupType.RMC.getName())) {
				greensheetFormProxy.setGroupType(GreensheetGroupType.RMC);
			} else if (formRoleCode.equalsIgnoreCase(GreensheetGroupType.DM.getName())) {
				greensheetFormProxy.setGroupType(GreensheetGroupType.DM);
			} else if (formRoleCode.equalsIgnoreCase(GreensheetGroupType.REV.getName())) {
				greensheetFormProxy.setGroupType(GreensheetGroupType.REV);
			}
			
		} else {
			return null;
		}
		
		//Task to do: The following needs to be worked on.
		GreensheetFormDataHelper dh = new GreensheetFormDataHelper();
		try {
			if (greensheetFormProxy.getId() > 0) {
				dh.getGreensheetFormAnswers(greensheetFormProxy);
			}
		} catch (GreensheetBaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); /*  WHY ARE WE CATCHING IT AND NOT LETTING THE ERROR PAGE AND EMAIL NOTIFICATION TO HAPPEN?? - Anatoli  */
			return null;
		}		
		
		return greensheetFormProxy;
	}
}
