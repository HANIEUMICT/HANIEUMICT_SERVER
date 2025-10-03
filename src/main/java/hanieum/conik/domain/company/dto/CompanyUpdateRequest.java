package hanieum.conik.domain.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

public record CompanyUpdateRequest(
        @Schema(description = "기업 이름", example = "행복 제조업")
        String name,

        @Schema(description = "기업 대표 이름", example = "윤도운")
        String owner,

        @Schema(description = "기업 이메일", example = "d.ddablue@naver.com")
        @Email
        String email,

        @Schema(description = "기업 전화번호", example = "010-1234-5678")
        String phoneNumber,

        @Schema(description = "기업 업태명", example = "데이식스")
        String businessType,

        @Schema(description = "기업 종목", example = "행복 전도 밴드")
        String industry,

        @Schema(description = "사업자등록 번호", example = "123-45-67890")
        String registrationNumber,

        @Schema(description = "사업자등록 파일")
        String registrationCertificateUrl,

        @Schema(description = "통장 사본 파일")
        String bankbookCopy,

        @Schema(description = "기업 정보 파일")
        String profileUrl
) {}
