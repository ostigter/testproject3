package org.example.library.client;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/library/client")
public class LibraryServlet extends HttpServlet {

    private static final long serialVersionUID = -3603372763544612999L;

    @Inject
    private LibraryClient libraryClient;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.write("<h1>Library Client</h1>");
        writer.write("<p>Books (JAX-RS):</p>");
        writer.write(libraryClient.listBooksJaxRs());
        writer.write("<p>Books (RESTEasy proxy):</p>");
        writer.write(libraryClient.listBooksProxy());
    }
}
