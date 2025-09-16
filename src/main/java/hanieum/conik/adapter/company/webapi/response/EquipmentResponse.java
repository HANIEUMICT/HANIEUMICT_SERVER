package hanieum.conik.adapter.company.webapi.response;

import hanieum.conik.domain.company.entity.Equipment;

public record EquipmentResponse(
        Long id,
        String name,
        Integer quantity,
        String description,
        String imageUrl
) {
    public static EquipmentResponse from(Equipment e) {
        return new EquipmentResponse(
                e.getId(),
                e.getName(),
                e.getQuantity(),
                e.getDescription(),
                e.getImageUrl()
        );
    }
}