package gov.nih.nci.iscs.numsix.greensheets.services;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GreensheetsFacade implements ServletContextListener {
	private ApplicationContext springAppContext;

	public ApplicationContext getSpringAppContext() {
		return springAppContext;
	}

	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		   ServletContext context = sce.getServletContext();
		   
		   springAppContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		   
		   context.setAttribute("GreensheetsFacade", this);
	}
}
