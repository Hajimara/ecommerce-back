package com.shop.controller;

import com.shop.dto.common.ApiResult;
import com.shop.dto.customer.response.CustomerDto;
import com.shop.service.ProcessCustomer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class ControllerCustomer {
    private final ProcessCustomer serviceCustomer;

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResult> getCustomer(@PathVariable("customerId") Long customerId) {
        ApiResult apiResult = new ApiResult();

        CustomerDto customerDto = serviceCustomer.getCustomerWithAddresses(customerId);

        apiResult.setData(customerDto);

        return ResponseEntity.ok().body(apiResult);
    }
}
