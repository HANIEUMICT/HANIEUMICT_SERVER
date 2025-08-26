package hanieum.conik.adapter.company.webapi.response;

import hanieum.conik.domain.company.entity.Company;

public record CompanySummaryResponse(
        Long id,
        String name,
        String businessType,
        String owner,
        String registrationNumber,
        CompanyAddressResponse address
) {
    public static CompanySummaryResponse from(Company company) {
        return new CompanySummaryResponse(
                company.getId(),
                company.getName(),
                company.getBusinessType(),
                company.getOwner(),
                company.getRegistrationNumber(),
                CompanyAddressResponse.from(company.getAddress())
        );
    }
}