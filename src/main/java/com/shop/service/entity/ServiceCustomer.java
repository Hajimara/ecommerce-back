package com.shop.service.entity;

import com.shop.dto.customer.response.CustomerDto;
import com.shop.entity.Customer;
import com.shop.mapper.MapperCustomer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ServiceCustomer {
    private final MapperCustomer mapperCustomer;

    public CustomerDto toCustomerDto(Customer customer) {
        return mapperCustomer.toCustomerDto(customer);
    }
}
