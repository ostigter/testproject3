package org.ozsoft.ssh;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * Example application showing how to transfer files over SFTP with the JSch
 * library.
 * 
 * @author Oscar Stigter
 */
public class SftpExample {

    private static final String HOST = "172.19.234.93";

    private static final int PORT = 22;

    private static final String USERNAME = "john";

    private static final String PASSWORD = "secret";

    private static final int TIMEOUT = 5000;

    public static void main(String[] args) {
        JSch jsch = new JSch();
        try {
            System.out.println("Connecting");
            Session session = jsch.getSession(USERNAME, HOST, PORT);
            session.setPassword(PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(TIMEOUT);

            System.out.println("Opening SFTP channel");
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect(TIMEOUT);

            System.out.println("Retrieving file");
            sftpChannel.lcd("C:/LocalData/Temp");
            sftpChannel.cd("/opt/lis/etc/docs");
            SftpATTRS attr = sftpChannel.stat("docs.properties");
            System.out.format("Remote file size: %d\n", attr.getSize());
            sftpChannel.get("docs.properties", "docs.properties");

            System.out.println("Sending file");
            sftpChannel.cd("/tmp");
            sftpChannel.put("docs.properties", "test.tmp");

            System.out.println("Checking remote file");
            attr = sftpChannel.stat("test.tmp");
            System.out.format("Remote file size: %d\n", attr.getSize());

            System.out.println("Deleting file");
            sftpChannel.rm("test.tmp");

            sftpChannel.exit();
            session.disconnect();
            System.out.println("Disconnected");

        } catch (SftpException e) {
            System.err.println("SFTP error: " + e);

        } catch (JSchException e) {
            System.err.println("SSH error: " + e);
        }
    }

}
