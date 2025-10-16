package hanieum.conik.application.company.provided;

import hanieum.conik.domain.company.dto.EquipmentRequest;

public interface EquipmentSaver {
    Long add(Long companyDetailId, EquipmentRequest request);

    void delete(Long companyDetailId, Long equipmentId);
}
