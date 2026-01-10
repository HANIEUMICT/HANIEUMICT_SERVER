package hanieum.conik.application.deal;

import hanieum.conik.adapter.deal.dto.response.DealSummaryResponse;
import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.deal.provided.DealFinder;
import hanieum.conik.application.deal.required.DealRepository;
import hanieum.conik.application.project.provided.ProjectFinder;
import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.deal.entity.Deal;
import hanieum.conik.domain.deal.enumerate.DealStep;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.global.adapter.security.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DealQueryService implements DealFinder {

    private final DealRepository dealRepository;
    private final ProjectFinder projectFinder;
    private final CompanyFinder companyFinder;

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
}
