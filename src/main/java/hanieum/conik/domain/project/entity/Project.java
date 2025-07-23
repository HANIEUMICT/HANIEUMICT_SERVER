package hanieum.conik.domain.project.entity;

import hanieum.conik.domain.project.enumerate.ProjectStatus;
import hanieum.conik.domain.project.enumerate.ProjectType;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends AbstractEntity {
    private Long memberId;

    private String projectTitle;

    private ProjectType type;

    private String category;

    private String categoryService;

    private String purpose;

    private String purposeDetail;

    private Integer projectQuantity;

    private String requests;

    private LocalDate deadline;

    private boolean canDeadlineChange;

    private Integer requestEstimate;

    private LocalDate publicUntil;

    private ProjectStatus projectStatus;

    private boolean canPhoneConsult;

    private String deliveryAddress;

    private boolean isFinalized;

    public static Project create(Long userId, String projectTitle, ProjectType projectType, String projectCategory, String projectCategoryService,
                                 String projectPurpose, String projectPurposeDetail, Integer projectQuantity, String projectRequests,
                                 LocalDate projectDeadline, boolean canDeadlineChange, Integer projectRequestEstimate, LocalDate projectPublicUntil,
                                 ProjectStatus projectStatus, boolean canPhoneConsult, String projectAddress
    ) {
        Project project = new Project();
        project.memberId              = userId;
        project.projectTitle        = projectTitle;
        project.type                = projectType;
        project.category            = projectCategory;
        project.categoryService     = projectCategoryService;
        project.purpose             = projectPurpose;
        project.purposeDetail       = projectPurposeDetail;
        project.projectQuantity     = projectQuantity;
        project.requests            = projectRequests;
        project.deadline            = projectDeadline;
        project.canDeadlineChange   = canDeadlineChange;
        project.requestEstimate     = projectRequestEstimate;
        project.publicUntil         = projectPublicUntil;
        project.projectStatus       = projectStatus;
        project.canPhoneConsult     = canPhoneConsult;
        project.deliveryAddress     = projectAddress;
        return project;
    }

    public static Project create(Long memberId) {
        Project project = new Project();
        project.memberId = memberId;
        return project;
    }
}