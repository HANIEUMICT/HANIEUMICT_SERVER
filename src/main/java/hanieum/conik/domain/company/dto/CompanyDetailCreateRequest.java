package hanieum.conik.domain.company.dto;

import jakarta.validation.Valid;

import java.util.List;

public record CompanyDetailCreateRequest(
        @Valid CompanyDetailRequest detail,                // 기존 세부정보 DTO
        @Valid List<EquipmentRequest> equipments,          // 선택
        @Valid List<PortfolioRequest> portfolios           // 선택
) {}
