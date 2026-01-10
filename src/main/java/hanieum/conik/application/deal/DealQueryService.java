package hanieum.conik.application.deal;

import hanieum.conik.adapter.deal.dto.response.DealStepDetailResponse;
import hanieum.conik.adapter.deal.dto.response.DealSummaryResponse;
import hanieum.conik.adapter.deal.dto.response.DealTimelineResponse;
import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.deal.provided.DealFinder;
import hanieum.conik.application.deal.required.*;
import hanieum.conik.application.project.provided.ProjectFinder;
import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.deal.entity.Deal;
import hanieum.conik.domain.deal.enumerate.DealStep;
import hanieum.conik.domain.deal.exception.DealErrorType;
import hanieum.conik.domain.deal.exception.DealException;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.global.adapter.security.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DealQueryService implements DealFinder {

    private final DealRepository dealRepository;
    private final ProjectFinder projectFinder;
    private final CompanyFinder companyFinder;

    private final DealContractRepository contractRepository;
    private final DealInspectionRepository inspectionRepository;
    private final DealSampleProductionRepository sampleProductionRepository;
    private final DealSampleDeliveryRepository sampleDeliveryRepository;
    private final DealSampleApprovalRepository sampleApprovalRepository;
    private final DealMassProductionRepository massProductionRepository;
    private final DealProductDeliveryRepository productDeliveryRepository;

    @Override
    public Page<DealSummaryResponse> getDeals(AuthDetails authDetails, Pageable pageable) {
        // 거래 수락된 Deal 조회
        List<DealStep> acceptedSteps =
                Arrays.stream(DealStep.values())
                        .filter(step -> step.getStep() >= 1)
                        .toList();
        Page<Deal> deals =
                dealRepository.findAcceptedDeals(acceptedSteps, pageable);

        // Deal에 연관된 Project 일괄 조회
        Set<Long> projectIds = deals.stream()
                .map(Deal::getProjectId)
                .collect(Collectors.toSet());
        Map<Long, Project> projectMap =
                projectFinder.findProjectsByIds(projectIds).stream()
                        .collect(Collectors.toMap(Project::getId, Function.identity()));

        // Deal에 연관된 Company 일괄 조회
        Set<Long> companyIds = deals.stream()
                .map(Deal::getCompanyId)
                .collect(Collectors.toSet());
        Map<Long, Company> companyMap =
                companyFinder.findCompaniesByIds(companyIds).stream()
                        .collect(Collectors.toMap(Company::getId, Function.identity()));

        return deals.map(deal -> {
            Project project = projectMap.get(deal.getProjectId());
            Company company = companyMap.get(deal.getCompanyId());

            return DealSummaryResponse.from(deal, project, company);
        });
    }

    @Override
    public DealTimelineResponse getDealTimeline(Long dealId) {

        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new DealException(DealErrorType.DEAL_NOT_FOUND));

        List<DealStepDetailResponse> timeline = new ArrayList<>();

        for (DealStep step : DealStep.values()) {
            timeline.add(buildStepDetail(step, dealId));
        }

        return new DealTimelineResponse(
                deal.getId(),
                deal.getDealStep(),
                timeline
        );
    }

    private DealStepDetailResponse buildStepDetail(
            DealStep step,
            Long dealId
    ) {
        return switch (step) {

            case CONTRACT_CONFIRMED ->
                    contractRepository.findByDealId(dealId)
                            .map(contract ->
                                    DealStepDetailResponse.from(
                                            step,
                                            Map.of(
                                                    "confirmedAt", contract.getConfirmedAt()
                                            )
                                    )
                            )
                            .orElse(DealStepDetailResponse.empty(step));

            case COMPANY_INSPECTION_COMPLETED ->
                    inspectionRepository.findByDealId(dealId)
                            .map(inspection ->
                                    DealStepDetailResponse.from(
                                            step,
                                            Map.of(
                                                    "completedAt", inspection.getCompletedAt()
                                            )
                                    )
                            )
                            .orElse(DealStepDetailResponse.empty(step));

            case SAMPLE_PRODUCTION ->
                    sampleProductionRepository.findByDealId(dealId)
                            .map(sample ->
                                    DealStepDetailResponse.from(
                                            step,
                                            Map.of(
                                                    "startDate", sample.getStartDate(),
                                                    "endDate", sample.getEndDate(),
                                                    "images", sample.getImageUrls()
                                            )
                                    )
                            )
                            .orElse(DealStepDetailResponse.empty(step));

            case SAMPLE_DELIVERY ->
                    sampleDeliveryRepository.findByDealId(dealId)
                            .map(delivery ->
                                    DealStepDetailResponse.from(
                                            step,
                                            Map.of(
                                                    "deliveredAt", delivery.getDeliveredAt(),
                                                    "address", delivery.getAddress()
                                            )
                                    )
                            )
                            .orElse(DealStepDetailResponse.empty(step));

            case SAMPLE_APPROVED, SAMPLE_REJECTED ->
                    sampleApprovalRepository.findByDealId(dealId)
                            .map(approval ->
                                    DealStepDetailResponse.from(
                                            step,
                                            Map.of(
                                                    "approved", approval.isApproved(),
                                                    "documentUrl", approval.getDocumentUrl()
                                            )
                                    )
                            )
                            .orElse(DealStepDetailResponse.empty(step));

            case MASS_PRODUCTION, MASS_PRODUCTION_COMPLETED ->
                    massProductionRepository.findByDealId(dealId)
                            .map(prod ->
                                    DealStepDetailResponse.from(
                                            step,
                                            Map.of(
                                                    "startDate", prod.getStartDate(),
                                                    "images", prod.getImageUrls()
                                            )
                                    )
                            )
                            .orElse(DealStepDetailResponse.empty(step));

            case PRODUCT_DELIVERY ->
                    productDeliveryRepository.findByDealId(dealId)
                            .map(delivery ->
                                    DealStepDetailResponse.from(
                                            step,
                                            Map.of(
                                                    "startedAt", delivery.getStartedAt()
                                            )
                                    )
                            )
                            .orElse(DealStepDetailResponse.empty(step));

            default -> DealStepDetailResponse.empty(step);
        };
    }
}
