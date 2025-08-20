package hanieum.conik.adapter.company.webapi.response;

import hanieum.conik.domain.company.Company;

import java.util.List;

public record CompanySummaryResponse(
        Long id,
        String name,
        String businessType,
        String owner,
        String registrationNumber,
        List<CompanyAddressResponse> addresses
) {
    public static CompanySummaryResponse from(Company company) {
        List<CompanyAddressResponse> addresses = company.getAddresses() == null
                ? List.of()
                : company.getAddresses().stream()
                .map(CompanyAddressResponse::from)
                .toList();

        return new CompanySummaryResponse(
                company.getId(),
                company.getName(),
                company.getBusinessType(),
                company.getOwner(),
                company.getRegistrationNumber(),
                addresses
        );
    }
}