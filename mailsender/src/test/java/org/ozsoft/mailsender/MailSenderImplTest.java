package org.ozsoft.mailsender;

/**
 * Test driver for the MailSenderImpl.
 * 
 * @author Oscar Stigter
 */
public class MailSenderImplTest {

	public static void main(String[] args) {
		String from = "donotreply@nowhere.net";
		String to = "oscar.stigter@gmail.com";
		String subject = "test";
		String body = "This is a test mail.";

		MailSender mailSender = new MailSenderImpl();
		mailSender.send(from, to, subject, body);
	}

}
