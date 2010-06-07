package org.ozsoft.mail;

public class Main {

    public static void main(String[] args) {
        MailSender mailSender = new MailSenderImpl();
        System.out.println("Sending mail...");
        mailSender.send("oscar.stigter@gmail.com", "Test", "This is a test mail.\n");
        System.out.println("Done.");
    }
    
}
