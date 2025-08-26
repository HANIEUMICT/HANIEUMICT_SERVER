package hanieum.conik.domain.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CompanyDetailRequest(
    @Schema(description = "설립일", example = "2020-01-01")
    LocalDate establishedAt,

    @Schema(description = "회사 로고 URL", example = "https://cdn.example.com/logo.png")
    @NotBlank
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
    String description
) {}
