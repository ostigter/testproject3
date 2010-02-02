package org.ozsoft.mailsender;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

/**
 * Mail sender implementation with the Java Mail API and an SMTP server.
 * 
 * @author Oscar Stigter
 */
public class MailSenderImpl implements MailSender {

	/** Properties file. */
	private static final String PROPERTIES_FILE = "/smtp.properties";
	
	/** Log. */
	private static final Logger LOG = Logger.getLogger(MailSenderImpl.class);
	
	/** Java Mail properties. */
	private final Properties properties;
	
	/**
	 * Constructor.
	 */
	public MailSenderImpl() {
		properties = readPropertiesFile();
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.mailsender.MailSender#send(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void send(String from, String to, String subject, String body) {
		LOG.debug(String.format("Creating mail from '%s' to '%s' with subject '%s'", from, to, subject));

		final String smtpUsername = properties.getProperty("mail.smtp.username", "");
		final String smtpPassword = properties.getProperty("mail.smtp.password", "");
		Authenticator authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(smtpUsername, smtpPassword);
			}
		};

		Session session = Session.getInstance(properties, authenticator);
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			message.setRecipient(RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			Multipart content = new MimeMultipart();
			BodyPart bodyPart = new MimeBodyPart();
			bodyPart.setContent(body, "text/plain");
			content.addBodyPart(bodyPart);
			message.setContent(content);
			
			LOG.debug("Sending mail");
			Transport.send(message);
			LOG.debug("Mail sent.");
		} catch (AddressException e) {
			System.err.println(e);
		} catch (MessagingException e) {
			System.err.println(e);
		}
	}

	/**
	 * Reads the properties file.
	 * 
	 * @return The properties.
	 */
	private static Properties readPropertiesFile() {
		Properties properties = new Properties();
		try {
			InputStream is = MailSenderImpl.class.getResourceAsStream(PROPERTIES_FILE);
			if (is != null) {
				properties.load(is);
			} else {
				String msg = "Properties file not found";
				LOG.error(msg);
				throw new RuntimeException(msg);
			}
		} catch (IOException e) {
			String msg = "Error reading properties file";
			LOG.error(msg, e);
			throw new RuntimeException(msg, e);
		}
		return properties;
	}

}
