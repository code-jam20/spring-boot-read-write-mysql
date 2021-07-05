package com.codejam;

import com.google.common.net.MediaType;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.Param;
import org.asynchttpclient.Response;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomerResourceTest extends BaseTest {
    private final String CUSTOMER_RESOURCE = "/secure/customers";
    private final String FIRST_NAME_PARAM = "FirstName";
    private final String LAST_NAME_PARAM = "LastName";
    private final String DEPARTMENT_PARAM = "Department";

    @Test
    public void testCreateCustomer() throws InterruptedException, ExecutionException, TimeoutException {
        final BoundRequestBuilder builder = createPost(BASE_URL + CUSTOMER_RESOURCE, Optional.of(MediaType.FORM_DATA))
                .setFormParams(asList(new Param(FIRST_NAME_PARAM, "John"),
                        new Param(LAST_NAME_PARAM, "Doe"),
                        new Param(DEPARTMENT_PARAM, "HR")));

        final Response response = httpClient.executeRequest(builder.build()).get(10, TimeUnit.SECONDS);
        assertThat(response.getStatusCode()).isEqualTo(javax.ws.rs.core.Response.Status.OK.getStatusCode());
        final String responseBody = new String(response.getResponseBody().getBytes());
        assertThat(responseBody).contains("\"firstName\":\"John\"");
    }

    @Test
    public void testGetCustomer() throws InterruptedException, ExecutionException, TimeoutException {
        BoundRequestBuilder builder = createPost(BASE_URL + CUSTOMER_RESOURCE, Optional.of(MediaType.FORM_DATA))
                .setFormParams(asList(new Param(FIRST_NAME_PARAM, "Jane"),
                        new Param(LAST_NAME_PARAM, "Doe"),
                        new Param(DEPARTMENT_PARAM, "PR")));

        Response response = httpClient.executeRequest(builder.build()).get(10, TimeUnit.SECONDS);
        assertThat(response.getStatusCode()).isEqualTo(javax.ws.rs.core.Response.Status.OK.getStatusCode());

        builder = createGet(BASE_URL + CUSTOMER_RESOURCE)
                .setQueryParams(asList(new Param(FIRST_NAME_PARAM, "Jane"),
                        new Param(LAST_NAME_PARAM, "Doe")));

        response = httpClient.executeRequest(builder.build()).get(10, TimeUnit.SECONDS);
        assertThat(response.getStatusCode()).isEqualTo(javax.ws.rs.core.Response.Status.OK.getStatusCode());
        final String responseBody = new String(response.getResponseBody().getBytes());
        assertThat(responseBody).contains("\"firstName\":\"Jane\"");
    }

    @Test
    public void testGetCustomerNotFound() throws InterruptedException, ExecutionException, TimeoutException {
        final BoundRequestBuilder builder = createGet(BASE_URL + CUSTOMER_RESOURCE)
                .setQueryParams(asList(new Param(FIRST_NAME_PARAM, "Sara"),
                        new Param(LAST_NAME_PARAM, "Mccain")));

        final Response response = httpClient.executeRequest(builder.build()).get(10, TimeUnit.SECONDS);
        assertThat(response.getStatusCode()).isEqualTo(javax.ws.rs.core.Response.Status.NOT_FOUND.getStatusCode());
    }
}
