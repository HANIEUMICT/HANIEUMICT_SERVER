package hanieum.conik.adapter.member.dto;

import hanieum.conik.domain.member.MemberAddress;

public record MemberAddressResponse(
        Long addressId,
        String zipcode,
        String streetAddress,
        String detailAddress
) {
    public static MemberAddressResponse from(MemberAddress address) {
        return new MemberAddressResponse(
                address.getId(),
                address.getAddressPostalCode(),
                address.getAddressStreetAddress(),
                address.getAddressDetailAddress()
        );
    }
}