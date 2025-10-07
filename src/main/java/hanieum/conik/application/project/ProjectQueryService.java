package hanieum.conik.application.project;

import hanieum.conik.adapter.project.dto.request.ProjectListResponse;
import hanieum.conik.adapter.project.dto.request.ProjectStatusSummary;
import hanieum.conik.adapter.project.dto.response.ProjectDetailResponse;
import hanieum.conik.adapter.project.dto.response.ProjectWithProposalsResponse;
import hanieum.conik.adapter.proposal.dto.response.ProposalThumbnailResponse;
import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.favorite.required.FavoriteRepository;
import hanieum.conik.application.member.required.MemberRepository;
import hanieum.conik.application.project.provided.ProjectFinder;
import hanieum.conik.application.project.required.ProjectRepository;
import hanieum.conik.application.proposal.provided.ProposalFinder;
import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.member.exception.MemberErrorType;
import hanieum.conik.domain.member.exception.MemberException;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.enumerate.ProgressStatus;
import hanieum.conik.domain.project.enumerate.ProjectProgressStep;
import hanieum.conik.domain.project.enumerate.SubmitStatus;
import hanieum.conik.domain.project.exception.ProjectErrorType;
import hanieum.conik.domain.project.exception.ProjectException;
import hanieum.conik.domain.proposal.domain.entity.Proposal;
import hanieum.conik.global.adapter.security.AuthDetails;
import jakarta.persistence.criteria.Expression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectQueryService implements ProjectFinder {
    private final ProjectRepository projectRepository;
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final ProposalFinder proposalFinder;
    private final CompanyFinder companyFinder;

    @Override
    public Project findProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(ProjectErrorType.PROJECT_NOT_FOUND));
    }

    // TODO : Pagable 응답 커스텀하여 전체적으로 필요한 필드만 반환하도록 수정
    @Override
    public ProjectListResponse getMemberProjects(
            AuthDetails authDetails, Long memberId, SubmitStatus submitStatus, ProgressStatus progressStatus, Pageable pageable
    ) {
        Member currentMember = (authDetails != null)
                ? memberRepository.findById(authDetails.getMemberId())
                    .orElseThrow(() -> new MemberException(MemberErrorType.MEMBER_NOT_FOUND))
                : null;

        Specification<Project> spec = buildProjectSpec(memberId, submitStatus, progressStatus);
        Page<Project> projects = projectRepository.findAll(spec, pageable);

        ProjectStatusSummary summary = progressStatus != null
                ? buildProjectStatusSummary(memberId, submitStatus)
                : null ;
        Page<ProjectDetailResponse> projectResponses = projects.map(project -> buildProjectResponse(project, currentMember));

        return ProjectListResponse.from(summary, projectResponses);
    }

    @Override
    public Page<ProjectDetailResponse> findProjectsByCompanyId(Long companyId, Pageable pageable) {
        Page<Project> projects = projectRepository.findByCompanyId(companyId, pageable);
        return projects.map(ProjectDetailResponse::from);
    }

    @Override
    public Project validateProjectOpenStatus(Long projectId){
        Project project = findProject(projectId);
        if (project.getPublicUntil().isBefore(LocalDate.now())) {
            throw new ProjectException(ProjectErrorType.PROJECT_EXPIRED);
        }
        return project;
    }

    @Override
    public ProjectWithProposalsResponse getProjectDetailWithProposals(Long projectId) {
        // 1. 프로젝트 조회
        Project project = projectRepository.findByIdWithDrawingFiles(projectId)
                .orElseThrow(() -> new ProjectException(ProjectErrorType.PROJECT_NOT_FOUND));

        // 2. ProposalFinder를 통해 제안서들 조회
        List<Proposal> proposals = proposalFinder.findSubmittedProposalsByProjectId(projectId);

        // 3. 회사들 일괄 로딩 후 매핑
        Set<Long> companyIds = proposals.stream()
                .map(Proposal::getCompanyId)
                .collect(Collectors.toSet());

        Map<Long, Company> companyMap = companyFinder.findCompaniesByIds(companyIds).stream()
                .collect(Collectors.toMap(Company::getId, Function.identity()));

        // 4) DTO 조합 (단건 조회 제거)
        List<ProposalThumbnailResponse> proposalThumbnails = proposals.stream()
                .map(p -> ProposalThumbnailResponse.from(p, companyMap.get(p.getCompanyId())))
                .toList();

        // 4. 최종 응답 조합
        return new ProjectWithProposalsResponse(
                ProjectDetailResponse.from(project),
                proposalThumbnails
        );
    }

    private ProposalThumbnailResponse createProposalThumbnailResponse(Proposal proposal) {
        Company companyWithDetail = companyFinder.findCompany(proposal.getCompanyId());

        return ProposalThumbnailResponse.from(proposal, companyWithDetail);
    }

    private Specification<Project> buildProjectSpec(
            Long memberId,
            SubmitStatus submitStatus,
            ProgressStatus progressStatus
    ) {
        Specification<Project> spec = Specification.where(null);

        if (memberId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("memberId"), memberId));
        }

        if (submitStatus == null) {
            spec = spec.and((root, query, cb) ->
                    root.get("submitStatus").in(List.of(SubmitStatus.TEMPORARY_SAVE, SubmitStatus.SUBMIT)));
        } else {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("submitStatus"), submitStatus));
        }

        if (progressStatus != null) {
            spec = spec.and((root, query, cb) -> {
                Expression<ProjectProgressStep> stepExpr = root.get("currentStep");

                // TODO : ENUM → DB 저장 시 step 필드로 변환하는 Converter 추가하여 매핑 일관성 유지 고려중
                return switch (progressStatus) {
                    case BEFORE -> cb.or(
                            cb.isNull(stepExpr),
                            stepExpr.in(ProjectProgressStep.OPEN, ProjectProgressStep.REQUESTED)
                    );
                    case IN_PROGRESS -> stepExpr.in(
                            ProjectProgressStep.CONTRACT_CONFIRMED,
                            ProjectProgressStep.COMPANY_INSPECTION_COMPLETED,
                            ProjectProgressStep.SAMPLE_PRODUCTION,
                            ProjectProgressStep.SAMPLE_PRODUCTION_COMPLETED,
                            ProjectProgressStep.SAMPLE_DELIVERY,
                            ProjectProgressStep.SAMPLE_DELIVERED,
                            ProjectProgressStep.SAMPLE_APPROVED,
                            ProjectProgressStep.SAMPLE_REJECTED,
                            ProjectProgressStep.MASS_PRODUCTION,
                            ProjectProgressStep.MASS_PRODUCTION_COMPLETED,
                            ProjectProgressStep.PRODUCT_DELIVERY
                    );
                    case COMPLETED -> stepExpr.in(ProjectProgressStep.CLOSED);
                };
            });
        }

        return spec;
    }

    private ProjectDetailResponse buildProjectResponse(Project project, Member currentMember) {
        long favoriteCount = favoriteRepository.countByProjectId(project.getId());
        boolean isFavorite = (currentMember != null)
                && favoriteRepository.existsByCompanyIdAndProjectId(currentMember.getCompanyId(), project.getId());

        return ProjectDetailResponse.from(project, favoriteCount, isFavorite);
    }

    private ProjectStatusSummary buildProjectStatusSummary(Long memberId, SubmitStatus submitStatus) {
        long beforeCount = projectRepository.count(buildProjectSpec(memberId, submitStatus, ProgressStatus.BEFORE));
        long inProgressCount = projectRepository.count(buildProjectSpec(memberId, submitStatus, ProgressStatus.IN_PROGRESS));
        long completedCount = projectRepository.count(buildProjectSpec(memberId, submitStatus, ProgressStatus.COMPLETED));

        return ProjectStatusSummary.from(beforeCount, inProgressCount, completedCount);
    }
}
