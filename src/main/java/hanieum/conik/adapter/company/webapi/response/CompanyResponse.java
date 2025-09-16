package hanieum.conik.adapter.company.webapi.response;

import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.company.entity.CompanyAddress;
import hanieum.conik.domain.company.enumerate.CompanyStatus;

import java.util.List;

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
        AddressDto address
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
                AddressDto.from(c.getAddress())
        );
    }

    public record AddressDto(
            String zipCode,
            String road,
            String detail
    ) {
        public static AddressDto from(CompanyAddress a) {
            if (a == null) return null;
            return new AddressDto(
                    a.getPostalCode(),
                    a.getStreetAddress(),
                    a.getDetailAddress()
            );
        }
    }
}