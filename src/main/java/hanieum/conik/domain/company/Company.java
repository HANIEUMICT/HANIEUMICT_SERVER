package hanieum.conik.domain.company;

import hanieum.conik.domain.company.dto.CompanyRegisterRequest;
import hanieum.conik.domain.company.enumerate.CompanyStatus;
import hanieum.conik.domain.member.shared.Email;
import hanieum.conik.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false)
    private Email email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String businessType;

    @Column(nullable = false)
    private String industry;

    @Column(nullable = false)
    private String registrationNumber;

    @Column(nullable = false, length = 2048)
    private String registrationCertificateUrl;

    @Column(nullable = false, length = 2048)
    private String bankbookCopy;

    @Column(nullable = false, length = 2048)
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyStatus status;

    private Company(String name, String owner, Email email, String phoneNumber, String businessType, String industry, String registrationNumber, String registrationCertificateUrl, String profileUrl, String bankbookCopy) {
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
    }

    /**
     * 기업 등록
     * */
    public static Company register(String name, String owner, Email email, String phoneNumber, String businessType, String industry, String registrationNumber, String registrationCertificateUrl, String profileUrl, String bankbookCopy) {
        return new Company(
                name,
                owner,
                email,
                phoneNumber,
                businessType,
                industry,
                registrationNumber,
                registrationCertificateUrl,
                profileUrl,
                bankbookCopy
        );
    }
}
