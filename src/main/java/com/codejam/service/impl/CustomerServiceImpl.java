package com.codejam.service.impl;

import com.codejam.entities.Customer;
import com.codejam.error.ServiceErrorsEnum;
import com.codejam.error.ServiceException;
import com.codejam.repository.read.CustomerReadRepository;
import com.codejam.repository.write.CustomerWriteRepository;
import com.codejam.service.CustomerService;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Service Implementation that fetch customers / perform geo spatial search.
 *
 * @author tdhanjal
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    /**
     * Writes to master DB
     */
    @Autowired
    CustomerWriteRepository customerWriteRepository;

    /**
     * Reads from slave DB
     */
    @Autowired
    CustomerReadRepository customerReadRepository;

    /**
     * Create customer with name and department.
     *
     * @param firstName
     * @param lastName
     * @param department
     * @return Customer
     */
    @Override
    public CompletionStage<Customer> createCustomer(String firstName, String lastName, String department) {
        try {
            // Persist Customer
            LOGGER.info("Creating customer with name {} {} using master node", firstName, lastName);
            final Customer customerLocation = customerWriteRepository.save(new Customer(firstName, lastName, department));
            return CompletableFuture.completedFuture(customerLocation);
        } catch (Exception ex) {
            if (ex instanceof DataIntegrityViolationException || ex instanceof ConstraintViolationException) {
                LOGGER.warn("MySQL exception " + ex);
                throw new ServiceException(ServiceErrorsEnum.CUSTOMER_ALREADY_EXIST);
            } else {
                LOGGER.error("Unable to process request because of " + ex);
                throw new ServiceException(ServiceErrorsEnum.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Get customer based on first name and last name.
     *
     * @param firstName
     * @param lastName
     * @return Customer
     */
    @Override
    public CompletionStage<Customer> getCustomerByName(String firstName, String lastName) {
        LOGGER.info("Getting customer with name {} {} using slave node", firstName, lastName);
        final Optional<Customer> customer = customerReadRepository.retrieveByName(firstName, lastName);

        // Throw valid error when no customers found
        if (!customer.isPresent()) {
            throw new ServiceException(ServiceErrorsEnum.CUSTOMER_NOT_FOUND);
        }
        return CompletableFuture.completedFuture(customer.get());
    }
}
