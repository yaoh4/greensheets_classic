package gov.nih.nci.cbiit.scimgmt.greensheets.qstnfrmanalyzer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.apache.log4j.PropertyConfigurator;


public class Application {

	static final String CONFIG_FILE_LOCATION = "config";
	static final String LOG4J_CONFIG_FILE_NAME = "log4j.properties";
	
	public static void main(String[] args) {

		String log4ConfigFilePath = System.getProperty("user.dir") +
				System.getProperty("file.separator") +
				CONFIG_FILE_LOCATION +
				System.getProperty("file.separator") +
				LOG4J_CONFIG_FILE_NAME;
		System.out.println("Configuring Log4j from: " + log4ConfigFilePath);
		PropertyConfigurator.configure(log4ConfigFilePath);

		ApplicationContext springContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		FlowController flowController = springContext.getBean("flowController", FlowController.class);

		if (args != null && args.length > 0 && "-postprocess".equalsIgnoreCase(args[0])) {
			flowController.postProcess();
		}
		else {
			flowController.execute();
		}
	}

}
