package hanieum.conik.adapter.company.response;

import hanieum.conik.domain.company.entity.Company;

public record CompanyThumbnailResponse(
        Long companyId,

        String companyName,

        String profileUrl
) {
    public static CompanyThumbnailResponse from(Company company) {
        return new CompanyThumbnailResponse(
                company.getId(),
                company.getName(),
                company.getCompanyDetail().getLogoUrl()
        );
    }}
