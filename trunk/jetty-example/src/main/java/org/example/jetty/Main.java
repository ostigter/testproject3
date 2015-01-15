package org.example.jetty;

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

    public static void main(String[] args) throws Exception {
        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(PORT);
        connector.setIdleTimeout(60 * 60 * 1000); // 1 hour
        connector.setSoLingerTime(-1);
        server.addConnector(connector);

        WebAppContext webApp = new WebAppContext();
        webApp.setContextPath(CONTEXT_PATH);
        // Enable in production environment:
        webApp.setWar(Main.class.getProtectionDomain().getCodeSource().getLocation().toExternalForm());
        // Enable in development environment:
        // webApp.setWar("src/main/webapp");

        server.setHandler(webApp);

        server.start();
        server.setStopAtShutdown(true);
        server.join();
    }
}
