package org.ozsoft.mail;

public interface MailSender {
    
    void send(String recipient, String subject, String body);

}
