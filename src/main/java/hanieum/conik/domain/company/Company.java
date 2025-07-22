package hanieum.conik.domain.company;

import hanieum.conik.domain.company.enumerate.CompanyStatus;
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
    private String ownerEmail;

    @Column(nullable = false)
    private String ownerPhoneNumber;

    @Column(nullable = false)
    private String businessType;

    @Column(nullable = false)
    private String industry;

    @Column(nullable = false)
    private String registrationNumber;   // camelCase

    @Column(nullable = false)
    private String registrationCertificateUrl;

    @Column(nullable = false)
    private String bankAccountUrl;

    @Column(nullable = false)
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyStatus status;
}
