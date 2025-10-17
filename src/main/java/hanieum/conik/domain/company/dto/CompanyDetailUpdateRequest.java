package hanieum.conik.domain.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CompanyDetailUpdateRequest(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        CompanyDetailRequest detail,

        @Schema(
                description = "보유 장비 목록",
                example = """
        [
          {"name": "마이데이 응원봉","description": "알루미늄 가공","quantity": 2,"imageUrls": ["https://cdn.example.com/equip/cnc-1.jpg", "https://cdn.example.com/equip/cnc-2.jpg"]},
          {"name": "밀링 머신","description": "3축 밀링","quantity": 1, "imageUrls": ["https://cdn.example.com/equip/mill-1.jpg"]}
        ]
        """
        )
        @Valid @NotNull
        List<EquipmentUpdateRequest> equipments,



        @Schema(
                description = "포트폴리오 목록",
                example = """
            [
              {"quantity": 100, "description": "데이식스 굿즈", "imageUrls": ["https://cdn.example.com/pf/bracket-1.jpg", "https://cdn.example.com/pf/bracket-2.jpg"], "category": "가공"},
              {"quantity": 50,"description": "하우징 제작","imageUrls": ["https://cdn.example.com/pf/housing-1.jpg"], "category": "제작"}
            ]
        """
        ) @Valid @NotNull
        List<PortfolioUpdateRequest> portfolios
) {}
