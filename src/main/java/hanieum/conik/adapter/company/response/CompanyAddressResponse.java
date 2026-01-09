package hanieum.conik.adapter.company.response;

import hanieum.conik.domain.company.entity.CompanyAddress;

public record CompanyAddressResponse(
        String postal,

        String street,

        String detail,

        String addressName,

        String recipient,

        String phoneNumber
) {
    public static CompanyAddressResponse from(CompanyAddress address) {
        return new CompanyAddressResponse(
                address.getPostalCode(),
                address.getStreetAddress(),
                address.getDetailAddress(),
                address.getName(),
                address.getRecipient(),
                address.getPhoneNumber()
        );
    }
}