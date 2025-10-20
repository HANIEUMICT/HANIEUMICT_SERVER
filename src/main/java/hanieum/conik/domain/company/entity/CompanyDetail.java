package hanieum.conik.domain.company.entity;

import hanieum.conik.domain.company.dto.CompanyDetailRequest;
import hanieum.conik.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyDetail extends BaseEntity {
    @Id
    @Column(name = "company_id")   // PK=FK
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "company_id")
    private Company company;

    private LocalDate establishedAt;

    @Column(nullable = false, length = 2048)
    private String logoUrl; // 회사 로고 URL

    private Integer employeeCount;

    @Column(length = 1024)
    private String websiteUrl;

    @Column(length = 100)
    private String contactAvailableTime;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer rating;

    @Column(nullable = false)
    private Integer totalOrderCount = 0;

    @Column(nullable = false)
    private Integer repeatOrderCount = 0;

    private Integer avgProductionLeadHours;

    private Integer avgResponseMinutes;

    @OneToMany(mappedBy = "companyDetail", cascade = ALL, orphanRemoval = true)
    private List<Equipment> equipments = new ArrayList<>();

    @OneToMany(mappedBy = "companyDetail", cascade = ALL, orphanRemoval = true)
    private List<Portfolio> portfolios = new ArrayList<>();

    private CompanyDetail(
            Company company,
            LocalDate establishedAt,
            String logoUrl,
            Integer employeeCount,
            String websiteUrl,
            String contactAvailableTime,
            String description) {
        Objects.requireNonNull(company, "company must not be null");
        Objects.requireNonNull(establishedAt, "establishedAt must not be null");
        Objects.requireNonNull(logoUrl, "logoUrl must not be null");

        this.establishedAt = establishedAt;
        this.logoUrl = logoUrl;
        this.employeeCount = employeeCount;
        this.websiteUrl = websiteUrl;
        this.contactAvailableTime = contactAvailableTime;
        this.description = description;

        setCompany(company);
    }

    /**
     * 기업 상세 정보를 생성한다.
     */
    public static CompanyDetail create(Company company,
                                       CompanyDetailRequest request,
                                       List<Equipment> equipments,
                                       List<Portfolio> portfolios) {

        CompanyDetail detail = new CompanyDetail(company, request.establishedAt(), request.logoUrl(), request.employeeCount(), request.websiteUrl(), request.contactAvailableTime(), request.description());

        company.attachDetail(detail);

        if (equipments != null) {equipments.forEach(detail::addEquipment);}
        if (portfolios != null) {portfolios.forEach(detail::addPortfolio);}

        return detail;
    }

    /**
     * 기업을 지정한다.
     */
    void setCompany(Company company) {
        this.company = company;
    }

    /**
     * 장비를 추가한다.
     */
    public void addEquipment(Equipment equipment) {
        equipments.add(equipment);
        equipment.setCompanyDetail(this);
    }

    /**
     * 포트폴리오를 추가한다.
     */
    public void addPortfolio(Portfolio portfolio) {
        portfolios.add(portfolio);
        portfolio.setCompanyDetail(this);
    }

    /**
     * 장비를 제거한다.
     */
    public void removeEquipment(Equipment equipment) {
        if (equipment == null) return;
        equipments.remove(equipment);
        equipment.setCompanyDetail(null);
    }

    /**
     * 포트폴리오를 제거한다.
     */
    public void removePortfolio(Portfolio portfolio) {
        if (portfolio == null) return;
        portfolios.remove(portfolio);
        portfolio.setCompanyDetail(null);
    }

    public void update(CompanyDetailRequest detail) {
        if (detail == null) return;
        if (detail.establishedAt() != null) this.establishedAt = detail.establishedAt();
        if (detail.logoUrl() != null) this.logoUrl = detail.logoUrl();
        if (detail.employeeCount() != null) this.employeeCount = detail.employeeCount();
        if (detail.websiteUrl() != null) this.websiteUrl = detail.websiteUrl();
        if (detail.contactAvailableTime() != null) this.contactAvailableTime = detail.contactAvailableTime();
        if (detail.description() != null) this.description = detail.description();
    }
}

