package org.ozsoft.secs.message;

import org.ozsoft.secs.SecsParseException;
import org.ozsoft.secs.SecsPrimaryMessage;
import org.ozsoft.secs.SecsReplyMessage;
import org.ozsoft.secs.format.A;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.L;

/**
 * S99F1 Greeting Request test primary message. <br />
 * <br />
 * 
 * Returns a greeting based on the specified name. <br />
 * <br />
 * 
 * Format:
 * <pre>
 * <L
 *      NAME     // <A>
 * >
 * </pre>
 * 
 * @author Oscar Stigter
 */
public class S99F1 extends SecsPrimaryMessage {

    private static final int STREAM = 99;

    private static final int FUNCTION = 1;

    private static final boolean WITH_REPLY = true;
    
    private static final String DESCRIPTION = "Greeting Request (GR)";

    private static final String GREETING = "Hello, %s!";
    
    private String name;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
            throw new SecsParseException("Data not set");
        }
        if (!(data instanceof L)) {
            throw new SecsParseException("Root data item must be of type L");
        }
        L l = (L) data;
        if (l.length() != 1) {
            throw new SecsParseException("L must have length of exactly 1 item");
        }
        data = l.getItem(0);
        if (!(data instanceof A)) {
            throw new SecsParseException("NAME must be of type A");
        }
        String name = ((A) data).getValue();
        if (name.isEmpty()) {
            throw new SecsParseException("Empty NAME");
        }
        setName(name);
    }

    @Override
    protected Data<?> getData() throws SecsParseException {
        if (name == null) {
            throw new SecsParseException("NAME not set");
        }
        if (name.isEmpty()) {
            throw new SecsParseException("Empty NAME");
        }
        
        L l = new L();
        l.addItem(new A(name));
        return l;
    }

    @Override
    protected SecsReplyMessage handle() {
        // Always accept any request.
        String greeting = String.format(GREETING, name);
        S99F2 s99f2 = new S99F2();
        s99f2.setGrAck(S99F2.GRACK_ACCEPT);
        s99f2.setGreeting(greeting);
        return s99f2;
    }

}
