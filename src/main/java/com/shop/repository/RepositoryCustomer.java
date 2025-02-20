package com.shop.repository;

import com.shop.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepositoryCustomer extends JpaRepository<Customer, Long> {

    List<Customer> findAll();

    Optional<Customer> findById(Long customerId);

    boolean existsByEmail(String email);
}
