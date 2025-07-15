package hanieum.conik.quote.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Table(name = "project")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

//    private User user;

    @NotBlank(message = "프로젝트 이름은 필수 입력 값입니다.")
    @Column(name = "project_name", nullable = false, length = 255)
    private String name;

    @NotBlank(message = "프로젝트 유형은 필수 입력 값입니다.")
    @Column(name = "project_type", nullable = false)
    private String type;

    @NotBlank(message = "프로젝트 카테고리는 필수 입력 값입니다.")
    @Column(name = "project_category", nullable = false)
    private String category;

    @NotBlank(message = "프로젝트 목적은 필수 입력 값입니다.")
    @Column(name = "project_purpose", nullable = false)
    private String purpose;

    @Column(name = "project_purpose_detail")
    private String purposeDetail;

    @Column(name = "project_drawing")
    private String drawing;

    @NotNull(message = "수량은 필수 입력 값입니다.")
    @Column(name = "project_quantity", nullable = false)
    private Integer quantity;

    @NotBlank(message = "요청 상세는 필수 입력 값입니다.")
    @Column(name = "project_request_details", nullable = false)
    private String requestDetails;

    @NotNull(message = "마감일은 필수 입력 값입니다.")
    @Column(name = "project_deadline", nullable = false)
    private LocalDate deadline;

    @NotNull(message = "예상 예산은 필수 입력 값입니다.")
    @Column(name = "project_estimated_budget", precision = 15, scale = 2, nullable = false)
    private BigDecimal estimatedBudget;

    @NotNull(message = "입찰 마감일은 필수 입력 값입니다.")
    @Column(name = "project_bidding_deadline", nullable = false)
    private LocalDate biddingDeadline;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

//    private List<Factory> publicFactoryList;

    @NotNull(message = "전화 상담 요청 여부는 필수 입력 값입니다.")
    @Column(name = "phone_consultation", nullable = false)
    private Boolean isPhoneConsultationRequested;

    @NotBlank(message = "배송지 주소는 필수 입력 값입니다.")
    @Column(name = "delivery_address", nullable = false, length = 500)
    private String address;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "project_progress", nullable = false, length = 20)
    private ProjectProgressStatus progress = ProjectProgressStatus.BEFORE_BIDDING;
}
