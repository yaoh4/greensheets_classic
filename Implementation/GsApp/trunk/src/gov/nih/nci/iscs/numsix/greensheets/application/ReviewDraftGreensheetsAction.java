/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseDispatchAction;
import gov.nih.nci.iscs.numsix.greensheets.services.ProcessNewQuestionDefsService;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUserRole;
import gov.nih.nci.iscs.numsix.greensheets.utils.FilesysUtils;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

public class ReviewDraftGreensheetsAction extends GsBaseDispatchAction  {

	private static final Logger logger = Logger
	.getLogger(ReviewDraftGreensheetsAction.class);

	static ProcessNewQuestionDefsService processNewQuestionDefsService;


	public ProcessNewQuestionDefsService getProcessNewQuestionDefsService() {
		return processNewQuestionDefsService;
	}

	public void setProcessNewQuestionDefsService(
			ProcessNewQuestionDefsService processNewQuestionDefsService) {
		this.processNewQuestionDefsService = processNewQuestionDefsService;
	}

	/**
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */

	public ActionForward loadReviewDraftGsPage(ActionMapping mapping, ActionForm aForm,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {

		String displayReviewPage = "success";
		ReviewDraftGreensheetsForm rvwDraftForm = (ReviewDraftGreensheetsForm) aForm;

		req.getSession().removeAttribute(GreensheetsKeys.USER_NOT_FOUND);
		this.generateReviewDraftDropdowns(req,rvwDraftForm);
		//Need to display Review Greensheets and Review Module Status buttons
		rvwDraftForm.setDisplayReviewModuleStatusButton(false);
		rvwDraftForm.setDisplayPromoteButton(false);
		rvwDraftForm.setDisplayViewGreensheetButton(false);
		rvwDraftForm.setDisplayRejectButton(false);
		req.setAttribute(Constants.SELECTED_DRAFT_MODULE,null );

		return mapping.findForward(displayReviewPage);
	}
	
	public ActionForward reviewDraft(ActionMapping mapping, ActionForm aForm,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		String displayReviewPage = "success";
		ReviewDraftGreensheetsForm rvwDraftForm = (ReviewDraftGreensheetsForm) aForm;
      
		req.getSession().removeAttribute(GreensheetsKeys.USER_NOT_FOUND);

		GreensheetUserSession gus = GreensheetActionHelper.getGreensheetUserSession(req);
		GsUser gsUser = gus.getUser();
		GsUserRole gsUserRole = gsUser.getRole();
		if (gsUserRole.equals(GsUserRole.SPEC)) {
			if (gus.getUser().getMyPortfolioIds() != null) {
				req.setAttribute("MY_PORTFOLIO_OPT", "true");
			}
			displayReviewPage = "success";
		}

		// If "role" = "Program" or "Analyst"
		if (gsUserRole.equals(GsUserRole.PGM_DIR) || gsUserRole.equals(GsUserRole.PGM_ANL)) {
			displayReviewPage = "success";
		}
		
		this.generateDynamicMechDropdowns(req,rvwDraftForm);
		//Need to display Review Greensheets and Review Module Status buttons
		rvwDraftForm.setDisplayReviewModuleStatusButton(true);
		rvwDraftForm.setDisplayPromoteButton(false);
		rvwDraftForm.setDisplayViewGreensheetButton(true);
		rvwDraftForm.setDisplayRejectButton(false);
		req.setAttribute(Constants.SELECTED_DRAFT_MODULE,null);
		return mapping.findForward(displayReviewPage);
	}

	public ActionForward generateDynamicMechDraft(ActionMapping mapping, ActionForm aForm,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {

		String displayReviewPage = "success";
		ReviewDraftGreensheetsForm rvwDraftForm = (ReviewDraftGreensheetsForm) aForm;

		req.getSession().removeAttribute(GreensheetsKeys.USER_NOT_FOUND);

		GreensheetUserSession gus = GreensheetActionHelper.getGreensheetUserSession(req);
		GsUser gsUser = gus.getUser();
		GsUserRole gsUserRole = gsUser.getRole();
		if (gsUserRole.equals(GsUserRole.SPEC)) {
			if (gus.getUser().getMyPortfolioIds() != null) {
				req.setAttribute("MY_PORTFOLIO_OPT", "true");
			}
			// set forward name
			displayReviewPage = "success";
		}

		// If "role" = "Program" or "Analyst"
		if (gsUserRole.equals(GsUserRole.PGM_DIR) || gsUserRole.equals(GsUserRole.PGM_ANL)) {
			displayReviewPage = "success";
		}

		this.generateDynamicMechDropdowns(req,rvwDraftForm);
		//Need to display Review Greensheets and Review Module Status buttons
		rvwDraftForm.setDisplayReviewModuleStatusButton(true);
		rvwDraftForm.setDisplayPromoteButton(false);
		rvwDraftForm.setDisplayViewGreensheetButton(true);
		rvwDraftForm.setDisplayRejectButton(false);
		req.setAttribute(Constants.SELECTED_DRAFT_MODULE,null);
		return mapping.findForward(displayReviewPage);
	}

	private void generateReviewDraftDropdowns(HttpServletRequest req, ReviewDraftGreensheetsForm rvwDraftForm) 
			throws Exception {
		HashSet<String> draftModuleTypeMechList = null;
		HashSet<String> updatedListCount = new HashSet<String>();
		List<String> typeMechModuleSplit = new ArrayList<String>();
		ArrayList<String> moduleList = new ArrayList<String>();
		ArrayList<String> typeList = new ArrayList<String>();
		ArrayList<String> mechList = new ArrayList<String>();
		ArrayList<String> updateDropList = new ArrayList<String>();
		ArrayList<String> allModuleList = new ArrayList<String>();
		HashSet<String> moduleListDb = null;
		boolean showUpdateOnly = true;
		boolean isUpdate = false;
		String updateDropDownType = rvwDraftForm.getUpdateOption();
		String moduleName =rvwDraftForm.getModuleName();
		String selectedModuleName = rvwDraftForm.getSelectedModuleName();
		if (moduleName==null){
			if (selectedModuleName!=null){
				moduleName = selectedModuleName;
			}
		}
		updatedListCount = processNewQuestionDefsService.getUpdatedOnlyTypeMechListFromDB(moduleName);
		logger.debug("Reviewdraft - updatedListCount: " + updatedListCount);
		if((updatedListCount.size() == 0)) {
			showUpdateOnly = false;
		}
		if ((updateDropDownType == null) || (updateDropDownType.equalsIgnoreCase(Constants.UPDATE_DROP_DOWN_OPTION_ALL)) ){
			processNewQuestionDefsService.setTypeMechList(null);
			draftModuleTypeMechList = processNewQuestionDefsService.getDraftList();
		}
		else {
			if((updatedListCount.size() != 0)) {
				isUpdate = true;
				processNewQuestionDefsService.setTypeMechUpdatedList(null);
				draftModuleTypeMechList = processNewQuestionDefsService.getDraftList();
			}
			else {
				processNewQuestionDefsService.setTypeMechList(null);
				draftModuleTypeMechList = processNewQuestionDefsService.getDraftList();	
			}
		}		
		
		// DB returns a hashset which contains comma separated module, type mech values
		if(draftModuleTypeMechList != null) {
			for(String s : draftModuleTypeMechList){
				for (String split : s.split(",")) {
					typeMechModuleSplit.add(split.trim());
				}
			}
		}
		
		moduleList.add("Please Select Module");	
		if ((typeMechModuleSplit.contains("PC"))){
			moduleList.add("Program Competing");		
		}
		if ((typeMechModuleSplit.contains("PNC"))){
			moduleList.add("Program Non Competing");		
		}
		if ((typeMechModuleSplit.contains("SC"))){
			moduleList.add("Specialist Competing");		
		}
		if ((typeMechModuleSplit.contains("SNC"))){
			moduleList.add("Specialist Non Competing");		
		}
		
		if(isUpdate){
			moduleListDb = processNewQuestionDefsService.getModuleListFromDB();
			if (!moduleListDb.isEmpty()){
				allModuleList.addAll(moduleListDb);
			}
			if ((allModuleList.contains("PC"))){
				moduleList.add("Program Competing");		
			}
			if ((allModuleList.contains("PNC"))){
				moduleList.add("Program Non Competing");		
			}
			if ((allModuleList.contains("SC"))){
				moduleList.add("Specialist Competing");		
			}
			if ((allModuleList.contains("SNC"))){
				moduleList.add("Specialist Non Competing");		
			}
		}
		
		updateDropList.add(Constants.UPDATE_DROP_DOWN_OPTION_ALL);	
		if(showUpdateOnly){
			updateDropList.add(Constants.UPDATE_DROP_DOWN_OPTION_UPDATE);
		}
		Collections.sort(moduleList);
		Collections.sort(typeList);
		Collections.sort(mechList);
		req.setAttribute(Constants.DRAFT_MODULE_NAME, moduleList);
		req.setAttribute(Constants.DRAFT_TYPE, typeList);
		req.setAttribute(Constants.DRAFT_MECHANISM, mechList);
		req.setAttribute(Constants.DRAFT_UPDATE, updateDropList);
	}

	private void generateDynamicMechDropdowns(HttpServletRequest req, ReviewDraftGreensheetsForm rvwDraftForm) 
			throws Exception {
		HashSet<String> draftModuleTypeMechList = null;
		HashSet<String> dynamicMechList = null;
		
		String roleCode = null;
		boolean showUpdateOnly = true;
		boolean isUpdate = false;
		List<String> typeMechModuleSplit = new ArrayList<String>();
		ArrayList<String> allModuleList = new ArrayList<String>();
		ArrayList<String> moduleList = new ArrayList<String>();
		ArrayList<String> typeList = new ArrayList<String>();
		ArrayList<String> mechList = new ArrayList<String>();
		ArrayList<String> dyanamicMechList = new ArrayList<String>();
		ArrayList<String> updateDropList = new ArrayList<String>();
		HashSet<String> updatedListCount = new HashSet<String>();
		HashSet<String> moduleListDb = null;
		String updateDropDownType = rvwDraftForm.getUpdateOption();
		String moduleName =rvwDraftForm.getModuleName();
		String selectedModuleName = rvwDraftForm.getSelectedModuleName();
		if (moduleName == null && selectedModuleName != null){
			moduleName = selectedModuleName;
		}
		
		updatedListCount = processNewQuestionDefsService.getUpdatedOnlyTypeMechListFromDB(moduleName);
		logger.debug("Reviewdraft updatedListCount: " + updatedListCount);

		if (updateDropDownType == null || updateDropDownType.equalsIgnoreCase(Constants.UPDATE_DROP_DOWN_OPTION_ALL)){	   
			processNewQuestionDefsService.setTypeMechList(null);
			draftModuleTypeMechList = processNewQuestionDefsService.getDraftList();
		} else {
			if(updatedListCount.size()!=0){
				showUpdateOnly = false;
				isUpdate= true;
				processNewQuestionDefsService.setTypeMechUpdatedList(null);
				draftModuleTypeMechList = processNewQuestionDefsService.getDraftList();
			} else {
				processNewQuestionDefsService.setTypeMechList(null);
				draftModuleTypeMechList = processNewQuestionDefsService.getDraftList();	
			}
		}
		
		String type = rvwDraftForm.getType();
		if (moduleName != null){
			roleCode = FilesysUtils.getRoleCodeFromModuleName(moduleName);
		}

		// DB returns a hashset which contains comma separated module, type mech values
		for (String typeMech : draftModuleTypeMechList){
			for (String s : typeMech.toString().split(",")) {
				typeMechModuleSplit.add(s.trim());
			}
		}

		if ((typeMechModuleSplit.contains("PC"))){
			moduleList.add("Program Competing");		
		}
		if ((typeMechModuleSplit.contains("PNC"))){
			moduleList.add("Program Non Competing");		
		}
		if ((typeMechModuleSplit.contains("SC"))){
			moduleList.add("Specialist Competing");		
		}
		if ((typeMechModuleSplit.contains("SNC"))){
			moduleList.add("Specialist Non Competing");		
		}
		
		if (isUpdate) {
			moduleListDb = processNewQuestionDefsService.getModuleListFromDB();
			if (!moduleListDb.isEmpty()) {
				allModuleList.addAll(moduleListDb);
			}
			if (allModuleList.contains("PC") && !moduleList.contains("Program Competing")) {
				moduleList.add("Program Competing");
			}
			if (allModuleList.contains("PNC") && !moduleList.contains("Program Non Competing")) {
				moduleList.add("Program Non Competing");
			}
			if (allModuleList.contains("SC") && !moduleList.contains("Specialist Competing")) {
				moduleList.add("Specialist Competing");
			}
			if (allModuleList.contains("SNC") && !moduleList.contains("Specialist Non Competing")) {
				moduleList.add("Specialist Non Competing");
			}
		}

		for (int i=0; i<typeMechModuleSplit.size();i++){
			if (i != 0){
				if (!typeList.contains(typeMechModuleSplit.get(i+1))){
					if (moduleName.equalsIgnoreCase("Program Competing") && typeMechModuleSplit.get(i).equalsIgnoreCase("PC")){
						typeList.add(typeMechModuleSplit.get(i+1));
					}
					if (moduleName.equalsIgnoreCase("Program Non Competing") && typeMechModuleSplit.get(i).equalsIgnoreCase("PNC")){
						typeList.add(typeMechModuleSplit.get(i+1));
					}
					if (moduleName.equalsIgnoreCase("Specialist Competing") && typeMechModuleSplit.get(i).equalsIgnoreCase("SC")){
						typeList.add(typeMechModuleSplit.get(i+1));
					}
					if (moduleName.equalsIgnoreCase("Specialist Non Competing") && typeMechModuleSplit.get(i).equalsIgnoreCase("SNC")){
						typeList.add(typeMechModuleSplit.get(i+1));
					}
				} 
				if (!mechList.contains(typeMechModuleSplit.get(i+2))){
					mechList.add(typeMechModuleSplit.get(i+2));
				}
			} else {
				if (((moduleName.equalsIgnoreCase("Program Competing"))
						&& (typeMechModuleSplit.get(i).equalsIgnoreCase("PC")))) {
					typeList.add(typeMechModuleSplit.get(i + 1));
				}
				if (((moduleName.equalsIgnoreCase("Program Non Competing"))
						&& (typeMechModuleSplit.get(i).equalsIgnoreCase("PNC")))) {
					typeList.add(typeMechModuleSplit.get(i + 1));
				}
				if (((moduleName.equalsIgnoreCase("Specialist Competing"))
						&& (typeMechModuleSplit.get(i).equalsIgnoreCase("SC")))) {
					typeList.add(typeMechModuleSplit.get(i + 1));
				}
				if (((moduleName.equalsIgnoreCase("Specialist Non Competing"))
						&& (typeMechModuleSplit.get(i).equalsIgnoreCase("SNC")))) {
					typeList.add(typeMechModuleSplit.get(i + 1));
				}
				mechList.add(typeMechModuleSplit.get(i + 2));
			}
			i= i+2;
		}
		Collections.sort(typeList);
		// To check if this is the first time Type list is loading so in order
		// to generate dynamic mech list based on first type
		if (type == null && typeList != null && !typeList.isEmpty()) {
			type = typeList.get(0).toString();
		}
		if ((updateDropDownType == null)
				|| (updateDropDownType.equalsIgnoreCase(Constants.UPDATE_DROP_DOWN_OPTION_ALL))) {
			processNewQuestionDefsService.setDynamicMechList(type, roleCode);
			dynamicMechList = processNewQuestionDefsService.getMechDynamicList();
		} else {
			processNewQuestionDefsService.setUpdatedDynamicMechList(type, roleCode);
			dynamicMechList = processNewQuestionDefsService.getUpdatedMechDynamicList();
		}

		if (!typeList.isEmpty()){
			if (dynamicMechList.isEmpty()){
				type = typeList.get(0).toString();
				processNewQuestionDefsService.setDynamicMechList(type, roleCode);
				dynamicMechList = processNewQuestionDefsService.getMechDynamicList();	
			}
		}

		for (String typeMech : dynamicMechList){
			dyanamicMechList.add(typeMech);
		}
		
		updateDropList.add(Constants.UPDATE_DROP_DOWN_OPTION_ALL);	
		if(showUpdateOnly){
			updateDropList.add(Constants.UPDATE_DROP_DOWN_OPTION_UPDATE);
		}
		Collections.sort(moduleList);
		Collections.sort(mechList);
		Collections.sort(dyanamicMechList);
		
		req.setAttribute(Constants.DRAFT_MODULE_NAME, moduleList);
		req.setAttribute(Constants.DRAFT_TYPE, typeList);
		req.setAttribute(Constants.DRAFT_MECHANISM, dyanamicMechList);
		req.setAttribute(Constants.DRAFT_UPDATE, updateDropList);
	}

	public void generateDynamicMechDropdownsPromReject(HttpServletRequest req, ReviewDraftGreensheetsForm rvwDraftForm) throws Exception {
		HashSet<String> draftModuleTypeMechList = null;
		HashSet<String> dynamicMechList = null;
		HashSet<String> updatedListCount = new HashSet<String>();
		String roleCode = null;
		String roleCodeSelected = null;
		boolean showUpdateOnly = true;
		boolean isUpdate = false;
		ArrayList<String> allModuleList = new ArrayList<String>();
		List<String> typeMechModuleSplit = new ArrayList<String>();
		ArrayList<String> moduleList = new ArrayList<String>();
		ArrayList<String> typeList = new ArrayList<String>();
		ArrayList<String> mechList = new ArrayList<String>();
		ArrayList<String> dyanamicMechList = new ArrayList<String>();
		ArrayList<String> updateDropList = new ArrayList<String>();
		
		String updateDropDownType = rvwDraftForm.getUpdateOption();
		String moduleName =rvwDraftForm.getModuleName();
		String selectedModuleName = rvwDraftForm.getSelectedModuleName();
		if (moduleName==null && selectedModuleName!=null){
			moduleName = selectedModuleName;
		}
		updatedListCount = processNewQuestionDefsService.getUpdatedOnlyTypeMechListFromDB(moduleName);
		logger.debug("Reviewdraft updatedListCount: " + updatedListCount);
		if((updatedListCount.size()==0)){
			showUpdateOnly = false;
		}
		if ((updateDropDownType == null) ||(updateDropDownType.equalsIgnoreCase(Constants.UPDATE_DROP_DOWN_OPTION_ALL)) ){
			processNewQuestionDefsService.setTypeMechList(null);
			draftModuleTypeMechList = processNewQuestionDefsService.getDraftList();
		} else {
			if((updatedListCount.size()!=0)){
				isUpdate= true;
				processNewQuestionDefsService.setTypeMechUpdatedList(null);
				draftModuleTypeMechList = processNewQuestionDefsService.getDraftList();
			} else {
				processNewQuestionDefsService.setTypeMechList(null);
				draftModuleTypeMechList = processNewQuestionDefsService.getDraftList();	
			}
		}
		
		String type = rvwDraftForm.getType();
		roleCode = FilesysUtils.getRoleCodeFromModuleName(moduleName);
		if (selectedModuleName != null){
			roleCodeSelected = FilesysUtils.getRoleCodeFromModuleName(selectedModuleName);
		}

		// DB returns a hashset which contains comma seperated module, type mech values
		for(String s : draftModuleTypeMechList) {
			for (String split : s.split(",")) {
				typeMechModuleSplit.add(split.trim());
			}
		}

		if ((typeMechModuleSplit.contains("PC"))&&(!roleCodeSelected.equalsIgnoreCase("PC"))){
			moduleList.add("Program Competing");		
		}
		if ((typeMechModuleSplit.contains("PNC"))&&(!roleCodeSelected.equalsIgnoreCase("PNC"))){
			moduleList.add("Program Non Competing");		
		}
		if ((typeMechModuleSplit.contains("SC"))&&(!roleCodeSelected.equalsIgnoreCase("SC"))){
			moduleList.add("Specialist Competing");		
		}
		if ((typeMechModuleSplit.contains("SNC"))&&(!roleCodeSelected.equalsIgnoreCase("SNC"))){
			moduleList.add("Specialist Non Competing");		
		}
		if(isUpdate){
			for(String module : processNewQuestionDefsService.getModuleListFromDB()) {
				allModuleList.add(module);

				if ((allModuleList.contains("PC"))){
					moduleList.add("Program Competing");		
				}
				if ((allModuleList.contains("PNC"))){
					moduleList.add("Program Non Competing");		
				}
				if ((allModuleList.contains("SC"))){
					moduleList.add("Specialist Competing");		
				}
				if ((allModuleList.contains("SNC"))){
					moduleList.add("Specialist Non Competing");		
				}
			}
		}
		if (!moduleList.isEmpty()){
			moduleName = moduleList.get(0).toString();
			updatedListCount = processNewQuestionDefsService.getUpdatedOnlyTypeMechListFromDB(moduleName);

			if((updatedListCount.size()==0)){
				showUpdateOnly = false;
			}
		}
		if (!moduleList.isEmpty()){
			moduleName = moduleList.get(0).toString();
			for (int i=0; i<typeMechModuleSplit.size();i++){
				if (i != 0){
					if (!typeList.contains(typeMechModuleSplit.get(i+1))){
						if (((moduleName.equalsIgnoreCase("Program Competing"))&&(typeMechModuleSplit.get(i).equalsIgnoreCase("PC")))){
							typeList.add(typeMechModuleSplit.get(i+1));
						}
						if (((moduleName.equalsIgnoreCase("Program Non Competing"))&&(typeMechModuleSplit.get(i).equalsIgnoreCase("PNC")))){
							typeList.add(typeMechModuleSplit.get(i+1));
						}
						if (((moduleName.equalsIgnoreCase("Specialist Competing"))&&(typeMechModuleSplit.get(i).equalsIgnoreCase("SC")))){
							typeList.add(typeMechModuleSplit.get(i+1));
						}
						if (((moduleName.equalsIgnoreCase("Specialist Non Competing"))&&(typeMechModuleSplit.get(i).equalsIgnoreCase("SNC")))){
							typeList.add(typeMechModuleSplit.get(i+1));
						}
					} 
					if (!mechList.contains(typeMechModuleSplit.get(i+2))){
						mechList.add(typeMechModuleSplit.get(i+2));
					}
				} else {
					if (((moduleName.equalsIgnoreCase("Program Non Competing"))&&(typeMechModuleSplit.get(i).equalsIgnoreCase("PNC")))){
						typeList.add(typeMechModuleSplit.get(i+1));
					}
					if (((moduleName.equalsIgnoreCase("Specialist Competing"))&&(typeMechModuleSplit.get(i).equalsIgnoreCase("SC")))){
						typeList.add(typeMechModuleSplit.get(i+1));
					}
					if (((moduleName.equalsIgnoreCase("Specialist Non Competing"))&&(typeMechModuleSplit.get(i).equalsIgnoreCase("SNC")))){
						typeList.add(typeMechModuleSplit.get(i+1));
					}
					mechList.add(typeMechModuleSplit.get(i+2));
				}
				i= i+2;
			}

			Collections.sort(typeList);
			//To check if this is the first time Type list is loading so in order to generate dynamic mech list based on first type
			if (type==null && typeList!=null && !typeList.isEmpty()){
				type = typeList.get(0).toString();
			}

			if ((updateDropDownType == null) ||(updateDropDownType.equalsIgnoreCase(Constants.UPDATE_DROP_DOWN_OPTION_ALL)) ){
				processNewQuestionDefsService.setDynamicMechList(type, roleCode);
				dynamicMechList = processNewQuestionDefsService.getMechDynamicList();
			} else {
				processNewQuestionDefsService.setUpdatedDynamicMechList(type, roleCode);
				dynamicMechList = processNewQuestionDefsService.getUpdatedMechDynamicList();	
			}
			if (!typeList.isEmpty() && dynamicMechList.isEmpty()){
				type = typeList.get(0).toString();
				roleCode=FilesysUtils.getRoleCodeFromModuleName(moduleName);
				processNewQuestionDefsService.setDynamicMechList(type, roleCode);
				dynamicMechList = processNewQuestionDefsService.getMechDynamicList();	
			}

			for(String dynamicMech : dynamicMechList){
				dyanamicMechList.add(dynamicMech);
			}

			updateDropList.add(Constants.UPDATE_DROP_DOWN_OPTION_ALL);	
			if(showUpdateOnly){
				updateDropList.add(Constants.UPDATE_DROP_DOWN_OPTION_UPDATE);
			}
			Collections.sort(moduleList);

			Collections.sort(mechList);
			Collections.sort(dyanamicMechList);

			req.setAttribute(Constants.DRAFT_MODULE_NAME, moduleList);
			req.setAttribute(Constants.DRAFT_TYPE, typeList);
			req.setAttribute(Constants.DRAFT_MECHANISM, dyanamicMechList);
			req.setAttribute(Constants.DRAFT_UPDATE, updateDropList);
		} else {
			moduleList.add("Please Select Module");	
			req.setAttribute(Constants.DRAFT_MODULE_NAME, moduleList);
			req.setAttribute(Constants.DRAFT_TYPE, typeList);
			req.setAttribute(Constants.DRAFT_MECHANISM, dyanamicMechList);
			rvwDraftForm.setDisplayReviewModuleStatusButton(false);
			rvwDraftForm.setDisplayViewGreensheetButton(false);
		}
	}
}
