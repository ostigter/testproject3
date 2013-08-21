package org.ozsoft.secs.message;

import org.ozsoft.secs.SecsException;
import org.ozsoft.secs.SecsParseException;
import org.ozsoft.secs.SecsReplyMessage;
import org.ozsoft.secs.format.A;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.L;

/**
 * S99F2 Greeting Acknowledge (GA) test reply message. <br />
 * <br />
 * 
 * Format:
 * <pre>
 * <L
 *      GRACK     // <B:1> 0x00 = Accept, 0x01 = Reject
 *      GREETING  // <A>
 * >
 * </pre>
 * 
 * @author Oscar Stigter
 */
public class S99F2 extends SecsReplyMessage {

    public static final int GRACK_ACCEPT = 0x00;
    
    public static final int GRACK_REJECT = 0x01;
    
    private static final int STREAM = 99;

    private static final int FUNCTION = 2;
    
    private static final boolean WITH_REPLY = false;
    
    private static final String DESCRIPTION = "Greeting Acknowledge (GA)";
    
    private Integer grAck;
    
    private String greeting;
    
    public int getGrAck() {
        return grAck;
    }
    
    public void setGrAck(int grAck) {
        this.grAck = grAck;
    }
    
    public String getGreeting() {
        return greeting;
    }
    
    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
    
    @Override
    public int getStream() {
        return STREAM;
    }

    @Override
    public int getFunction() {
        return FUNCTION;
    }

    @Override
    public boolean withReply() {
        return WITH_REPLY;
    }

    @Override
    public String getDescripton() {
        return DESCRIPTION;
    }

    @Override
    protected void parseData(Data<?> data) throws SecsParseException {
        if (data == null) {
            throw new SecsParseException("Missing data");
        }
        if (!(data instanceof L)) {
            throw new SecsParseException("Top-level data item must be an L");
        }
        L l = (L) data;
        if (l.length() != 2) {
            throw new SecsParseException("L must contain exactly 2 items");
        }
        data = l.getItem(0);
        if (!(data instanceof B)) {
            throw new SecsParseException("GRACK must be of type B");
        }
        B b = (B) data;
        if (b.length() != 1) {
            throw new SecsParseException("GRACK must have length of exactly 1");
        }
        int grAck = ((B) data).get(0);
        if (grAck != GRACK_ACCEPT && grAck != GRACK_REJECT) {
            throw new SecsParseException("Invalid value for GRACK: " + grAck);
        }
        data = l.getItem(1);
        if (!(data instanceof A)) {
            throw new SecsParseException("GREETING must be an A");
        }
        String greeting = ((A) data).getValue();
        if (greeting.isEmpty()) {
            throw new SecsParseException("Empty GREETING");
        }
        setGrAck(grAck);
        setGreeting(greeting);
    }

    @Override
    protected Data<?> getData() throws SecsParseException {
        // Validation checks.
        if (grAck == null) {
            throw new SecsParseException("GRACK not set");
        }
        if (greeting == null) {
            throw new SecsParseException("GREETING not set");
        }
        if (greeting.isEmpty()) {
            throw new SecsParseException("Empty GREETING");
        }
        
        L l = new L();
        l.addItem(new B(grAck));
        l.addItem(new A(greeting));
        return l;
    }

    @Override
    protected void handle() throws SecsException {
        // Not implemented.
    }

}
