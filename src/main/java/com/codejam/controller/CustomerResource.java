package com.codejam.controller;

import com.codejam.service.impl.CustomerServiceImpl;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import static com.codejam.utils.AsyncUtil.completeResponse;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * This jersey controller handles all the API calls made on the end point.
 * <p>
 * - POST /customers: Crates new customers eith first name, last name and department
 * - GET /customers: Gets the customers by matching first name and last name
 *
 * @author tdhanjal
 */
@Path("/secure")
@Produces(APPLICATION_JSON)
public class CustomerResource {

    @Autowired
    CustomerServiceImpl customerService;

    @GET
    @Path("/health")
    public void getHealthCheck(@Suspended AsyncResponse asyncResponse) {
        asyncResponse.resume(ImmutableMap.of("status", "ok"));
    }

    @POST
    @Path("/customers")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void createCustomer(@FormParam("FirstName") final String firstName,
                               @FormParam("LastName") final String lastName,
                               @FormParam("Department") final String department,
                               @Suspended final AsyncResponse asyncResponse) {
        completeResponse(() ->
                        customerService.createCustomer(firstName,
                                lastName, department),
                asyncResponse);
    }

    @GET
    @Path("/customers")
    public void getCustomer(@QueryParam("FirstName") final String firstName,
                            @QueryParam("LastName") final String lastName,
                            @Suspended final AsyncResponse asyncResponse) {
        completeResponse(() ->
                        customerService.getCustomerByName(firstName, lastName),
                asyncResponse);
    }
}
