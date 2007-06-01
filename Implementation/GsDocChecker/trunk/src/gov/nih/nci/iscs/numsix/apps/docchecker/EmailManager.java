package gov.nih.nci.iscs.numsix.apps.docchecker;

import gov.nih.nci.iscs.numsix.apps.docchecker.exception.DocCheckerException;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import org.apache.log4j.Logger;

public class EmailManager {
	private String smtpServer;

	private int smtpPort;

	public static Logger log = Logger.getLogger("gov.nih.nci.iscs.numsix.apps.docchecker.EmailManager");

	public EmailManager() {
	}

	public EmailManager(String smtpServer, int port) {
		this.smtpServer = smtpServer;
		this.smtpPort = port;
	}

	public int sendFile(List emailRecipients, String sender, String subject, String message, String fileName) throws DocCheckerException {
		log.debug("Inside sendFile() method");
		String subscriber;
		InternetAddress emailAddress = null;
		InternetAddress[] reportRecipientsEmails;
		int recipientCount = 0;
		
		// Checks to see if the connection parameters to the SMTP server are available.
		if (this.smtpServer == null ||
			this.smtpServer.trim().equals("") || 
			this.smtpPort == 0 ||
			sender == null ||
			sender.trim().equals("") ||
			emailRecipients == null ||
			emailRecipients.size() == 0
			) {
			log.debug("args to sendFile() method are incorrect.");
//			return IConstants.FAILURE;
			throw new DocCheckerException("Either SMTP configuration information is missing or Email sender/recipients are missing.");
		}
		log.debug("args to sendFile() method appear to be good.");
		reportRecipientsEmails = new InternetAddress[emailRecipients.size()];
		Session session = getSession();
		log.debug("Mail session obtained.");
		
		try {
			log.debug("Constructing the email message inside the sendFile() method...");
			// create a message
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(sender));
			
			log.debug("Forming the recipients' email addresses");
			for (int index = 0; index < emailRecipients.size(); index++) {
				emailAddress = null;
				subscriber = (String) emailRecipients.get(index);
				if (subscriber != null && !(subscriber.trim().equals(""))) {
					emailAddress = new InternetAddress(subscriber);
					reportRecipientsEmails[recipientCount++] = emailAddress; 
				}
			}

			msg.setRecipients(Message.RecipientType.TO, reportRecipientsEmails);
			
			log.debug("Setting the subject in the email...");
			msg.setSubject(subject);
			
			log.debug("Setting the message in the email...");
			// create and fill the first message part
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setText(message);

			// create the second message part
			MimeBodyPart mbp2 = new MimeBodyPart();

			// attach the file to the message
			log.debug("Attaching the file to the email...");
			FileDataSource fds = new FileDataSource(fileName);
			mbp2.setDataHandler(new DataHandler(fds));
			mbp2.setFileName(fds.getName());

			// create the Multipart and add its parts to it
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			mp.addBodyPart(mbp2);

			// add the Multipart to the message
			msg.setContent(mp);

			// set the Date: header
			msg.setSentDate(new Date());

			// send the message
			log.debug("Sending the email out...");
			Transport.send(msg);

		} catch (MessagingException mex) {
			log.debug("Exception occurred inside the sendFile() method." + mex.getMessage());
			mex.printStackTrace();
			Exception ex = null;
			if ((ex = mex.getNextException()) != null) {
				ex.printStackTrace();
			}
//			return IConstants.FAILURE;
			throw new DocCheckerException("Exception occurred while sending out the emails with the report." + mex.getMessage());
		}
		
		return IConstants.SUCCESS;
	}

	public int getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	public Session getSession() {
		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpServer);

		Session session = Session.getInstance(props, null);

		return session;

	}
}