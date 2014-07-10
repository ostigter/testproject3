package org.example.library.client;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.example.library.api.Book;
import org.example.library.api.Library;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class LibraryClient extends HttpServlet {

    private static final long serialVersionUID = 3900650587710494019L;

    private static final String BASE_URL = "http://localhost:8080/jaxrs-example/rest";

    public String listBooksJaxRs() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(BASE_URL + "/library/book");
        Response clientResponse = webTarget.request().get();
        int statusCode = clientResponse.getStatus();
        String output = null;
        if (statusCode == HttpServletResponse.SC_OK) {
            output = clientResponse.readEntity(String.class);
        } else {
            output = String.format("ERROR: Could not retrieve list of books (HTTP status code: %d)\n", statusCode);
        }
        clientResponse.close();
        return output;
    }

    public String listBooksProxy() {
        ResteasyClient client = (ResteasyClient) ResteasyClientBuilder.newClient();
        ResteasyWebTarget target = client.target(BASE_URL);
        Library library = target.proxy(Library.class);
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for (Book book : library.listBooks()) {
            sb.append("<li>").append(book).append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }
}
