package hanieum.conik.application.project;

import hanieum.conik.adapter.project.dto.response.ProjectDetailResponse;
import hanieum.conik.adapter.project.dto.response.ProjectWithProposalsResponse;
import hanieum.conik.adapter.proposal.dto.response.ProposalThumbnailResponse;
import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.project.provided.ProjectFinder;
import hanieum.conik.application.project.required.ProjectRepository;
import hanieum.conik.application.proposal.provided.ProposalFinder;
import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.enumerate.SubmitStatus;
import hanieum.conik.domain.project.exception.ProjectErrorType;
import hanieum.conik.domain.project.exception.ProjectException;
import hanieum.conik.domain.proposal.domain.entity.Proposal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final ProposalFinder proposalFinder;
    private final CompanyFinder companyFinder;

    @Override
    public Project findProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(ProjectErrorType.PROJECT_NOT_FOUND));
    }

    @Override
    public Page<ProjectDetailResponse> getMemberProjects(Long memberId, SubmitStatus submitStatus, Pageable pageable) {
        Page<Project> projects;

        if (memberId != null) {
            projects = findProjectsWithStatus(submitStatus, memberId, pageable);
        } else {
            projects = findAllProjectsWithStatus(submitStatus, pageable);
        }

        return projects.map(ProjectDetailResponse::from);
    }

    private Page<Project> findProjectsWithStatus(SubmitStatus submitStatus, Long memberId, Pageable pageable) {
        return (submitStatus == null)
                ? projectRepository.findByMemberIdAndSubmitStatusIn(memberId, List.of(SubmitStatus.TEMPORARY_SAVE, SubmitStatus.SUBMIT), pageable)
                : projectRepository.findByMemberIdAndSubmitStatus(memberId, submitStatus, pageable);
    }

    private Page<Project> findAllProjectsWithStatus(SubmitStatus submitStatus, Pageable pageable) {
        return (submitStatus == null)
                ? projectRepository.findBySubmitStatusIn(List.of(SubmitStatus.TEMPORARY_SAVE, SubmitStatus.SUBMIT), pageable)
                : projectRepository.findBySubmitStatus(submitStatus, pageable);
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

        Map<Long, Company> companyMap = companyFinder.findAllCompany().stream()
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
}
