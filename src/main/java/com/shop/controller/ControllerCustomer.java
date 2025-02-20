package com.shop.controller;

import com.shop.dto.common.ApiResult;
import com.shop.dto.customer.request.RegisterCustomerDto;
import com.shop.dto.customer.response.CustomerDto;
import com.shop.dto.customer.response.CustomerIdDto;
import com.shop.service.ProcessCustomer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class ControllerCustomer {
    private final ProcessCustomer processCustomer;

    @PostMapping
    public ResponseEntity<ApiResult> registerCustomer(@Valid @RequestBody RegisterCustomerDto registerCustomerDto) {
        System.out.println("📌 [DEBUG] Received Password: " + registerCustomerDto.getPassword());
        ApiResult apiResult = new ApiResult();
        CustomerIdDto customerIdDto = processCustomer.register(registerCustomerDto);

        apiResult.setData(customerIdDto);

        return ResponseEntity.ok().body(apiResult);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResult> getCustomer(@PathVariable("customerId") Long customerId) {
        ApiResult apiResult = new ApiResult();

        CustomerDto customerDto = processCustomer.getCustomerWithAddresses(customerId);

        apiResult.setData(customerDto);

        return ResponseEntity.ok().body(apiResult);
    }
}
