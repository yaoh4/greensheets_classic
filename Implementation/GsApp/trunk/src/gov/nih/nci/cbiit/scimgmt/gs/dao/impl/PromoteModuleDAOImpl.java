package gov.nih.nci.cbiit.scimgmt.gs.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import gov.nih.nci.cbiit.scimgmt.gs.dao.PromoteModuleDAO;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormElements;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormElementsDraft;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormGrantMatrix;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormGrantMatrixDraft;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormInactiveTypeMech;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormInactiveTypeMechId;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormModules;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormModulesDraft;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormQuestions;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormQuestionsDraft;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormTemplates;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormTemplatesDraft;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormTemplatesMatrix;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormTemplatesMatrixDraft;

public class PromoteModuleDAOImpl implements PromoteModuleDAO {

	private static final Logger logger = Logger.getLogger(PromoteModuleDAOImpl.class);

	private static SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public boolean promoteDraftGreensheets(String module) throws Exception {
		logger.info(".... Promote Draft Greensheets for module: " + module);
		Map<String, FormQuestions> mapWorkingQuestions = new HashMap<String, FormQuestions>();
		Map<String, FormQuestionsDraft> mapDraftQuestions = new HashMap<String, FormQuestionsDraft>();
		
		Date startDate = new Date();

		try {
			String query = "SELECT ftm FROM FormTemplatesMatrix AS ftm WHERE endDate is null";
			List<FormTemplatesMatrix> masterTemplateMatrices = sessionFactory.getCurrentSession().createQuery(query).list();

			query = "SELECT fgm FROM FormGrantMatrix AS fgm";
			List<FormGrantMatrix> masterGrantMatrices = sessionFactory.getCurrentSession().createQuery(query).list();

			query = "SELECT fmd FROM FormModulesDraft AS fmd WHERE name = :module";
			List<FormModulesDraft> fmds = sessionFactory.getCurrentSession().createQuery(query).setParameter("module", module).list();
			FormModulesDraft draftModule = fmds.get(0);

			logger.debug("Processing " + draftModule.getFormTemplatesDrafts().size() + " Templates of module: " + module + ".");

			logger.debug("Create a map of draft questions...");
			// Create a map of draft questions
			for (FormTemplatesDraft ftm : draftModule.getFormTemplatesDrafts()) {
				for (FormElementsDraft fed : ftm.getFormElementsDrafts()) {
					FormQuestionsDraft formQuestionsDraft = fed.getFormQuestionsDraft();
					mapDraftQuestions.put(fed.getFormQuestionsDraft().getGsQuestionId(), formQuestionsDraft);
				}
			}
			logger.debug("Finished mapping of " + mapDraftQuestions.size() + " draft questions.");

			logger.debug("Create a map of working questions...");
			query = "SELECT fm FROM FormModules AS fm WHERE name = :module";
			List<FormModules> workingForms = sessionFactory.getCurrentSession().createQuery(query).setParameter("module", module).list();
			FormModules workingModule = !workingForms.isEmpty() ? workingForms.get(0) : new FormModules();

			// Create a map of working questions
			query = "SELECT t1 FROM FormQuestions t1 "
					+ "WHERE t1.gsfbChangeDate = "
					+ "(SELECT MAX(gsfbChangeDate) "
					+ "FROM FormQuestions t2 "
					+ "WHERE t1.gsQuestionId = t2.gsQuestionId) "
					+ "ORDER BY gsfbChangeDate DESC";

			List<FormQuestions> results = sessionFactory.getCurrentSession().createQuery(query).list();
			for (FormQuestions fq : results) {
				mapWorkingQuestions.put(fq.getGsQuestionId(), fq);
			}
			logger.debug("Finished mapping of " + mapWorkingQuestions.size() + " working questions.");

			/* Create a list of final questions from the two above maps that will be inserted into the form_questions_t table:
			 * Compare draft question and working question on a same:
			 * - GS question ID,
			 * - Question uuid and
			 * - GSFB date
			 * Save the draft question into database, if any of the above conditions is not valid, (i.e. new question);
			 * otherwise, keep the working question. */
			logger.debug("Create a list of final questions...");
			Map<String, FormQuestions> mapFinalQuestions = new HashMap<String, FormQuestions>();
			for (String gsQuestionId : mapDraftQuestions.keySet()) {
				FormQuestionsDraft draftQuestion = mapDraftQuestions.get(gsQuestionId);
				FormQuestions workingQuestion = mapWorkingQuestions.get(gsQuestionId);
				if (mapWorkingQuestions.containsKey(gsQuestionId) 
					&& draftQuestion.getQuestionUuid().equals(workingQuestion.getQuestionUuid())
					&& draftQuestion.getGsfbChangeDate().equals(workingQuestion.getGsfbChangeDate())) {

					mapFinalQuestions.put(gsQuestionId, mapWorkingQuestions.get(gsQuestionId));

				} else {
					mapFinalQuestions.put(gsQuestionId, mapDraftQuestions.get(gsQuestionId).toFormQuestion());
				}
			}
			logger.debug("Finished generating the list of " + mapFinalQuestions.size() + " final questions.");

			/* Insert the draft module into form_modules_t, if the module name doesn't exist;
			 * otherwise, set the module uuid with the draft module uuid, as working module uuid
			 * will not be populated by the initial database setup script (i.e. standalone script). */
			if (workingModule.getName() == null) {
				workingModule.setDescription(draftModule.getDescription());
				workingModule.setName(draftModule.getName());
				workingModule.setModuleUuid(draftModule.getModuleUuid());
				workingModule.setShowCheckAll(draftModule.getShowCheckAll());
				workingModule.setShowDdEmptyoptionText(draftModule.getShowDdEmptyoptionText());
			} else if (workingModule.getModuleUuid() == null) {
				workingModule.setModuleUuid(draftModule.getModuleUuid());
			}

			Set<FormTemplates> workingTemplates = workingModule.getFormTemplateses();
			Set<FormTemplatesDraft> draftTemplates = draftModule.getFormTemplatesDrafts();
			Set<FormTemplates> finalTemplates = new HashSet<FormTemplates>();

			logger.debug("Disconnecting " + workingTemplates.size() + " templates from module " + module + " before promoting.");
			for (FormTemplates workingTemplate : workingTemplates) {
				workingTemplate.setFormModules(null);
			}

			for (FormTemplatesDraft draftTemplate : draftTemplates) {
				logger.debug("Processing template: " + draftTemplate.getTemplateUuid() + " ....");

				boolean found = false;
				FormTemplates finalTemplate = null;

				logger.debug("Searching database for templates in module " + module + " that are not updated or new ....");
				for (FormTemplates workingTemplate : workingTemplates) {

					if (draftTemplate.getTemplateUuid().equals(workingTemplate.getTemplateUuid())
							&& draftTemplate.getGsfbChangeDate().equals(workingTemplate.getGsfbChangeDate())) {

						finalTemplate = workingTemplate;
						finalTemplate.setFormModules(workingModule);

						found = true;
						break;
					}
				}

				if (found) {
					logger.debug("Template: " + draftTemplate.getTemplateUuid() + " exists and has not been updated.");

					/* Compare draft template matrices with the master list on type, mech, and role:
					 * - If none of the master template matrices satisfies the condition,
					 * o Insert the draft template matrix
					 * - Otherwise, compare the draft template uuid with the working template uuid:
					 * - If the uuid's don't match (i.e. new template matrix for the working template),
					 * o insert the template matrix,
					 * - Otherwise continue processing the next draft template matrix. */
					Set<FormTemplatesMatrix> processedExistingTemplateMatrices = processExistingTemplateMatrix(masterTemplateMatrices, finalTemplate,
							draftTemplate.getFormTemplatesMatrixDrafts());
					finalTemplate.setFormTemplatesMatrixes(processedExistingTemplateMatrices);

					for (FormGrantMatrix formGrantMatrix : finalTemplate.getFormGrantMatrixes()) {
						formGrantMatrix.setFormTemplates(finalTemplate);
					}

				} else {
					logger.debug("Template: " + draftTemplate.getTemplateUuid() + " is new or has been updated.");

					finalTemplate = draftTemplate.toFormTemplate();

					// Linking back the template to its module
					// (At the initial deployment, all the templates are disconnected from their modules)
					workingModule.setFormTemplateses(finalTemplates);
					finalTemplate.setFormModules(workingModule);

					logger.debug("Populate Form Elements table for template: " + draftTemplate.getTemplateUuid());

					// Process elements
					List<FormElements> fes = new ArrayList<>();
					finalTemplate.setFormElementses(fes);
					Map<String, FormElements> mapElements = new HashMap<>();
					for (FormElementsDraft draftElement : draftTemplate.getFormElementsDrafts()) {
						FormElements formElement = draftElement.toFormElements(mapFinalQuestions, mapElements);
						mapElements.put(formElement.getElementUuid(), formElement);
						formElement.setFormTemplates(finalTemplate);
						fes.add(formElement);
					}

					logger.debug("Populate Form Templates Matrix table for template: " + draftTemplate.getTemplateUuid());

					/* Compare draft template matrices with the master list on type, mech, and role:
					 * - If the conditions are valid
					 * o Set the end date of the data in the database to the current date,
					 * o Insert the draft template matrix
					 * - Otherwise, insert the draft template matrix. */
					Set<FormTemplatesMatrix> processedUpdatedTemplateMatrices = processUpdatedTemplateMatrix(masterTemplateMatrices,
							draftTemplate.getFormTemplatesMatrixDrafts(), finalTemplate, startDate);
					finalTemplate.setFormTemplatesMatrixes(processedUpdatedTemplateMatrices);

					logger.debug("Populate Form Grant Matrix table for template: " + draftTemplate.getTemplateUuid());

					/* Compare draft grant matrices with the master list on type, mech, and role:
					 * - If none of the master list items satisfies the condition insert the draft grant matrix. */
					Set<FormGrantMatrix> processedGrantMatrix = processGrantMatrix(draftTemplate.getFormGrantMatrixDrafts(), 
							masterGrantMatrices, finalTemplate);
					finalTemplate.setFormGrantMatrixes(processedGrantMatrix);
				}

				finalTemplates.add(finalTemplate);

				logger.debug("Completed processing template: " + finalTemplate.getTemplateUuid());
			}

			logger.debug("Start persisting question data for: " + mapFinalQuestions.size() + " questions ....");
			for (FormQuestions fq : mapFinalQuestions.values()) {
				sessionFactory.getCurrentSession().persist(fq);
			}
			logger.debug("Completed persisting questions.");

			logger.debug("Start persisting " + finalTemplates.size() + " templates ....");
			workingModule.setFormTemplateses(finalTemplates);
			sessionFactory.getCurrentSession().persist(workingModule);
			logger.debug("Completed persisting templates.");

			for (FormTemplates workingFormTemplate : workingTemplates) {
				if (workingFormTemplate.getFormModules() == null) {
					sessionFactory.getCurrentSession().persist(workingFormTemplate);
				}
			}

		} catch (Exception e) {
			logger.error("Error promoting draft greensheets to production table: ", e);
			return false;
		}

		return true;
	}

