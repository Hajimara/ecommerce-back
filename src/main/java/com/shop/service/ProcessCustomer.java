package com.shop.service;

import com.shop.dto.customer.request.RegisterCustomerDto;
import com.shop.dto.customer.response.CustomerDto;
import com.shop.dto.customer.response.CustomerIdDto;
import com.shop.entity.Address;
import com.shop.entity.Customer;
import com.shop.repository.RepositoryAddress;
import com.shop.repository.RepositoryCustomer;
import com.shop.service.entity.ServiceAddress;
import com.shop.service.entity.ServiceCustomer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

// Process계층은 비즈니스 로직 구성
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProcessCustomer {

    private final PasswordEncoder passwordEncoder;

    // @RequiredArgsConstructor를 사용하면 Lombok이 final 필드만 포함하는 생성자를 자동으로 만들어줌
    // 즉, @Autowired 없이도 customerRepository가 자동으로 주입됨
    private final RepositoryCustomer repositoryCustomer;
    private final RepositoryAddress repositoryAddress;
    private final ServiceCustomer serviceCustomer;
    private final ServiceAddress serviceAddress;

    // 가입
    public CustomerIdDto register(RegisterCustomerDto registerCustomerDto) {
        boolean isExistsEmail = repositoryCustomer.existsByEmail(registerCustomerDto.getEmail());

        // 이메일 중복 확인
        if (isExistsEmail) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다"+ registerCustomerDto.getEmail());
        }

        registerCustomerDto.setPassword(passwordEncoder.encode(registerCustomerDto.getPassword()));

        Customer customer = serviceCustomer.toEntity(registerCustomerDto);

        List<Address> addressList = serviceAddress.toEntity(registerCustomerDto.getAddressList());

        serviceCustomer.create(customer, addressList);

        return serviceCustomer.toCustomerIdDto(customer);
    }

    // 특정고객 조회 및 주소 매칭
    public CustomerDto getCustomerWithAddresses(Long customerId) {
        Customer customer = repositoryCustomer.findById(customerId).orElseThrow(() -> new IllegalArgumentException("고객 정보를 찾을 수 없습니다. ID: " + customerId));

        List<Address> addresses = repositoryAddress.findAllByCustomerCustomerId(customerId);

        customer.getAddressList().clear();
        customer.getAddressList().addAll(addresses);

        return serviceCustomer.toCustomerDto(customer);
    }
}
