package hanieum.conik.adapter.member.dto;

import hanieum.conik.domain.member.MemberAddress;

public record MemberAddressResponse(
        Long id,
        String postalCode,
        String streetAddress,
        String detailAddress,
        String addressName,
        String recipient,
        String phoneNumber
) {
    public static MemberAddressResponse from(MemberAddress address) {
        return new MemberAddressResponse(
                address.getId(),
                address.getPostalCode(),
                address.getStreetAddress(),
                address.getDetailAddress(),
                address.getAddressName(),
                address.getRecipient(),
                address.getPhoneNumber()
        );
    }
}