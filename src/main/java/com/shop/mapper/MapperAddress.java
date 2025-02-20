package com.shop.mapper;

import com.shop.dto.address.request.ModifyAddressDto;
import com.shop.dto.address.request.RegisterAddressDto;
import com.shop.dto.address.response.AddressDto;
import com.shop.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

// 이 인터페이스는 Address 엔티티와 AddressDto, ModifyAddressDto, RegisterAddressDto 간 변환을 담당함
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MapperAddress {
    // ModifyAddressDto → Address 엔티티 변환
    Address toEntity(ModifyAddressDto modifyAddressDto);

    // RegisterAddressDto → Address 엔티티 변환
    Address toEntity(RegisterAddressDto registerAddressDto);

    List<Address> toEntity(List<AddressDto> AddressDtoList);

    // Address 엔티티 → AddressDto 변환
    AddressDto toAddressDto(Address address);
}
