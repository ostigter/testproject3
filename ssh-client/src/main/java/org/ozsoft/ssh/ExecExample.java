package org.ozsoft.ssh;

import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * Example application showing how to execute a remote shell command over SSH with the JSch library.
 * 
 * @author Oscar Stigter
 */
public class ExecExample {

    private static final String HOST = "172.19.234.93";

    private static final int PORT = 22;

    private static final String USERNAME = "john";

    private static final String PASSWORD = "secret";

    private static final int TIMEOUT = 5000;

    private static final String COMMAND = "ls -l";

    public static void main(String[] args) {
        JSch jsch = new JSch();
        try {
            System.out.println("Connecting");
            Session session = jsch.getSession(USERNAME, HOST, PORT);
            session.setPassword(PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(TIMEOUT);

            System.out.println("Executing remote command");
            ChannelExec execChannel = (ChannelExec) session.openChannel("exec");
            execChannel.setCommand(COMMAND);
            execChannel.setInputStream(null);
            execChannel.setErrStream(System.err);
            try {
                InputStream in = execChannel.getInputStream();
                execChannel.connect(TIMEOUT);
                String result = null;
                byte[] tmp = new byte[1024];
                while (true) {
                    while (in.available() > 0) {
                        int i = in.read(tmp, 0, 1024);
                        if (i < 0) {
                            break;
                        }
                        result = new String(tmp, 0, i);
                        System.out.println("Result:\n" + result);
                    }
                    if (execChannel.isClosed()) {
                        System.out.println("Exit status: " + execChannel.getExitStatus());
                        break;
                    }
                }

            } catch (IOException e) {
                System.err.println(e);
            }

            execChannel.disconnect();
            session.disconnect();
            System.out.println("\nDisconnected");

        } catch (JSchException e) {
            System.err.println("SSH error: " + e);
        }
    }

}
