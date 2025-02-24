package com.shop.service.entity;

import com.shop.ExceptionNotFound;
import com.shop.constant.enums.ErrorCode;
import com.shop.dto.customer.request.RegisterCustomerDto;
import com.shop.dto.customer.response.CustomerDto;
import com.shop.dto.customer.response.CustomerIdDto;
import com.shop.entity.Address;
import com.shop.entity.Customer;
import com.shop.mapper.MapperCustomer;
import com.shop.repository.RepositoryCustomer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ServiceCustomer {
    private final MapperCustomer mapperCustomer;
    private final RepositoryCustomer repositoryCustomer;

    public Customer create(Customer customer, List<Address> addressList) {
        for (Address address : addressList) {
            address.setCustomer(customer); // ✅ 각 Address에 Customer 설정
        }
        customer.updateAddress(addressList);
        return repositoryCustomer.save(customer);
    }

    public void delete (Long customerId){
        repositoryCustomer.deleteById(customerId);
    }

    public CustomerDto toCustomerDto(Customer customer) {
        return mapperCustomer.toCustomerDto(customer);
    }

    public CustomerIdDto toCustomerIdDto(Customer customer){
        return mapperCustomer.toCustomerIdDto(customer);
    }

    public Customer toEntity(RegisterCustomerDto registerCustomerDto) {
        return mapperCustomer.toEntity(registerCustomerDto);
    }

    public Customer getByEmailElseThrow(String email) {
        return repositoryCustomer.findByEmail(email)
                .orElseThrow(() -> new ExceptionNotFound(ErrorCode.MEMBER_NOT_FOUND));
    }
}
