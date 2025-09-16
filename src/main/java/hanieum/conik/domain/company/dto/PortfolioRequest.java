package hanieum.conik.domain.company.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PortfolioRequest(
        @Min(0) Integer quantity,
        String description,
        String imageUrl,
        @NotBlank String category
) {}
