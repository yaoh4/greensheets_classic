package gov.nih.nci.cbiit.scimgmt.gs.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import gov.nih.nci.cbiit.scimgmt.gs.dao.FormUtilDAO;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormQuestionsDraft;

public class FormUtilDAOImpl implements FormUtilDAO {
	private static final Log log = LogFactory.getLog(FormUtilDAOImpl.class);

    private static SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}
		
	public boolean delete(Object obj, Integer id) {
		boolean success = false;
		log.debug("deleting " + obj.getClass().getName() + "  instance");
		try {
			Session session = sessionFactory.getCurrentSession();
			Object loadedObj = session.load(obj.getClass(), id);
			session.delete(loadedObj);

			log.debug("delete successful for ID: " + id);
			success = true;
		} catch (RuntimeException re) {
			log.error("delete failed for ID: " + id, re);
			throw re;
		}
		return success;
	}
	
	public boolean persist(Object obj) {
		boolean success = false;
		log.debug("persisting " + obj.getClass().getName() + " instance");
		try {
			sessionFactory.getCurrentSession().persist(obj);
			log.debug("persist successful");
			success = true;
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
		return success;
	}

	public <T> List<T> findByUniqueId(Class<T> clazz, String columnName, String uuid) {
		log.debug("getting " + clazz.getName() + " instance with unique id: " + uuid);
		try {
			Session session = sessionFactory.getCurrentSession();
			
/*			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<T> criteria = builder.createQuery(clazz);
			Root<T> root = criteria.from(clazz);
			criteria.select(root);
			criteria.where(builder.equal(root.get(columnName), uuid));
			List<T> resultList = session.createQuery(criteria).getResultList();
*/
			Criteria criteria = session.createCriteria(clazz);
			List<T> resultList = criteria.add(Restrictions.like(columnName, uuid)).list();
			sessionFactory.getCurrentSession().flush();
			
			if (resultList.size() == 0) {
				log.debug("get successful, no instance found");
			} else {				
				log.debug("get successful, instance found");
			}
			return resultList;
		
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void deleteQuestions() {
		log.debug("Removing orphan questions and answers, with no elements, from draft tables ...");
		String query = "select fqd from FormQuestionsDraft as fqd where id not in (select formQuestionsDraft.id from FormElementsDraft)";
		List<FormQuestionsDraft> fqds = sessionFactory.getCurrentSession().createQuery(query).list();
		
		for (FormQuestionsDraft fqd : fqds) {
			Integer id = fqd.getId();
			FormQuestionsDraft fqdTmp = new FormQuestionsDraft();
			fqdTmp.setId(id);
			delete(fqdTmp, id);
		}
		sessionFactory.getCurrentSession().flush();
	}
}
