package org.example.jetty;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Example console application with an embedded Jetty server.
 * 
 * @author Oscar Stigter
 */
public class Main {

    private static final int PORT = 8080;

    private static final String WEB_APP_HOME = "./webapps";

    private static final File WORK_DIR = new File("./work");

    public static void main(String[] args) throws Exception {
        Server server = new Server(PORT);

        // Custom Jetty handler using one context.
        ContextHandler helloHandler = new ContextHandler("/hello");
        helloHandler.setHandler(new HelloHandler());

        // Standard WAR file using another context.
        WebAppContext webApp = new WebAppContext(WEB_APP_HOME + "/javaee-servlet.war", "/webapp");
        if (!WORK_DIR.exists()) {
            WORK_DIR.mkdirs();
        }
        webApp.setTempDirectory(WORK_DIR);

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { helloHandler, webApp });
        server.setHandler(contexts);

        server.start();
        System.out.println("\nServer started -- press ENTER to shutdown.");

        waitForEnterPressed();

        server.stop();
        deleteFile(WORK_DIR);
        System.out.println("\nServer stopped.");
    }

    private static void waitForEnterPressed() {
        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private static void deleteFile(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteFile(child);
            }
        }
        file.delete();
    }

    /**
     * Example Jetty handler.
     * 
     * @author Oscar Stigter
     */
    private static class HelloHandler extends AbstractHandler {

        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.getWriter().println("<h1>Hello World</h1>");
            baseRequest.setHandled(true);
        }
    }
}
