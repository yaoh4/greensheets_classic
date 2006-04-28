/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.utils;

import java.io.*;
import java.util.*;


/**
 *
 *
 *
 *  @author kpuscas, Number Six Software
 */
public class AppConfigLoader {

    private void AppConigLoader() {
    }

    public static void initAppConfigProperties(String path) throws Exception {
        Properties p = new Properties();
        p.load(new FileInputStream(path + "greensheetconfig.properties"));
        AppConfigProperties.getInstance().addProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES, p);
    }


    public static void initDbProperties(String path) throws Exception{
        Properties p = new Properties();
        p.load(new FileInputStream(path + "db.properties"));
        AppConfigProperties.getInstance().addProperty(GreensheetsKeys.KEY_DB_PROPERTIES, p);
    }


    public static void initErrorMessages(String path) throws Exception { 
        Properties p = new Properties();
        p.load(new FileInputStream(path + "errormessages.properties"));
        AppConfigProperties.getInstance().addProperty(GreensheetsKeys.KEY_ERROR_MESSAGES, p);

    }

    public static void initPreferences(String path) throws Exception {
        Properties p = new Properties();
        p.load(new FileInputStream(path + "preferences.properties"));
        AppConfigProperties.getInstance().addProperty(GreensheetsKeys.KEY_USER_PREFERENCES, p);
    }


    public static void loadQuestionsXmlSrc(String path) throws Exception {


        File gsFormXmlTranslator = new File(path + "xmlxsltsrc/GsFormXmlTranslator.xslt");
        File gsFormToFoTranslator = new File(path + "xmlxsltsrc/GsFormToFoTranslator.xslt");

        AppConfigProperties.getInstance().addProperty("TEST_QUESTIONS_SRC", new File(path + "xmlxsltsrc/TEST_Questions.xml"));
        AppConfigProperties.getInstance().addProperty("PC_QUESTIONS_SRC_6.28.05", new File(path + "xmlxsltsrc/PC_Questions.6.28.05.xml"));
        AppConfigProperties.getInstance().addProperty("PNC_QUESTIONS_SRC_6.28.05", new File(path + "xmlxsltsrc/PNC_Questions.6.28.05.xml"));
        AppConfigProperties.getInstance().addProperty("SC_QUESTIONS_SRC_6.28.05", new File(path + "xmlxsltsrc/SC_Questions.6.28.05.xml"));
        AppConfigProperties.getInstance().addProperty("SNC_QUESTIONS_SRC_6.28.05", new File(path + "xmlxsltsrc/SNC_Questions.6.28.05.xml"));

        AppConfigProperties.getInstance().addProperty("PC_QUESTIONS_SRC_3.23.05", new File(path + "xmlxsltsrc/PC_Questions.3.23.05.xml"));
        AppConfigProperties.getInstance().addProperty("PNC_QUESTIONS_SRC_3.23.05", new File(path + "xmlxsltsrc/PNC_Questions.3.23.05.xml"));
        AppConfigProperties.getInstance().addProperty("SC_QUESTIONS_SRC_3.23.05", new File(path + "xmlxsltsrc/SC_Questions.3.23.05.xml"));
        AppConfigProperties.getInstance().addProperty("SNC_QUESTIONS_SRC_3.23.05", new File(path + "xmlxsltsrc/SNC_Questions.3.23.05.xml"));



        AppConfigProperties.getInstance().addProperty("FORM_XML_TRANSLATOR", gsFormXmlTranslator);
        AppConfigProperties.getInstance().addProperty("FORM_FO_TRANSLATOR", gsFormToFoTranslator);

    }

}
