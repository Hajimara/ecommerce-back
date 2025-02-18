package com.shop.repository;

import com.shop.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositoryAddress extends JpaRepository<Address, Long> {
    List<Address> findAllByCustomerCustomerId(Long customerId);
}
