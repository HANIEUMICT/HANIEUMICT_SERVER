package hanieum.conik.application.company;

import hanieum.conik.application.company.provided.CompanyFinder;
import hanieum.conik.application.company.provided.EquipmentSaver;
import hanieum.conik.application.company.required.EquipmentRepository;
import hanieum.conik.domain.company.dto.EquipmentRequest;
import hanieum.conik.domain.company.dto.EquipmentUpdateRequest;
import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.company.entity.CompanyDetail;
import hanieum.conik.domain.company.entity.Equipment;
import hanieum.conik.domain.company.exception.CompanyErrorType;
import hanieum.conik.domain.company.exception.CompanyException;
import hanieum.conik.global.apiPayload.exception.GlobalErrorType;
import hanieum.conik.global.apiPayload.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentModifyService implements EquipmentSaver {
    private final CompanyFinder companyFinder;
    private final EquipmentRepository equipmentRepository;

    @Override
    public void add(Long companyId, List<EquipmentRequest> equipmentRequests) {
        Company company = companyFinder.findCompany(companyId);

        CompanyDetail detail = company.getCompanyDetail();
        if (detail == null) {
            throw new CompanyException(CompanyErrorType.COMPANY_DETAIL_NOT_FOUND);
        }

        if (equipmentRequests == null || equipmentRequests.isEmpty()) {
            return;
        }

        List<Equipment> list = equipmentRequests.stream()
                .map(Equipment::create)
                .toList();

        list.forEach(detail::addEquipment);

        equipmentRepository.saveAllAndFlush(list);
    }

    @Override
    public void delete(Long companyId, List<Long> equipmentIds) {
        Company company = companyFinder.findCompany(companyId);
        CompanyDetail detail = company.getCompanyDetail();
        if (detail == null) {
            throw new CompanyException(CompanyErrorType.COMPANY_DETAIL_NOT_FOUND);
        }

        List<Long> distinctIds = (equipmentIds == null) ? List.of()
                : equipmentIds.stream().filter(java.util.Objects::nonNull).distinct().toList();
        long deletedCount = equipmentRepository.deleteByCompanyDetailIdAndIdIn(detail.getId(), distinctIds);
        if (deletedCount != distinctIds.size()) {
            throw new CompanyException(CompanyErrorType.EQUIPMENT_NOT_FOUND);
        }
    }

    @Override
    public void sync(CompanyDetail detail, List<EquipmentUpdateRequest> requested) {
        updateAddKeepDelete(
                detail.getEquipments(),
                requested,
                Equipment::getId,
                EquipmentUpdateRequest::id,
                this::newEquipmentFrom,
                detail::addEquipment,
                detail::removeEquipment
        );
    }


    private Equipment newEquipmentFrom(EquipmentUpdateRequest request) {
        return Equipment.create(EquipmentRequest.fromEquipmentUpdateRequest(request));
    }

    private <E, U> void updateAddKeepDelete(
            List<E> current,
            List<U> requested,
            Function<E, Long> currentIdFn,
            Function<U, Long> requestedIdFn,
            Function<U, E> creator,
            Consumer<E> adder,
            Consumer<E> remover
    ) {
        if (requested == null) requested = List.of();

        Set<Long> seen = new HashSet<>();
        for (U u : requested) {
            Long rid = requestedIdFn.apply(u);
            if (rid != null && !seen.add(rid)) {
                throw new GlobalException(GlobalErrorType.DUPLICATED_CHILD_ID);
            }
        }

        Map<Long, E> byId = current.stream()
                .filter(e -> currentIdFn.apply(e) != null)
                .collect(Collectors.toMap(currentIdFn, Function.identity()));

        Set<Long> keep = new HashSet<>();

        for (U u : requested) {
            Long rid = requestedIdFn.apply(u);
            if (rid == null) {
                E created = creator.apply(u);
                adder.accept(created);
            } else {
                E exist = byId.get(rid);
                if (exist == null) {
                    throw new CompanyException(CompanyErrorType.EQUIPMENT_NOT_FOUND);
                }
                keep.add(rid);
            }
        }

        List<E> toRemove = current.stream()
                .filter(e -> {
                    Long id = currentIdFn.apply(e);
                    return id != null && !keep.contains(id);
                })
                .toList();
        toRemove.forEach(remover);
    }
}
