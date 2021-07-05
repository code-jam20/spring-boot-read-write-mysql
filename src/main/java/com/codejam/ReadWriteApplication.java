package com.codejam;

import com.codejam.controller.CustomerResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The spring boot application class, that starts the app.
 */
@SpringBootApplication
public class ReadWriteApplication extends ResourceConfig {

    public ReadWriteApplication() {
        register(CustomerResource.class);
    }

    /**
     * Main method that starts the Spring Boot application.
     *
     * @param args Arguments passed to the app.
     */
    public static void main(String[] args) {
        SpringApplication.run(ReadWriteApplication.class, args);
    }

}
