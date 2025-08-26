package hanieum.conik.domain.company.entity;

import hanieum.conik.domain.company.dto.PortfolioRequest;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Portfolio extends AbstractEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyDetail companyDetail;

    @Column(nullable = false)
    private Integer quantity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 1024)
    private String imageUrl;

    private String category;

    private Portfolio(Integer quantity, String description, String imageUrl, String category){
        this.quantity = quantity;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public static Portfolio create(PortfolioRequest request){
        return new Portfolio(request.quantity(), request.description(), request.imageUrl(), request.category());
    }

    void setCompanyDetail(CompanyDetail detail) { this.companyDetail = detail; }

    public void update(PortfolioRequest request) {
        if (request.quantity() != null) this.quantity = Math.max(0, request.quantity());
        this.description = request.description();
        this.imageUrl = request.imageUrl();
        this.category = request.category();
    }

    public void remove() {
        if (this.companyDetail != null) this.companyDetail.removePortfolio(this.getId());
    }
}
