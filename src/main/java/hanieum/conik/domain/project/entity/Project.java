package hanieum.conik.domain.project.entity;

import hanieum.conik.adapter.project.dto.request.BidStatusUpdateRequest;
import hanieum.conik.adapter.project.dto.request.ProjectRegisterRequest;
import hanieum.conik.domain.deal.enumerate.DealStep;
import hanieum.conik.domain.project.enumerate.*;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends AbstractEntity {
    private Long memberId;

    private String projectTitle;

    private String category;

    private String categoryDetail;

    private String categoryDetailEtc;

    private String purpose;

    private String purposeEtc;

    private Integer projectQuantity;

    private String requests;

    private LocalDate deadline;

    private boolean canDeadlineChange;

    private Integer requestEstimate;

    private LocalDate publicUntil;

    private ProjectStatus projectStatus;

    private ConsultType consultType;

    private boolean canPhoneConsult;

    private Long addressId;

    private SubmitStatus submitStatus = SubmitStatus.INITIALIZE;

    private ProjectBidStatus projectBidStatus = ProjectBidStatus.PRE_BID;

    @Enumerated(EnumType.STRING)
    private DealStep currentStep = DealStep.OPEN;

    @BatchSize(size = 250)
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectDrawingFile> drawingFiles = new ArrayList<>();

    public static Project create(Long userId, String projectTitle, String category, String categoryDetail, String categoryDetailEtc,
                                 String purpose, String purposeEtc, Integer projectQuantity, String projectRequests,
                                 LocalDate projectDeadline, boolean canDeadlineChange, Integer projectRequestEstimate, LocalDate projectPublicUntil,
                                 ProjectStatus projectStatus, ConsultType consultType, boolean canPhoneConsult, Long addressId, DealStep currentStep
    ) {
        Project project = new Project();
        project.memberId            = userId;
        project.projectTitle        = projectTitle;
        project.category            = category;
        project.categoryDetail      = categoryDetail;
        project.categoryDetailEtc   = categoryDetailEtc;
        project.purpose             = purpose;
        project.purposeEtc          = purposeEtc;
        project.projectQuantity     = projectQuantity;
        project.requests            = projectRequests;
        project.deadline            = projectDeadline;
        project.canDeadlineChange   = canDeadlineChange;
        project.requestEstimate     = projectRequestEstimate;
        project.publicUntil         = projectPublicUntil;
        project.projectStatus       = projectStatus;
        project.consultType         = consultType;
        project.canPhoneConsult     = canPhoneConsult;
        project.addressId           = addressId;
        project.currentStep         = currentStep;
        return project;
    }

    public static Project initiate(Long memberId) {
        Project project = new Project();
        project.memberId = memberId;
        return project;
    }

    public void update(ProjectRegisterRequest request) {
        this.memberId = request.memberId();
        this.projectTitle = request.projectTitle();
        this.category = request.category();
        this.categoryDetail = request.categoryDetail();
        this.categoryDetailEtc = request.categoryDetailEtc();
        this.purpose = request.purpose();
        this.purposeEtc = request.purposeEtc();
        this.projectQuantity = request.projectQuantity();
        this.requests = request.requests();
        this.deadline = request.deadline();
        this.canDeadlineChange = request.canDeadlineChange();
        this.requestEstimate = request.requestEstimate();
        this.publicUntil = request.publicUntil();
        this.consultType = request.consultType();
        this.projectStatus = request.projectStatus();
        this.canPhoneConsult = request.canPhoneConsult();
        this.addressId = request.addressId();
        this.submitStatus = request.submitStatus();

        finalizeDrawingFiles();
    }

    public void updateBidStatusAndPublicUntil(BidStatusUpdateRequest bidStatusUpdateRequest) {
        this.projectBidStatus = bidStatusUpdateRequest.projectBidStatus();
        this.publicUntil = bidStatusUpdateRequest.publicUntil();
    }

    public void addDrawing(ProjectDrawingFile drawingFile) {                                     
        if (!this.drawingFiles.contains(drawingFile)) {
            this.drawingFiles.add(drawingFile);
            drawingFile.updateProject(this);
        }
    }

    public void removeDrawing(ProjectDrawingFile drawingFile) {
        this.drawingFiles.remove(drawingFile);
        drawingFile.updateProject(null);
    }

    public void finalizeDrawingFiles() { this.drawingFiles.forEach(ProjectDrawingFile::finalizeFile); }

    // TODO : Progress 변경 시 currentStep도 함께 갱신되도록 수정 (데이터 일관성 유지)
    public void updateCurrentStep(DealStep step) {
        this.currentStep = step;
    }
}