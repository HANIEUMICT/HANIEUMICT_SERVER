package hanieum.conik.domain.company.entity;

import hanieum.conik.domain.company.dto.CompanyRegisterRequest;
import hanieum.conik.domain.company.enumerate.CompanyStatus;
import hanieum.conik.domain.common.email.Email;
import hanieum.conik.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 회사 이름

    @Column(nullable = false)
    private String owner;  // 대표자 이름

    @Column(nullable = false)
    private Email email; // 회사 이메일

    @Column(nullable = false)
    private String phoneNumber; // 회사 전화

    @Column(nullable = false)
    private String businessType; // 업태

    @Column(nullable = false)
    private String industry; // 종목

    @Column(nullable = false)
    private String registrationNumber; // 사업자 등록번호

    @Column(nullable = false, length = 2048)
    private String registrationCertificateUrl; // 사업자 등록증 URL

    @Column(nullable = false, length = 2048)
    private String bankbookCopy;  // 통장사본

    @Column(nullable = false, length = 2048)
    private String profileUrl; // 회사 소개서

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyStatus status;

    @Embedded
    private CompanyAddress address;

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = LAZY)
    private CompanyDetail companyDetail;

    private Company(String name, String owner, Email email, String phoneNumber, String businessType, String industry, String registrationNumber, String registrationCertificateUrl, String profileUrl, String bankbookCopy, CompanyAddress address) {
        this.businessType = businessType;
        this.email = email;
        this.industry = industry;
        this.name = name;
        this.owner = owner;
        this.phoneNumber = phoneNumber;
        this.bankbookCopy = bankbookCopy;
        this.profileUrl = profileUrl;
        this.registrationCertificateUrl = registrationCertificateUrl;
        this.registrationNumber = registrationNumber;
        this.status = CompanyStatus.REGISTER_APPROVED;
        this.address = address;
    }

    /**
     * 기업 등록
     * */
    public static Company register(CompanyRegisterRequest request) {
        return new Company(
                request.name(),
                request.owner(),
                new Email(request.email()),
                request.phoneNumber(),
                request.businessType(),
                request.industry(),
                request.registrationNumber(),
                request.registrationCertificateUrl(),
                request.profileUrl(),
                request.bankbookCopy(),
                CompanyAddress.from(request.addressRegisterRequest())
        );
    }

    /**
     * 기업 정보 수정
     * */
    public void update(CompanyRegisterRequest request){
        this.name = request.name();
        this.owner = request.owner();
        this.email = new Email(request.email());
        this.phoneNumber = request.phoneNumber();
        this.businessType = request.businessType();
        this.industry = request.industry();
        this.registrationNumber = request.registrationNumber();
        this.registrationCertificateUrl = request.registrationCertificateUrl();
        this.profileUrl = request.profileUrl();
        this.bankbookCopy = request.bankbookCopy();
        this.address = CompanyAddress.from(request.addressRegisterRequest());
    }

    /**
     * 기업 상세 정보 등록
     * */
    public void attachDetail(CompanyDetail companyDetail) {
        this.companyDetail = companyDetail;
        if (companyDetail != null) companyDetail.setCompany(this);
    }

    /**
     * 기업 상세 정보 삭제
     */
    public void removeDetail() {
        if (this.companyDetail == null) return;
        CompanyDetail d = this.companyDetail;
        this.companyDetail = null; // ← orphanRemoval 트리거 (Detail DELETE)
        d.setCompany(null);        // 양방향 정리
    }
}
