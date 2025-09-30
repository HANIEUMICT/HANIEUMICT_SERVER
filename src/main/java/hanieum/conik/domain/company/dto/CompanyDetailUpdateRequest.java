package hanieum.conik.domain.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record CompanyDetailUpdateRequest(
        @Schema(description = "설립일", example = "2020-01-01")
        @PastOrPresent
        LocalDate establishedAt,

        @Schema(description = "회사 로고 URL", example = "https://cdn.example.com/logo.png")
        @Size(max = 2048)
        String logoUrl,

        @Schema(description = "임직원 수", example = "42")
        @PositiveOrZero
        Integer employeeCount,

        @Schema(description = "웹사이트 URL", example = "https://www.example.com")
        @Size(max = 2048)
        String websiteUrl,

        @Schema(description = "문의 가능 시간(24h 범위)", example = "09:00-18:00")
        @Size(max = 128)
        @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", message = "문의 가능 시간은 HH:mm-HH:mm 형식이어야 합니다.")
        String contactAvailableTime,

        @Schema(description = "기업 소개", example = "우리는 ...")
        @Size(max = 6000)
        String description,

        @Schema(description = "보유 장비 목록(없으면 생략 또는 빈 배열)", example = """
                  [
                    {"name":"CNC 선반","description":"알루미늄 가공","quantity":2,"imageUrl":"https://cdn.example.com/equip/cnc-1.jpg"},
                    {"name":"밀링 머신","description":"3축 밀링","quantity":1,"imageUrl":"https://cdn.example.com/equip/mill-1.jpg"}
                  ]
                """)
        @Valid List<EquipmentRequest> equipments,


        @Schema(description = "포트폴리오 목록(없으면 생략 또는 빈 배열)", example = """
                  [
                    {"quantity":100,"description":"브라켓 가공","imageUrl":"https://cdn.example.com/pf/bracket.jpg","category":"가공"},
                    {"quantity":50,"description":"하우징 제작","imageUrl":"https://cdn.example.com/pf/housing.jpg","category":"제작"}
                  ]
                """)
        @Valid List<PortfolioRequest> portfolios
) {}
