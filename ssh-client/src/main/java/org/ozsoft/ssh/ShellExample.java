package org.ozsoft.ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * Example application showing how to open an SSH shell with the JSch library.
 * 
 * @author Oscar Stigter
 */
public class ShellExample {

    private static final String HOST = "172.19.234.93";

    private static final String USERNAME = "john";

    private static final String PASSWORD = "secret";

    private static final int PORT = 22;

    private static final int TIMEOUT = 5000;

    public static void main(String[] args) {
        JSch jsch = new JSch();
        try {
            System.out.println("Connecting");
            Session session = jsch.getSession(USERNAME, HOST, PORT);
            session.setPassword(PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(TIMEOUT);

            System.out.println("Opening shell channel");
            Channel channel = session.openChannel("shell");
            channel.setInputStream(System.in);
            channel.setOutputStream(new AnsiFilterOutputStream());
            channel.connect(TIMEOUT);
        } catch (JSchException e) {
            System.err.println(e);
        }
    }

}
