package hanieum.conik.domain.company.entity;

import hanieum.conik.domain.company.dto.EquipmentRequest;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Equipment extends AbstractEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyDetail companyDetail;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, length = 512)
    private String imageUrl;

    private Equipment(String name, String description, Integer quantity, String imageUrl) {
        this.name = name;
        this.description = description;
        this.quantity = (quantity == null ? 0 : Math.max(0, quantity));
        this.imageUrl = imageUrl;
    }

    public static Equipment create(EquipmentRequest request) {
        return new Equipment(request.name(), request.description(), request.quantity(), request.imageUrl());
    }

    void setCompanyDetail(CompanyDetail detail) { this.companyDetail = detail; }

    public void update(EquipmentRequest request) {
        if (request.name() != null && !request.name().isBlank()) this.name = request.name().trim();
        this.description = request.description();
        if (request.quantity() != null) this.quantity = Math.max(0, request.quantity());
        this.imageUrl = request.imageUrl();
    }

    public void remove() {
        if (this.companyDetail != null) this.companyDetail.removeEquipment(this.getId());
    }
}
