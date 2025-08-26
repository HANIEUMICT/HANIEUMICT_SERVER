package hanieum.conik.adapter.company.webapi.response;

import hanieum.conik.domain.company.entity.Portfolio;

public record PortfolioResponse(
        Long id,
        String category,
        Integer quantity,
        String description,
        String imageUrl
) {
    public static PortfolioResponse from(Portfolio p) {
        return new PortfolioResponse(
                p.getId(),
                p.getCategory(),
                p.getQuantity(),
                p.getDescription(),
                p.getImageUrl()
        );
    }
}
