package hanieum.conik.adapter.company.response;

import hanieum.conik.domain.company.entity.Company;

public record CompanyProfileResponse(
        Long companyId,
        String name,
        String logoUrl,
        Integer avgProductionLeadHours,
        Integer totalOrderCount,
        Integer repeatOrderCount,
        Integer avgResponseMinutes
) {
    public static CompanyProfileResponse from(Company c) {
        var d = c.getCompanyDetail();
        return new CompanyProfileResponse(
                c.getId(),
                c.getName(),
                d != null ? d.getLogoUrl() : null,
                d != null ? d.getAvgProductionLeadHours() : null,
                d != null ? d.getTotalOrderCount() : 0,
                d != null ? d.getRepeatOrderCount() : 0,
                d != null ? d.getAvgResponseMinutes() : null
        );
    }
}
