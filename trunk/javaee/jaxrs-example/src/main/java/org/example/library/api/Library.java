package org.example.library.api;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/library")
public interface Library {

    @GET
    @Path("/book")
    @Produces("application/json")
    Collection<Book> listBooks();

    @GET
    @Path("/book/{id}")
    @Produces("application/json")
    Book getBook(@PathParam("id") long id);

    @PUT
    @Path("/book")
    @Consumes("application/json")
    void addBook(Book book);

    @DELETE
    @Path("/book/{id}")
    void deleteBook(@PathParam("id") long id);

}
