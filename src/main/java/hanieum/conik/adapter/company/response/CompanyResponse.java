package hanieum.conik.adapter.company.response;

import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.company.enumerate.CompanyStatus;

public record CompanyResponse(
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
        CompanyStatus status,
        CompanyAddressResponse address
) {
    public static CompanyResponse from(Company c) {
        return new CompanyResponse(
                c.getId(),
                c.getName(),
                c.getOwner(),
                c.getEmail() != null ? c.getEmail().address() : null, // Email 값객체 → 문자열
                c.getPhoneNumber(),
                c.getBusinessType(),
                c.getIndustry(),
                c.getRegistrationNumber(),
                c.getRegistrationCertificateUrl(),
                c.getBankbookCopy(),
                c.getProfileUrl(),
                c.getStatus(),
                CompanyAddressResponse.from(c.getAddress())
        );
    }
}