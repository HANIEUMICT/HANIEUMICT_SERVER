package hanieum.conik.adapter.company.persistence;

import hanieum.conik.domain.company.CompanyStatus;
import hanieum.conik.global.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "company")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name", nullable = false)
    private String name;

    @Column(name = "company_owner", nullable = false)
    private String owner;

    @Column(name = "company_owner_email", nullable = false)
    private String ownerEmail;

    @Column(name = "company_owner_phone", nullable = false)
    private String ownerPhoneNumber;

    @Column(name = "company_business_type", nullable = false)
    private String businessType;

    @Column(name = "company_industry", nullable = false)
    private String industry;

    @Column(name = "company_registration_number", nullable = false)
    private String registrationNumber;   // camelCase

    @Column(name = "company_registration_certificate_url", nullable = false)
    private String registrationCertificateUrl;

    @Column(name = "company_bank_account_url", nullable = false)
    private String bankAccountUrl;

    @Column(name = "company_profile_url", nullable = false)
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_status", nullable = false)
    private CompanyStatus status;
}
