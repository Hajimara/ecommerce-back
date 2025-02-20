package com.shop.mapper;

import com.shop.dto.customer.request.RegisterCustomerDto;
import com.shop.dto.customer.response.CustomerDto;
import com.shop.dto.customer.response.CustomerIdDto;
import com.shop.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MapperCustomer {
    CustomerDto toCustomerDto(Customer customer);

    CustomerIdDto toCustomerIdDto(Customer customer);

    Customer toEntity(RegisterCustomerDto registerCustomerDto);
}
