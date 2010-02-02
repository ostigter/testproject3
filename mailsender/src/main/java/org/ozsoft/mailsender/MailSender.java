package org.ozsoft.mailsender;

/**
 * Mail sender interface.
 * 
 * @author Oscar Stigter
 */
public interface MailSender {
    
	/**
	 * Sends a mail.
	 * 
	 * @param from
	 *            The 'from' field (sender).
	 * @param to
	 *            The 'to' field (recipient).
	 * @param subject
	 *            The subject.
	 * @param body
	 *            The body.
	 */
	void send(String from, String to, String subject, String body);
    
}
