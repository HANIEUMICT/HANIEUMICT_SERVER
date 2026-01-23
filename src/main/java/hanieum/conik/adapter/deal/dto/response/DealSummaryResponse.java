package hanieum.conik.adapter.deal.dto.response;

import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.company.entity.CompanyAddress;
import hanieum.conik.domain.deal.entity.Deal;
import hanieum.conik.domain.deal.enumerate.DealStep;
import hanieum.conik.domain.deal.enumerate.DeliveryStatus;
import hanieum.conik.domain.project.entity.Project;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DealSummaryResponse(
        Long dealId,

        Long projectId,
        String projectTitle,
        String category,
        String categoryDetail,

        DealStep dealStep,
        int stepNumber,
        String stepLabel,

        DeliveryStatus deliveryStatus,

        LocalDate deadline, // 납기일
        Integer requestEstimate, // 추정액
        LocalDate publicUntil, // 입찰 마감일
        LocalDateTime dealStartedAt, // 거래 시작일

        Long companyId,
        String companyName,
        CompanyAddress companyAddress

) {
    public static DealSummaryResponse from(Deal deal, Project project, Company company) {
        return new DealSummaryResponse(
                deal.getId(),

                project.getId(),
                project.getProjectTitle(),
                project.getCategory(),
                project.getCategoryDetail(),

                deal.getDealStep(),
                deal.getDealStep().getStep(),
                deal.getDealStep().getDescription(),

                deal.getDeliveryStatus(),

                project.getDeadline(),
                project.getRequestEstimate(),
                project.getPublicUntil(),
                deal.getCreatedAt(),

                company.getId(),
                company.getName(),
                company.getAddress()
        );
    }
}
