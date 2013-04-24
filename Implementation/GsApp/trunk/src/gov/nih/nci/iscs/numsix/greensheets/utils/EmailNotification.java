package gov.nih.nci.iscs.numsix.greensheets.utils;

import gov.nih.nci.iscs.numsix.greensheets.application.GreensheetFormSession;
import gov.nih.nci.iscs.numsix.greensheets.application.GreensheetUserSession;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.NciPerson;
import gov.nih.nci.salient.framework.service.impl.EmailService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.DynaActionForm;
import org.springframework.mail.SimpleMailMessage;

public class EmailNotification {

    private static final Logger logger = Logger
            .getLogger(EmailNotification.class);

    private static final SimpleDateFormat timeFormatter = 
    		new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
    
    private String sendingOfEmailsEnabled = "";
    private String environment = "";
    
    private SimpleMailMessage mailTemplate;
    private EmailService emailService;

    /*
    public static Properties appProperties = (Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES);
    public String sendEmail = appProperties.getProperty("gs_send_mail");
    public static String from = appProperties.getProperty("gs_email_from");
    public static String to = appProperties.getProperty("gs_error_email_to");
    public static String host = appProperties.getProperty("smtp_server");
    public static String environment = appProperties.getProperty("enviroment");
    */

//    public void sendEmailNotification(Exception exception, HttpServletRequest req) {
//
//    	String from="";
//    	
//        if (sendingOfEmailsEnabled != null) {
//            if (!Boolean.parseBoolean(sendingOfEmailsEnabled))
//                return;
//        }
//        environment = DbConnectionHelper.getDbEnvironment();
//        if (environment==null)  { environment = ""; }
//
//        if (!req.getSession().isNew()) {
//
//            /* We are going to send an e-mail to the "Support" e-mail address, about some 
//             * exception that occurred, and we would like to include in it, if possible, 
//             * known information about the grant and the specific greensheet with which the 
//             * user may have been working.  Which sounds simple, but is quite a pain in the 
//             * rear to get at!  (Part of the problem is that the exception might occur when
//             * the user is NOT working with a specific greensheet or grant; another part is
//             * how complicated the way is in which these objects are stored.)  
//             */
//
//            String formUID = "";
//            FormGrantProxy grant = null;
//            GreensheetFormProxy gsFormObject = null;
//            NciPerson nciPerson = null;
//
//            /* ****************************************************************************/
//            /* When we are here, we don't know during which action's processing the exception 
//             * occurred (about which we want to send an e-mail), what form was (perhaps) being
//             * submitted, what may or may not be in the session... We might come here from 
//             * almost anywhere. So, we are going to loop through request attributes (attributes
//             * not parameters - i.e., objects not Strings) and look at their *types* (types not
//             * names). We will be looking for request attributes that are of Struts' 
//             * DynaActionForm class. (They could be stored under any name though - that's why
//             * we can't rely on names.) If we found some DynaActionForm among request attributes, 
//             * we will go through that form's dynamic properties and see if among them there 
//             * is a property whose name is "formUID".  If there is, and it's not blank - awesome!   
//             * */
//            /* ****************************************************************************/
//            boolean foundAformUid = false;
//            if (req.getAttributeNames() != null) {
//                Enumeration<String> reqAttribNames = req.getAttributeNames();
//                while (reqAttribNames.hasMoreElements() && !foundAformUid) {
//                    String reqAttrName = reqAttribNames.nextElement();
//
//                    Object reqAttribObj = req.getAttribute(reqAttrName);
//                    if (reqAttribObj instanceof DynaActionForm) {
//                        DynaActionForm someStrutsDynaForm = (DynaActionForm) reqAttribObj;
//                        if (someStrutsDynaForm.getMap() != null) {
//                            for (Object o : someStrutsDynaForm.getMap().keySet()) {
//                                if (o instanceof String) {
//                                    String dynaFormPropName = (String) o;
//                                    if (dynaFormPropName.toUpperCase().equals("FORMUID")) {
//                                        Object formPropertyObject = ((DynaActionForm) reqAttribObj).get(dynaFormPropName);
//                                        if (formPropertyObject != null && formPropertyObject instanceof String) {
//                                            formUID = ((String) formPropertyObject);
//                                            foundAformUid = true;
//                                            break;
//                                        }
//                                    }
//                                }
//                            } // end of FOR loop that goes through the DynaActionForm's properties
//                        }
//                    } // end of what we do if we found a DynaActionForm
//                }
//            }
//
//            logger.debug(" - = +  FORM UID is: " + formUID);
//
//            /* ****************************************************************************/
//            /* Now, using the formUID that was so difficult to obtain, we can go hunting 
//             * for two objects from which we will get the info we so want to put into the 
//             * e-mail: one is an object representing a grant, and one representing a particular
//             * greensheet form for that grant (such as a Program greensheet or a Specialist 
//             * greensheet).  To get to those, we will look in the standard HTTP session for 
//             * an object of GreensheetUserSession class; if we have it, it should have a 
//             * child object of GreensheetFormSession class.  That one, in turn, has two 
//             * child objects, accessible through normal getters, which are what we look for: 
//             * the grant and the form!! */
//            /* ****************************************************************************/
//
//            if (req.getSession().getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION) != null
//                    && req.getSession().
//                            getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION)
//                    instanceof GreensheetUserSession) {
//                GreensheetUserSession gsSession =
//                        (GreensheetUserSession) req.getSession().getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
//
//                if (foundAformUid && formUID != null && !"".equals(formUID)) {
//                    Object o1 = gsSession.getGreensheetFormSession(formUID);
//                    if (o1 != null && o1 instanceof GreensheetFormSession) {
//                        grant = ((GreensheetFormSession) o1).getGrant();
//                        gsFormObject = ((GreensheetFormSession) o1).getForm();
//                    }
//                }
//                /*
//                 * Yaaay, now we should have info about the grant and the greensheet form ready
//                 * to include in the e-mail (unless they are remained null despite our best 
//                 * efforts, of course)...
//                 * So let's proceed with gathering some more info - about the user themselves. 
//                 */
//                GsUser userObject = gsSession.getUser();
//                if (userObject != null) {
//                    nciPerson = userObject.getNciPerson();
//                }
//
//                String grantId = req.getParameter(GreensheetsKeys.KEY_GRANT_ID);
//                String applId = req.getParameter(GreensheetsKeys.KEY_APPL_ID);
//                String groupType = req.getParameter(GreensheetsKeys.KEY_GS_GROUP_TYPE);
//                String gsFormStatus = "";
//                /* If the above things are coming in as request parameters, awesome, but a lot of the
//                 * time they would not be in request parameters - they would be stored in objects 
//                 * saved in the session, in complicated ways described above, when handling an 
//                 * earlier request related to the same grant/greensheet. So... 
//                 */
//
//                if (grantId == null || "".equals(grantId)) {
//                    if (grant != null && grant.getFullGrantNum() != null) {
//                        grantId = grant.getFullGrantNum(); // awesome - we need it!
//                    }
//                }
//                if (applId == null || "".equals(applId)) {
//                    if (grant != null && grant.getApplID() != null) {
//                        applId = grant.getApplID(); // sweet!
//                    }
//                }
//                if (groupType == null || "".equals(groupType)) {
//                    if (gsFormObject != null && gsFormObject.getFormRoleCode() != null) {
//                        groupType = gsFormObject.getFormRoleCode(); // ding!ding!ding!
//                    }
//                }
//                if (gsFormObject != null && gsFormObject.getFormStatus() != null) {
//                    gsFormStatus = gsFormObject.getFormStatus(); // this is also good to have
//                }
//
//                /* 
//                 * OK, in some cases we can't get to a specific GreensheetsForm by finding a formUID
//                 * request attribute... But there still might one (or more!) in GreensheetsUserSession...
//                 */
//                if (formUID == null || "".equals(formUID)) {
//                    Map map = gsSession.getGreensheetFormSessions();
//                    int formsFound = 0;
//                    if (map != null) {
//                        StringBuffer grantNumbers = new StringBuffer();
//                        StringBuffer applIDs = new StringBuffer();
//                        for (Object o3 : map.keySet()) {
//                            GreensheetFormSession gsFormSess = (GreensheetFormSession) map.get(o3);
//                            FormGrantProxy g = gsFormSess.getGrant();
//                            GreensheetFormProxy f = gsFormSess.getForm();
//                            if (f != null) {
//                                foundAformUid = true;
//                                formsFound++;
//                                if (formsFound > 1) {
//                                    grantNumbers.append(", ");
//                                    applIDs.append(", ");
//                                }
//                                if (formsFound <= 10) {
//                                    grantNumbers.append(g.getFullGrantNum());
//                                    if (f.getFormRoleCode() != null || !"".equals(f.getFormRoleCode())) {
//                                        grantNumbers.append(" (").append(f.getFormRoleCode()).append(")");
//                                    }
//                                    applIDs.append(g.getApplID());
//                                }
//                            }
//                        } // end of iterating through map of form-sessions in UserSession
//                        if (formsFound > 10) {
//                            grantNumbers.append(" and ").append(formsFound - 10).append(" more...");
//                            applIDs.append(" and ").append(formsFound - 10).append(" more...");
//                        }
//                        if (formsFound > 0 && (grantId == null || "".equals(grantId))) {
//                            grantId = grantNumbers.toString();
//                            applId = applIDs.toString();
//                        }
//                    }
//                }
//
//                /* Okay, perhaps the exception occurred when there were no "grant" and 
//                 * "greensheet form" objects saved in the session... There is still one 
//                 * more place where we can look in the hopes of finding at least the  
//                 * grant info, if not form info...
//                 */
//                boolean grantNumberCameFromSearchresults = false;
//                FormGrantProxy grantFromSearchResultsList = null;
//                if ((applId == null || "".equals(applId)) &&
//                        (grantId == null || "".equals(grantId))) {
//                    Object sessionGrantListAttrib = req.getSession().getAttribute("GRANT_LIST");
//                    if (sessionGrantListAttrib instanceof List &&
//                            ((List) sessionGrantListAttrib).size() == 1) {
//                        // Only one grant in grants list - sign that when search for grants
//                        // was done, it was possibly by grant number and returned only one match            		
//                        Object probablyAGrantObject = ((List) sessionGrantListAttrib).get(0);
//                        if (probablyAGrantObject instanceof FormGrantProxy) {
//                            grantFromSearchResultsList = (FormGrantProxy) probablyAGrantObject;
//                            if (grantFromSearchResultsList != null && grantFromSearchResultsList.getFullGrantNum() != null) {
//                                grantId = grantFromSearchResultsList.getFullGrantNum();
//                                grantNumberCameFromSearchresults = true;
//                            }
//                            if (grantFromSearchResultsList != null && grantFromSearchResultsList.getApplID() != null) {
//                                applId = grantFromSearchResultsList.getApplID();
//                                grantNumberCameFromSearchresults = true;
//                            }
//                        }
//                    }
//                } // End of trying to get a grant number from the list of grants saved in the session
//                  // after the user did a search(?).
//
//                String fullName = null;
//
//                if (nciPerson != null) {
//                    String sender = nciPerson.getEmail();
//                    if (sender != null) {
//                        from = sender;
//                    }
//                    fullName = nciPerson.getFullName();
//                }
//
//                /*
//                 * We are finally done with getting (if we can) all the information we'd like to
//                 * include in the e-mail message, and here we start actually putting the message 
//                 * together. 
//                 */
//                Properties props = new Properties();
//                props.put("mail.smtp.host", host);
//                Session mailSession = Session.getInstance(props);
//                StringBuffer sb = new StringBuffer("");
//                String errorTime = DateFormat.getInstance().format(new Date());
//                String envString = environment.equalsIgnoreCase("Production") ? "[GS]" : "[GS-" + environment.toUpperCase() + "]";
//                
//                SimpleMailMessage message = new SimpleMailMessage(mailTemplate);
//
//                try {
//                    // Instantiate a message
//                    Message msg = new MimeMessage(mailSession);
//
//                    //Set message attributes
//                    if (from.contains(";")) {
//
//                        int index = from.indexOf(";");
//                        if (index > 0) {
//                            from = from.substring(0, index);
//                        }
//                    }
//
//                    InternetAddress[] fromAddress = InternetAddress.parse(from, false);
//                    msg.addFrom(fromAddress);
//                    InternetAddress[] addresses = getEmailAddresses(to);
//                    msg.setRecipients(Message.RecipientType.TO, addresses);
//                    msg.setSentDate(new Date());
//
//                    sb.append("This is  an automated message from Greensheets application.");
//                    sb.append("\n\nIt may be sent on behalf of some user, but even then, it is sent automatically by the system.\n");
//
//                    msg.setSubject(envString + " Error in Greensheets application  " + errorTime);
//                    sb.append("\n\nDetails :");
//                    sb.append("\n" + paddUnderscore(50) + "\n");
//
//                    if (fullName != null) {
//                        sb.append("\n\nUser Name: " + fullName);
//                    } else {
//                        sb.append("\n\nUser Name: Not Available");
//                    }
//                    if (applId != null) {
//                        sb.append("\n\nApplication ID: " + applId);
//                        if (grantNumberCameFromSearchresults) {
//                            sb.append(" (probably taken from the results of a search for grant(s))");
//                        }
//                    } else {
//                        sb.append("\n\nApplication ID: Not Available");
//                    }
//                    if (grantId != null) {
//                        sb.append("\n\nGrant Number: " + grantId);
//                        if (grantNumberCameFromSearchresults) {
//                            sb.append(" (probably taken from the results of a search for grant(s))");
//                        }
//                    } else {
//                        sb.append("\n\nGrant Number: Not Available");
//                    }
//                    if (groupType != null) {
//                        sb.append("\n\nGreensheet Type: " + groupType);
//                    } else {
//                        sb.append("\n\nGreensheet Type: Not Available");
//                    }
//                    sb.append("\n\nGreensheet form status: ").append(
//                            gsFormStatus != null ? gsFormStatus : " Not availabe");
//
//                    sb.append("\n\n" + "TECHNICAL INFORMATION: " + "\n\n");
//                    sb.append(message);
//
//                    sb.append("\n\n\n\n");
//                    sb.append("\nNCI CBIIT - Rockville, MD");
//                    msg.setText(sb.toString());
//                    Transport.send(msg);
//                } catch (Throwable e) {
//                    logger.error(" + + + +  The following error occurred when composing an \n\t" +
//                            "e-mail message to the Support e-mail address about some other " +
//                            "exception: \n\t" + e);
//                }
//            } // End of what we do if we have a not-null GreensheetsUserSession object
//        } //  End of what we do if the session is not new  
//        else {
//            /* If this request was a session-establishing request (which is likely to be after a 
//             * session timeout besides a "really" brand-new request), helpful information about the 
//             * user, the grant and the form isn't going to be available yet anyway, so there is 
//             * little point in trying to send an e-mail. 
//             * */
//            return;
//        }
//    }

    
    /**
     * Sends an e-mail to support e-mail address with arbitrary text, not derived from 
     * the content of a particular exception.
     * @param text - text of the message to sent in the e-mail.
     */
    public  synchronized  void sendTextToSupportEmail(StringBuffer text) {

    	if (sendingOfEmailsEnabled != null) {
            if (!Boolean.parseBoolean(sendingOfEmailsEnabled))
                return;
        }
    	
    	environment = DbConnectionHelper.getDbEnvironment();
    	if (environment==null)  { environment = ""; }
    	
    	if (emailService!=null && emailService.getMailSender()!=null && mailTemplate!=null) {
            try {
                // Instantiate a message
            	SimpleMailMessage emailMsg = new SimpleMailMessage(mailTemplate);
            	emailMsg.setSentDate(new Date());
            	String envString = "[GS";
            	if (environment!=null && !"".equals(environment) && !environment.toUpperCase().contains("PROD")) {
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
            }
            catch (Throwable e) {
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
