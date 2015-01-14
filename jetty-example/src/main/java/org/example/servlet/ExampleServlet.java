package org.example.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExampleServlet extends HttpServlet {

    private static final long serialVersionUID = 6381828167428989365L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        final Writer writer = response.getWriter();
        writer.write("<h1>Example servlet</h1>\n");
        writer.write("<p>Hello World from a simple test servlet!</p>\n");
        writer.write("<p><a href=\"/webapp\">Back</a></p>\n");
    }
}
