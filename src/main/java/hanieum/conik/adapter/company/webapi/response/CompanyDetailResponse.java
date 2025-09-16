package hanieum.conik.adapter.company.webapi.response;

import hanieum.conik.domain.company.Company;
import hanieum.conik.domain.company.enumerate.CompanyStatus;

public record CompanyDetailResponse(
        Long id,

        String name,

        String owner,

        String email,

        String phoneNumber,

        String businessType,

        String industry,

        String registrationNumber,

        String registrationCertificateUrl,

        String bankbookCopy,

        String profileUrl,

        CompanyStatus status
) {
    public static CompanyDetailResponse from(Company company) {
        return new CompanyDetailResponse(
                company.getId(),
                company.getName(),
                company.getOwner(),
                company.getEmail().address(),
                company.getPhoneNumber(),
                company.getBusinessType(),
                company.getIndustry(),
                company.getRegistrationNumber(),
                company.getRegistrationCertificateUrl(),
                company.getBankbookCopy(),
                company.getProfileUrl(),
                company.getStatus()
        );
    }
}