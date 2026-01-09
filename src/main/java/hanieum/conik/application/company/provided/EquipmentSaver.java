package hanieum.conik.application.company.provided;

import hanieum.conik.domain.company.dto.EquipmentRequest;
import hanieum.conik.domain.company.dto.EquipmentUpdateRequest;
import hanieum.conik.domain.company.entity.CompanyDetail;

import java.util.List;

public interface EquipmentSaver {
    void add(Long companyId, List<EquipmentRequest> equipmentRequests);

    void delete(Long companyId, List<Long> equipmentIds);

    void sync(CompanyDetail detail, List<EquipmentUpdateRequest> requested);
}
