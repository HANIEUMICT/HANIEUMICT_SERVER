package hanieum.conik.domain.company.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record EquipmentRequest(
        @NotBlank String name,
        String description,
        @Min(0) Integer quantity,
        String imageUrl
) {}
