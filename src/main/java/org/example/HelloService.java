package org.example;

import org.apache.commons.lang.StringUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;


@Path("/hello")
public class HelloService {

    @GET
    @Path("/is-alive")
    @Produces(MediaType.TEXT_PLAIN)
    public String serverTime() {
        System.out.println("server time request");
        return new Date().toString();
    }

    @GET
    @Path("{clientName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response greetClient(@PathParam("clientName") String name) {


        if(StringUtils.isBlank(name)){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Invalid name").type(MediaType.TEXT_PLAIN).build();
        }

        String output = "Hi " + name;
        return Response.status(200).entity(output).build();
    }
}
