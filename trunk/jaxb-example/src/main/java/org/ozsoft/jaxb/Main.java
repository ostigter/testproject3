package org.ozsoft.jaxb;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.example.message.Message;
import org.example.message.ObjectFactory;

public class Main {
    
    private static final File FILE = new File("message.xml");

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        try {
            JAXBContext jc = JAXBContext.newInstance("org.example.message");
            
            Message message = null;
            JAXBElement<Message> element = null;
            
            System.out.println("Unmarshalling message from file...");
            Unmarshaller um = jc.createUnmarshaller();
            message = ((JAXBElement<Message>) um.unmarshal(FILE)).getValue();
            String timestamp = message.getTimestamp();
            System.out.println("Text: " + message.getText());
            System.out.println("Old timestamp: " + timestamp);
            
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            timestamp = df.format(new Date());
            message.setTimestamp(timestamp);
            System.out.println("New timestamp: " + timestamp);

            System.out.println("Marshalling message to file...");
            ObjectFactory factory = new ObjectFactory();
            element = factory.createMessage(message);
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(element, FILE);
            
            System.out.println("Finished.");
            
        } catch (JAXBException e) {
            System.err.println("ERROR: " + e);
        }
    }

}
