package com.codejam.repository.write;

import com.codejam.entities.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerWriteRepository extends CrudRepository<Customer, Long> {

}
