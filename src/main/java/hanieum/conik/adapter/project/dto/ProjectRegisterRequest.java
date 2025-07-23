package hanieum.conik.adapter.project.dto;

import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.enumerate.ProjectStatus;
import hanieum.conik.domain.project.enumerate.ProjectType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ProjectRegisterRequest (
        @NotNull
        @Schema(description = "멤버 아이디", example = "1")
        Long memberId,

        @NotNull
        @Schema(description = "프로젝트(공고) 제목", example = "새로운 프로젝트 제목")
        String projectTitle,

        @NotNull
        @Schema(description = "프로젝트(공고) 제조 분류", example = "PRODUCT_DEVELOPMENT | FINAL_PRODUCT")
        ProjectType type,

        @NotNull
        @Schema(description = "프로젝트(공고) 제조 서비스 카테고리", example = "종류1")
        String category,

        @NotNull
        @Schema(description = "프로젝트(공고) 요청할 제조 서비스", example = "종류2")
        String categoryService,

        @NotNull
        @Schema(description = "프로젝트(공고) 제품 용도", example = "종류3")
        String purpose,

        @NotNull
        @Schema(description = "프로젝트(공고) 상세 제품 용도", example = "새로운 제품 용도 설명")
        String purposeDetail,

        @NotNull
        @Schema(description = "프로젝트(공고) 제조 수량", example = "1000")
        Integer projectQuantity,

        @NotNull
        @Schema(description = "프로젝트(공고) 세부 요청 사항", example = "새로운 요청 사항 설명")
        String requests,

        @NotNull
        @Schema(description = "프로젝트(공고) 닙기일", example = "2025-12-31")
        LocalDate deadline,

        @NotNull
        @Schema(description = "프로젝트(공고) 납기일 협의 가능 여부", example = "true")
        Boolean canDeadlineChange,

        @NotNull
        @Schema(description = "프로젝트(공고) 추정 예산", example = "1000000")
        Integer requestEstimate,

        @NotNull
        @Schema(description = "프로젝트(공고) 입찰 마감일", example = "2025-12-31")
        LocalDate publicUntil,

        @NotNull
        @Schema(description = "프로젝트(공고) 작성 상태", example = "PUBLIC | PROTECTED | PRIVATE")
        ProjectStatus projectStatus,

        @NotNull
        @Schema(description = "프로젝트(공고) 전화 상담 여부", example = "true")
        Boolean canPhoneConsult,

        @NotNull
        @Schema(description = "배송지", example = "123456")
        String deliveryAddress,

        @NotNull
        @Schema(description = "작성 상태", example = "123456")
        Boolean isFinalized
){
        public static ProjectRegisterRequest from(Project project) {
                return new ProjectRegisterRequest(
                        project.getMemberId(),
                        project.getProjectTitle(),
                        project.getType(),
                        project.getCategory(),
                        project.getCategoryService(),
                        project.getPurpose(),
                        project.getPurposeDetail(),
                        project.getProjectQuantity(),
                        project.getRequests(),
                        project.getDeadline(),
                        project.isCanDeadlineChange(),
                        project.getRequestEstimate(),
                        project.getPublicUntil(),
                        project.getProjectStatus(),
                        project.isCanPhoneConsult(),
                        project.getDeliveryAddress(),
                        project.isFinalized()
                );
        }
}
