package hanieum.conik.adapter.company.response;

import hanieum.conik.domain.company.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CompanyDetailResponse(
        CompanyResponse company,                    // ← Company 전용 DTO 재사용
        DetailDto detail,                           // 상세(1:1)
        List<EquipmentResponse> equipments,     // 장비 목록
        List<PortfolioResponse> portfolios      // 포트폴리오 목록
) {

    public static CompanyDetailResponse fromCompanyOnly(Company c) {
        return new CompanyDetailResponse(
                CompanyResponse.from(c),
                null,
                List.of(),
                List.of()
        );
    }

    public static CompanyDetailResponse from(Company c, List<Equipment> eqs, List<Portfolio> pfs) {
        CompanyDetail d = c.getCompanyDetail();
        return new CompanyDetailResponse(
                CompanyResponse.from(c),
                d != null ? DetailDto.from(d) : null,
                eqs == null ? List.of() : eqs.stream().map(EquipmentResponse::from).toList(),
                pfs == null ? List.of() : pfs.stream().map(PortfolioResponse::from).toList()
        );
    }

    public record DetailDto(
            Long detailId,
            LocalDate establishedAt,
            String logoUrl,
            Integer employeeCount,
            String websiteUrl,
            String contactAvailableTime,
            String description,
            LocalDateTime modifiedAt,
            Integer rating,
            Integer totalOrderCount,
            Integer repeatOrderCount,
            Integer avgProductionLeadHours,
            Integer avgResponseMinutes
    ) {
        public static DetailDto from(CompanyDetail d) {
            return new DetailDto(
                    d.getId(),
                    d.getEstablishedAt(),
                    d.getLogoUrl(),
                    d.getEmployeeCount(),
                    d.getWebsiteUrl(),
                    d.getContactAvailableTime(),
                    d.getDescription(),
                    d.getModifiedAt(),
                    d.getRating(),
                    d.getTotalOrderCount(),
                    d.getRepeatOrderCount(),
                    d.getAvgProductionLeadHours(),
                    d.getAvgResponseMinutes()
            );
        }
    }
}