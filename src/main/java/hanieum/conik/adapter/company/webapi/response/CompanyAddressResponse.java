package hanieum.conik.adapter.company.webapi.response;

import hanieum.conik.domain.company.CompanyAddress;

public record CompanyAddressResponse(
        String postal,

        String street,

        String detail
) {
    public static CompanyAddressResponse from(CompanyAddress address) {
        return new CompanyAddressResponse(
                address.getAddressPostalCode(),
                address.getAddressStreetAddress(),
                address.getAddressDetailAddress()
        );
    }
}