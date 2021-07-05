package com.codejam.service;

import com.codejam.entities.Customer;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Service interface that masks the caller from the implementation that fetches / acts on Customer
 * related data.
 *
 * @author tdhanjal
 */
public interface CustomerService {

    /**
     * Creates new customer.
     *
     * @param firstName
     * @param lastName
     * @param department
     * @return Customer
     */
    CompletionStage<Customer> createCustomer(String firstName, String lastName, String department);

    /**
     * Get customer based on first name and last name.
     *
     * @param firstName
     * @param lastName
     * @return List<Customer>
     */
    CompletionStage<Customer> getCustomerByName(String firstName, String lastName);
}
