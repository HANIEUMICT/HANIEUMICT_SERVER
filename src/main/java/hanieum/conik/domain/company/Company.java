package hanieum.conik.domain.company;

import hanieum.conik.domain.address.AddressBase;
import hanieum.conik.domain.address.CompanyAddress;
import hanieum.conik.domain.address.dto.AddressRegisterRequest;
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
    private String name;

    @Column(nullable = false)
    private String owner;

    @Embedded
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
