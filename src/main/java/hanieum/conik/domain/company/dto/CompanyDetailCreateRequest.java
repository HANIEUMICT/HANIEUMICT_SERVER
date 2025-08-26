package hanieum.conik.domain.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

import java.util.List;

public record CompanyDetailCreateRequest(
        @Valid
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        CompanyDetailRequest detail,                // 기존 세부정보 DTO

        @Schema(description = "보유 장비 목록(없으면 생략 또는 빈 배열)", example = """
                  [
                    {"name":"CNC 선반","description":"알루미늄 가공","quantity":2,"imageUrl":"https://cdn.example.com/equip/cnc-1.jpg"},
                    {"name":"밀링 머신","description":"3축 밀링","quantity":1,"imageUrl":"https://cdn.example.com/equip/mill-1.jpg"}
                  ]
                """)
        @Valid
        List<EquipmentRequest> equipments,


        @Schema(description = "포트폴리오 목록(없으면 생략 또는 빈 배열)", example = """
                  [
                    {"quantity":100,"description":"브라켓 가공","imageUrl":"https://cdn.example.com/pf/bracket.jpg","category":"가공"},
                    {"quantity":50,"description":"하우징 제작","imageUrl":"https://cdn.example.com/pf/housing.jpg","category":"제작"}
                  ]
                """)
        @Valid List<PortfolioRequest> portfolios
) {}
