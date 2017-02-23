package gov.nih.nci.cbiit.scimgmt.gsdb;

import gov.nih.nci.cbiit.scimgmt.gsdb.dao.GSFormsDAO;
import gov.nih.nci.cbiit.scimgmt.gsdb.model.WorkingTemplates;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class Main {

    @Autowired
    GSFormsDAO gsFormsDAO;

    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
        Main mainClass = ctx.getBean(Main.class);
        mainClass.migrateData();
    }

    private void migrateData() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            logger.info("Data Migration - Starting at " + dateFormat.format(date) + " ...");
            List<WorkingTemplates> templateMatrix = gsFormsDAO.populateFormTemplateMatrix();
            if (templateMatrix == null) {
                templateMatrix = gsFormsDAO.retrieveFormTemplateMatrix();
            }
            gsFormsDAO.populateModules();
            // Update relationship between FORMS_T records and FORM_TEMPLATE_MATRIX_T records
            gsFormsDAO.populateFormsToMatrixLinks();

            int currentIndex = 0;
            while (currentIndex >= 0) {
                currentIndex = gsFormsDAO.extractTemplateContent(templateMatrix, currentIndex);
            }
            date = new Date();
            logger.info("Data Migration - Completed successfully at " + dateFormat.format(date) + " .");
            System.exit(0);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        System.out.println("Done");
    }
}
