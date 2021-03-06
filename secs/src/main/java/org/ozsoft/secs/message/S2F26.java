package org.ozsoft.secs.message;

import org.ozsoft.secs.SecsReplyMessage;
import org.ozsoft.secs.format.Data;

/**
 * S2F26 Loopback Diagnostic Acknowledge (LDA) reply message. <br />
 * <br />
 * 
 * Format:
 * <pre>
 * TESTDATA     // any data item, or empty
 * </pre>
 * 
 * @author Oscar Stigter
 */
public class S2F26 extends SecsReplyMessage {

    private static final int STREAM = 2;

    private static final int FUNCTION = 26;
    
    private static final boolean WITH_REPLY = false;
    
    private static final String DESCRIPTION = "Loopback Diagnostic Acknowledge (LDA)";
    
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
    public void parseData(Data<?> data) {
        // Just copy test data as-is.
        setTestData(data);
    }

    @Override
    public Data<?> getData() {
        // Just return test data as-is.
        return getTestData();
    }

    @Override
    protected void handle() {
        // Not implemented.
    }

}
