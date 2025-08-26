package hanieum.conik.adapter.company.webapi.response;

import hanieum.conik.domain.company.entity.CompanyAddress;

public record CompanyAddressResponse(
        String postal,

        String street,

        String detail
) {
    public static CompanyAddressResponse from(CompanyAddress address) {
        return new CompanyAddressResponse(
                address.getPostalCode(),
                address.getStreetAddress(),
                address.getDetailAddress()
        );
    }
}