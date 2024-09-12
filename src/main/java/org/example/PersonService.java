package org.example;

import org.example.entity.Person;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/persons")
public class PersonService {


    private static List<Person> data;

    static {
        data = new ArrayList<>();
        Person p = new Person();
        p.setId(100);
        p.setName("John");

        Person p1 = new Person();
        p1.setId(200);
        p1.setName("Bob");

        Person p2 = new Person();
        p2.setId(300);
        p2.setName("Mike");

        data.addAll(Arrays.asList(p, p1, p2));

    }

    @GET
    @Path("/test")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response test() {
        if (data.size() < 1) {
            return Response.status(Response.Status.NO_CONTENT).entity("No person found").build();
        }

        int index = new Random().nextInt(data.size());
        return Response.ok(data.get(index)).build();
    }

    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response all() {
        return Response.ok(data).build();
    }
}