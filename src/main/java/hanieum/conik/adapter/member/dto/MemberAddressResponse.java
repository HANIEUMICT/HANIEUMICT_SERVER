package hanieum.conik.adapter.member.dto;

import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.member.MemberAddress;

public record MemberAddressResponse(
        Long id,
        String postalCode,
        String streetAddress,
        String detailAddress,
        String addressName,
        String recipient,
        String phoneNumber,
        boolean isDefault
) {
    public static MemberAddressResponse from(MemberAddress address) {
        return new MemberAddressResponse(
                address.getId(),
                address.getPostalCode(),
                address.getStreetAddress(),
                address.getDetailAddress(),
                address.getAddressName(),
                address.getRecipient(),
                address.getPhoneNumber(),
                false // TODO: 기존 코드와의 호환성으로 디폴트 false로 반환 - 논의 필요
        );
    }

    public static MemberAddressResponse from(MemberAddress address, Member member) {
        boolean isDefault = member.getDefaultAddress() != null
                && member.getDefaultAddress().equals(address);

        return new MemberAddressResponse(
                address.getId(),
                address.getPostalCode(),
                address.getStreetAddress(),
                address.getDetailAddress(),
                address.getAddressName(),
                address.getRecipient(),
                address.getPhoneNumber(),
                isDefault
        );
    }
}