package hanieum.conik.application.company.required;

import hanieum.conik.domain.company.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Optional<Equipment> findByIdAndCompanyDetailId(Long equipmentId, Long companyDetailId);
    List<Equipment> findByCompanyDetailId(Long companyId);
    long deleteByCompanyDetailIdAndIdIn(Long companyId, List<Long> equipmentIds);
}
