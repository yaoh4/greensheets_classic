package gov.nih.nci.iscs.numsix.greensheets.utils;

import gov.nih.nci.cbiit.scimgmt.common.service.impl.EmailService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;
import org.springframework.mail.SimpleMailMessage;

public class EmailNotification {

    private static final Logger logger = Logger
            .getLogger(EmailNotification.class);

    private static final SimpleDateFormat timeFormatter =
            new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

    private String sendingOfEmailsEnabled = "";
    private String environment = "";

    private SimpleMailMessage mailTemplate;
    private SimpleMailMessage questionMailTemplate;
    private EmailService emailService;

    /**
     * Sends an e-mail to support e-mail address with arbitrary text, not derived from the content of a particular exception.
     * 
     * @param text
     *            - text of the message to sent in the e-mail.
     */
    public synchronized void sendTextToSupportEmail(StringBuffer text) {

        if (sendingOfEmailsEnabled != null) {
            if (!Boolean.parseBoolean(sendingOfEmailsEnabled))
                return;
        }

        environment = DbConnectionHelper.getDbEnvironment();
        if (environment == null) {
            environment = "";
        }

        if (emailService != null && emailService.getMailSender() != null && mailTemplate != null) {
            try {
                // Instantiate a message
                SimpleMailMessage emailMsg = new SimpleMailMessage(mailTemplate);
                emailMsg.setSentDate(new Date());
                String envString = "[GS";
                if (environment != null && !"".equals(environment) && !environment.toUpperCase().contains("PROD")) {
                    envString = envString + " - " + environment;
                }
                envString += " " + timeFormatter.format(new Date()) + "]";
                emailMsg.setSubject(envString + " ISSUE: Redundant grant records detected when retrieving a greensheet");

                text.insert(0, "This is an automated message from Greensheets application.\n" +
                        "It was sent without a user's direct participation. Its purpose is to notify the " +
                        "Greensheets support team of a problematic condition detected in the database:\n\n");
                text.append("\n\n");
                text.append("\nNCI CBIIT - Rockville, MD");

                emailMsg.setText(text.toString());
                emailService.getMailSender().send(emailMsg);
            } catch (Throwable e) {
                logger.error(" + + + +  The following error occurred when composing an \n\t" +
                        "e-mail message to the Support e-mail address about redundant " +
                        "GPMATS actions: \n\t" + e);
            }
        }
    }

    /**
     * Sends an e-mail to support e-mail address with arbitrary text, not derived from the content of a particular exception.
     * 
     * @param text
     *            - text of the message to sent in the e-mail.
     */
    public synchronized void sendPostProcessEmail(String subject, String content, String comments, String module, String action) {

        if (sendingOfEmailsEnabled != null) {
            if (!Boolean.parseBoolean(sendingOfEmailsEnabled))
                return;
        }

        environment = DbConnectionHelper.getDbEnvironment();
        if (environment == null) {
            environment = "";
        }

        if (emailService != null && emailService.getMailSender() != null && questionMailTemplate != null) {
            try {
                // Instantiate a message
                SimpleMailMessage emailMsg = new SimpleMailMessage(questionMailTemplate);
                emailMsg.setSentDate(new Date());
                String muduleName = "";
                if (module.trim().equalsIgnoreCase("PC"))
                    muduleName = "Program Competing";
                if (module.trim().equalsIgnoreCase("PNC"))
                    muduleName = "Program Non Competing";
                if (module.trim().equalsIgnoreCase("SC"))
                    muduleName = "Specialist Competing";
                if (module.trim().equalsIgnoreCase("SNC"))
                    muduleName = "Specialist Non Competing";

                StringBuffer emailContent = new StringBuffer();

                String envString = "[GS";
                if (environment != null && !"".equals(environment) && !environment.toUpperCase().contains("PROD")) {
                    envString = envString + " - " + environment;
                }
                envString += " " + timeFormatter.format(new Date()) + "]";
                emailMsg.setSubject(envString + subject);

                emailContent.append(content + "\n\n");
                emailContent.append("\n");
                if (action.equalsIgnoreCase("processDraft")) {
                	emailContent.append("Comments:\n");
                	emailContent.append(comments + "\n");
                	emailContent.append("\n");
                    emailContent.append("Following were updated:\n");
                    emailContent.append("Module: " + muduleName + "\n");
                    emailContent.append("\n");
                    
                    emailContent.append("\n\n");
                }
                if(action.equalsIgnoreCase("rejectDraft")){
                    emailContent.append("The " + muduleName + " Draft Greensheets were rejected."); 
                    
                }
                
                String greensheetsURL = ((Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES)).getProperty("url.greensheet");
                
                emailContent.append("\nGreensheets Application URL: " + greensheetsURL);

                emailMsg.setText(emailContent.toString());
                emailService.getMailSender().send(emailMsg);
            } catch (Throwable e) {
                logger.error(" + + + +  The following error occurred when composing an \n\t" +
                        "e-mail message to the Support e-mail address about redundant " +
                        "GPMATS actions: \n\t" + e);
            }
        }
    }

    private static InternetAddress[] getEmailAddresses(String commaSeperatedAEmails) throws Exception {
        InternetAddress[] emails = null;
        if (commaSeperatedAEmails != null) {
            String[] addresses = commaSeperatedAEmails.split(",");
            emails = new InternetAddress[addresses.length];
            for (int i = 0; i < addresses.length; i++) {
                emails[i] = new InternetAddress(addresses[i]);
            }
        }
        return emails;
    }

    private static String paddUnderscore(int n) {
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < n; i++) {
            sb.append("_");
        }
        return sb.toString();
    }

    public SimpleMailMessage getMailTemplate() {
        return mailTemplate;
    }

    public void setMailTemplate(SimpleMailMessage mailTemplate) {
        this.mailTemplate = mailTemplate;
    }

    public SimpleMailMessage getQuestionMailTemplate() {
        return questionMailTemplate;
    }

    public void setQuestionMailTemplate(SimpleMailMessage questionMailTemplate) {
        this.questionMailTemplate = questionMailTemplate;
    }

    public EmailService getEmailService() {
        return emailService;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public String getSendingOfEmailsEnabled() {
        return sendingOfEmailsEnabled;
    }

    public void setSendingOfEmailsEnabled(String sendingOfEmailsEnabled) {
        this.sendingOfEmailsEnabled = sendingOfEmailsEnabled;
    }

}
