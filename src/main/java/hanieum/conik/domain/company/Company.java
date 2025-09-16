package hanieum.conik.domain.company;

import hanieum.conik.domain.common.address.dto.AddressRegisterRequest;
import hanieum.conik.domain.company.dto.CompanyRegisterRequest;
import hanieum.conik.domain.company.enumerate.CompanyStatus;
import hanieum.conik.domain.member.shared.Email;
import hanieum.conik.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Embedded
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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private CompanyDetail companyDetail;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "company_id", nullable = false)
    private List<CompanyAddress> addresses = new ArrayList<>();

    private Company(String name, String owner, Email email, String phoneNumber, String businessType, String industry, String registrationNumber, String registrationCertificateUrl, String profileUrl, String bankbookCopy, AddressRegisterRequest address) {
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
        this.addresses.add(CompanyAddress.register(address));
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
                request.addressRegisterRequest()
        );
    }
}
