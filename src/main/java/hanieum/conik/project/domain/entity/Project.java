package hanieum.conik.project.domain.entity;

import hanieum.conik.global.domain.BaseEntity;
import hanieum.conik.project.domain.enumerate.ProjectStatus;
import hanieum.conik.project.domain.enumerate.ProjectType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseEntity {
    private Long projectId;

    private Long userId;

    private String projectTitle;

    private ProjectType projectType;

    private String projectCategory;

    private String projectCategoryService;

    private String projectPurpose;

    private String projectPurposeDetail;

    private String projectDrawing;

    private Integer projectQuantity;

    private String projectRequests;

    private LocalDate projectDeadline;

    private boolean canDeadlineChange;

    private Integer projectRequestEstimate;

    private LocalDate projectPublicUntil;

    private ProjectStatus projectStatus;

    private boolean canPhoneConsult;

    private String projectAddress;

    public static Project create(Long userId, String projectTitle, ProjectType projectType, String projectCategory, String projectCategoryService,
                                 String projectPurpose, String projectPurposeDetail, String projectDrawing, Integer projectQuantity,
                                 String projectRequests, LocalDate projectDeadline, boolean canDeadlineChange, Integer projectRequestEstimate,
                                 LocalDate projectPublicUntil, ProjectStatus projectStatus, boolean canPhoneConsult, String projectAddress
    ) {
        Project project = new Project();
        project.userId                 = userId;
        project.projectTitle           = projectTitle;
        project.projectType            = projectType;
        project.projectCategory        = projectCategory;
        project.projectCategoryService = projectCategoryService;
        project.projectPurpose         = projectPurpose;
        project.projectPurposeDetail   = projectPurposeDetail;
        project.projectDrawing         = projectDrawing;
        project.projectQuantity        = projectQuantity;
        project.projectRequests        = projectRequests;
        project.projectDeadline        = projectDeadline;
        project.canDeadlineChange      = canDeadlineChange;
        project.projectRequestEstimate = projectRequestEstimate;
        project.projectPublicUntil     = projectPublicUntil;
        project.projectStatus          = projectStatus;
        project.canPhoneConsult        = canPhoneConsult;
        project.projectAddress         = projectAddress;
        return project;
    }
}