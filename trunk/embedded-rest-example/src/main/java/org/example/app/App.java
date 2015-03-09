package org.example.app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class App {

    private static final int PORT = 8080;

    private static final String CONTEXT = "/embedded-rest-example";

    public static void main(String[] args) {
        Server jettyServer = new Server(PORT);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(CONTEXT);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/rest/*");
        jerseyServlet.setInitOrder(0);
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "org.example.app.rest");

        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
            System.err.println("ERROR: Could not start Jetty server");
            e.printStackTrace(System.err);
        } finally {
            jettyServer.destroy();
        }
    }
}
