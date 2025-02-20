package com.shop.service.entity;

import com.shop.dto.address.response.AddressDto;
import com.shop.entity.Address;
import com.shop.mapper.MapperAddress;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ServiceAddress {
    private final MapperAddress mapperAddress;

    public List<Address> toEntity(List<AddressDto> addressDtoList) {
        return mapperAddress.toEntity(addressDtoList);
    }
}
