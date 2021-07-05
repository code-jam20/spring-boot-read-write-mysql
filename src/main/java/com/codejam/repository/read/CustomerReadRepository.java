package com.codejam.repository.read;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codejam.entities.Customer;

import java.util.Optional;


@Repository
public interface CustomerReadRepository extends CrudRepository<Customer, Long> {

    String GET_CUSTOMER_BY_FIRST_NAME = "SELECT * from customer WHERE first_name = :firstName AND last_name = :lastName";

    @Query(value = GET_CUSTOMER_BY_FIRST_NAME, nativeQuery = true)
    Optional<Customer> retrieveByName(@Param("firstName") String firstName,
                                      @Param("lastName") String lastName);
}
