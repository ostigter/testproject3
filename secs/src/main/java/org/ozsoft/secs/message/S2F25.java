package org.ozsoft.secs.message;

import org.ozsoft.secs.SecsPrimaryMessage;
import org.ozsoft.secs.SecsReplyMessage;
import org.ozsoft.secs.format.Data;

/**
 * S2F25 Loopback Diagnostic Request (LDR) message. <br />
 * <br />
 * 
 * Format:
 * <pre>
 * TESTDATA     // any data item, or empty
 * </pre>
 * 
 * @author Oscar Stigter
 */
public class S2F25 extends SecsPrimaryMessage {

    private static final int STREAM = 2;

    private static final int FUNCTION = 25;

    private static final boolean WITH_REPLY = true;
    
    private static final String DESCRIPTION = "Loopback Diagnostic Request (LDR)";
    
    private Data<?> testData;
    
    public Data<?> getTestData() {
        return testData;
    }
    
    public void setTestData(Data<?> testData) {
        this.testData = testData;
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
    protected void parseData(Data<?> data) {
        // Just copy test data as-is.
        setTestData(data);
    }

    @Override
    protected Data<?> getData() {
        // Just return test data as-is.
        return getTestData();
    }

    @Override
    protected SecsReplyMessage handle() {
        // Always acknowledge request.
        S2F26 s2f26 = new S2F26();
        s2f26.setTestData(testData);
        return s2f26;
    }

}
