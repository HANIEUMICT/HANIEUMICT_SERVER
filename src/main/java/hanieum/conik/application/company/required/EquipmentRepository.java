package hanieum.conik.application.company.required;

import hanieum.conik.domain.company.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByCompanyDetailId(Long companyId);
}
