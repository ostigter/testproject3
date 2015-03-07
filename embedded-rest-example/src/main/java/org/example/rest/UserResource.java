package org.example.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/users")
public class UserResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "Listing all users";
    }
}
