package org.ozsoft.secs.message;

import org.ozsoft.secs.MessageHandler;
import org.ozsoft.secs.SecsParseException;
import org.ozsoft.secs.format.A;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.L;

/**
 * S99F1 test message handler. <br />
 * <br />
 * 
 * Format of S99F1 request message:
 * <pre>
 * <L
 *      NAME    // <A>
 * >
 * </pre>
 * 
 * Format of S99F2 reply message:
 * <pre>
 * <L
 *      <B:1>   // Acknowledge byte (0x00 = Accept)
 *      <A>     // Greeting based on NAME
 * >
 * </pre>
 * 
 * @author Oscar Stigter
 */
public class S99F1 extends MessageHandler {

    private static final int STREAM = 99;

    private static final int FUNCTION = 1;

    private static final String DESCRIPTION = "Test Request (TR)";

    private static final String GREETING = "Hello, %s!";

    public S99F1() {
        super(STREAM, FUNCTION, DESCRIPTION);
    }

    @Override
    public DataMessage handle(DataMessage message) throws SecsParseException {
        int sessionId = message.getSessionId();
        long transactionId = message.getTransactionId();
        
        Data<?> requestText = message.getText();
        if (requestText == null) {
            return createS9F7(sessionId, transactionId);
        }
        if (!(requestText instanceof L)) {
            return createS9F7(sessionId, transactionId);
        }
        L l = (L) requestText;
        if (l.length() != 1) {
            return createS9F7(sessionId, transactionId);
        }
        requestText = l.getItem(0);
        if (!(requestText instanceof A)) {
            return createS9F7(sessionId, transactionId);
        }
        String name = ((A) requestText).getValue();
        if (name.isEmpty()) {
            return createS9F7(sessionId, transactionId);
        }

        // Send S99F2 Test Response (TR) with acknowledge byte and greeting.
        L replyText = new L();
        replyText.addItem(new B(0x00));  // Accept
        replyText.addItem(new A(String.format(GREETING, name))); // Greeting
        return new DataMessage(sessionId, STREAM, FUNCTION + 1, false, transactionId, replyText);
    }

    /**
     * Returns a S9F7 (Bad data) reply message.
     * 
     * @param sessionId
     *            Session ID of the request message.
     * @param transactionId
     *            Transaction ID of the request message.
     * 
     * @return The S9F7 message.
     */
    private static DataMessage createS9F7(int sessionId, long transactionId) {
        return new DataMessage(sessionId, 9, 7, false, transactionId, null);
    }

}
