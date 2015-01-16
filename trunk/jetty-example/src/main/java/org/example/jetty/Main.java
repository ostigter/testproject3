package org.example.jetty;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Example console application with an embedded Jetty server wit a dynamic web application.
 * 
 * @author Oscar Stigter
 */
public class Main {

    private static final int PORT = 8080;

    private static final String CONTEXT_PATH = "/";

    private static final String DEV_WEBAPP_DIR = "src/main/webapp";

    public static void main(String[] args) throws Exception {
        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(PORT);
        connector.setIdleTimeout(60 * 60 * 1000); // 1 hour
        connector.setSoLingerTime(-1);
        server.addConnector(connector);

        WebAppContext webApp = new WebAppContext();
        webApp.setContextPath(CONTEXT_PATH);
        if (new File(DEV_WEBAPP_DIR).exists()) {
            // Development environment; use web application source tree.
            webApp.setWar(DEV_WEBAPP_DIR);
        } else {
            // Production environment; use resources from within JAR file.
            webApp.setWar(Main.class.getProtectionDomain().getCodeSource().getLocation().toExternalForm());
        }

        server.setHandler(webApp);

        server.start();
        server.setStopAtShutdown(true);
        server.join();
    }
}