	@Override
	public void loadInactiveMechType(String module, HashSet<String> inActiveList) {
		for (String item : inActiveList) {
			String[] itemsArray = item.split(",");
			String type = itemsArray[1];
			String mech = itemsArray[2];

			FormInactiveTypeMechId ftitmi = new FormInactiveTypeMechId(module, type, mech);
			FormInactiveTypeMech fitm = new FormInactiveTypeMech(ftitmi);
			sessionFactory.getCurrentSession().persist(fitm);
		}
	}

	@Override
	public void reActivedMechType(String module, String typeMech) {
		logger.debug("... reActivedMechType for module: " + module);

		String[] itemsArray = typeMech.split(",");
		String type = itemsArray[0];
		String mech = itemsArray[1];

		String query = "DELETE FROM FormInactiveTypeMech "
				+ "WHERE id.module = :module "
				+ "AND id.applTypeCode = :applTypeCode "
				+ "AND id.activityCode = :activityCode";
		sessionFactory.getCurrentSession().createQuery(query)
			.setParameter("module", module)
			.setParameter("applTypeCode", type)
			.setParameter("activityCode", mech)
			.executeUpdate();

		sessionFactory.getCurrentSession().flush();
	}

	private Set<FormTemplatesMatrix> processExistingTemplateMatrix(List<FormTemplatesMatrix> masterTemplateMatrices, FormTemplates workingTemplate,
			Set<FormTemplatesMatrixDraft> draftTemplateMatrices) {

		Set<FormTemplatesMatrix> templateMatrices = new HashSet<FormTemplatesMatrix>();

		for (FormTemplatesMatrixDraft draftTemplateMatrix : draftTemplateMatrices) {
			boolean found = false;
			for (FormTemplatesMatrix masterTemplateMatrix : masterTemplateMatrices) {
				if (draftTemplateMatrix.getActivityCode().equals(masterTemplateMatrix.getActivityCode())
						&& draftTemplateMatrix.getApplTypeCode().equals(masterTemplateMatrix.getApplTypeCode())
						&& draftTemplateMatrix.getFormRoleCode().equals(masterTemplateMatrix.getFormRoleCode())) {

					if (workingTemplate.getTemplateUuid().equals(masterTemplateMatrix.getFormTemplates().getTemplateUuid())) {
						templateMatrices.add(masterTemplateMatrix);
						found = true;
					}
					break;
				}
			}
			if (!found) {
				FormTemplatesMatrix formTemplateMatrix = draftTemplateMatrix.toFormTemplatesMatrix();
				formTemplateMatrix.setFormTemplates(workingTemplate);
				templateMatrices.add(formTemplateMatrix);
			}
		}

		return templateMatrices;
	}

