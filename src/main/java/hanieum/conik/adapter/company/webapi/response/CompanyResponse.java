package hanieum.conik.adapter.company.webapi.response;

import hanieum.conik.domain.company.Company;

import java.util.List;

public record CompanyResponse(
        CompanyDetailResponse detail,

        List<CompanyAddressResponse> addresses
) {
    public static CompanyResponse from(Company company) {
        CompanyDetailResponse detail = CompanyDetailResponse.from(company);
        List<CompanyAddressResponse> addresses = company.getAddresses().stream()
                .map(CompanyAddressResponse::from)
                .toList();

        return new CompanyResponse(detail, addresses);
    }
}