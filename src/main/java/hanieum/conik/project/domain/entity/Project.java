package hanieum.conik.project.domain.entity;

import hanieum.conik.global.domain.BaseEntity;
import hanieum.conik.project.domain.enumerate.ProjectStatus;
import hanieum.conik.project.domain.enumerate.ProjectType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long projectId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String projectTitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectType projectType;

    @Column(nullable = false)
    private String projectCategory;

    @Column(nullable = false)
    private String projectCategoryService;

    @Column(nullable = false)
    private String projectPurpose;

    @Column(nullable = false)
    private String projectPurposeDetail;

    private String projectDrawing;

    private Integer projectQuantity;

    @Column(columnDefinition = "TEXT")
    private String projectRequests;

    private LocalDate projectDeadline;

    @Column(nullable = false)
    private boolean canDeadlineChange;

    private Integer projectRequestEstimate;

    private LocalDate projectPublicUntil;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    @Column(nullable = false)
    private boolean canPhoneConsult;

    @Column(nullable = false)
    private String projectAddress;

    public static Project create(Long userId, String projectTitle, ProjectType projectType, String projectCategory,
                                 String projectCategoryService, String projectPurpose, String projectPurposeDetail,
                                 String projectDrawing, Integer projectQuantity, String projectRequests, LocalDate projectDeadline,
                                 boolean canDeadlineChange, Integer projectRequestEstimate, LocalDate projectPublicUntil,
                                 ProjectStatus projectStatus, boolean canPhoneConsult, String projectAddress) {
        Project project = new Project();
        project.userId = userId;
        project.projectTitle = projectTitle;
        project.projectType = projectType;
        project.projectCategory = projectCategory;
        project.projectCategoryService = projectCategoryService;
        project.projectPurpose = projectPurpose;
        project.projectPurposeDetail = projectPurposeDetail;
        project.projectDrawing = projectDrawing;
        project.projectQuantity = projectQuantity;
        project.projectRequests = projectRequests;
        project.projectDeadline = projectDeadline;
        project.canDeadlineChange = canDeadlineChange;
        project.projectRequestEstimate = projectRequestEstimate;
        project.projectPublicUntil = projectPublicUntil;
        project.projectStatus = projectStatus;
        project.canPhoneConsult = canPhoneConsult;
        project.projectAddress = projectAddress;
        return project;
    }
}