	private Set<FormTemplatesMatrix> processUpdatedTemplateMatrix(List<FormTemplatesMatrix> masterTemplateMatrices,
			Set<FormTemplatesMatrixDraft> draftTemplateMatrices, FormTemplates finalTemplate, Date startDate) {

		Set<FormTemplatesMatrix> templateMatrices = new HashSet<FormTemplatesMatrix>();

		for (FormTemplatesMatrixDraft draftTemplateMatrix : draftTemplateMatrices) {
			for (FormTemplatesMatrix masterTemplateMatrix : masterTemplateMatrices) {
				if (draftTemplateMatrix.getActivityCode().equals(masterTemplateMatrix.getActivityCode())
						&& draftTemplateMatrix.getApplTypeCode().equals(masterTemplateMatrix.getApplTypeCode())
						&& draftTemplateMatrix.getFormRoleCode().equals(masterTemplateMatrix.getFormRoleCode())) {

					masterTemplateMatrix.setEndDate(startDate);
					sessionFactory.getCurrentSession().update(masterTemplateMatrix);

					break;
				}
			}

			FormTemplatesMatrix formTemplateMatrix = draftTemplateMatrix.toFormTemplatesMatrix();
			formTemplateMatrix.setStartDate(startDate);
			formTemplateMatrix.setFormTemplates(finalTemplate);
			templateMatrices.add(formTemplateMatrix);
		}

		return templateMatrices;
	}

