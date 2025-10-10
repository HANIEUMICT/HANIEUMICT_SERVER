package hanieum.conik.domain.company.entity;

import hanieum.conik.domain.company.dto.PortfolioRequest;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Portfolio extends AbstractEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private Integer quantity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 1024)
    private List<String> imageUrls;

    private String category;

    private Portfolio(Integer quantity, String description, List<String> imageUrls, String category){
        this.quantity = (quantity == null ? 0 : Math.max(0, quantity));
        this.description = description;
        this.imageUrls = imageUrls;
        this.category = category;
    }

    public static Portfolio create(PortfolioRequest request){
        Integer quantity = request.quantity();
        return new Portfolio(
                (quantity == null ? 0 : Math.max(0, quantity)),
                request.description(),
                request.imageUrls(),
                request.category()
        );
    }

    void setCompanyDetail(CompanyDetail detail) { this.company.attachDetail(detail);}

    public void update(PortfolioRequest request) {
        if (request.quantity() != null) this.quantity = Math.max(0, request.quantity());
        this.description = request.description();
        this.imageUrls = request.imageUrls();
        this.category = request.category();
    }

    public void remove() {
        if (this.company.getCompanyDetail() != null) this.company.getCompanyDetail().removePortfolio(this.getId());
    }
}