	private Set<FormGrantMatrix> processGrantMatrix(Set<FormGrantMatrixDraft> draftGrantMatrices, List<FormGrantMatrix> masterGrantMatrices,
			FormTemplates finalTemplate) {

		Set<FormGrantMatrix> finalGrantMatrices = new HashSet<FormGrantMatrix>();

		for (FormGrantMatrixDraft formGrantMatrix : draftGrantMatrices) {
			boolean found = false;

			for (FormGrantMatrix masterGrantMatrix : masterGrantMatrices) {
				if (formGrantMatrix.getActivityCode().equals(masterGrantMatrix.getActivityCode())
						&& formGrantMatrix.getApplTypeCode().equals(masterGrantMatrix.getApplTypeCode())
						&& formGrantMatrix.getFormRoleCode().equals(masterGrantMatrix.getFormRoleCode())) {

					masterGrantMatrix.setFormTemplates(finalTemplate);
					sessionFactory.getCurrentSession().update(masterGrantMatrix);

					found = true;
					break;
				}
			}
			if (!found) {
				FormGrantMatrix fgm = formGrantMatrix.toFormGrantMatrix();
				fgm.setFormTemplates(finalTemplate);
				finalGrantMatrices.add(fgm);
			}
		}

		return finalGrantMatrices;
	}

}