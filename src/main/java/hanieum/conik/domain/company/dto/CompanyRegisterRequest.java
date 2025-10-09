package hanieum.conik.domain.company.dto;

import hanieum.conik.domain.common.address.dto.AddressRegisterRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CompanyRegisterRequest(
    @Schema(description = "기업 이름", example = "행복 제조업")
    @NotBlank(message = "기업 이름은 필수 입력입니다.")
    String name,

    @Schema(description = "기업 대표 이름", example = "윤도운")
    @NotBlank(message = "기업 대표 이름은 필수 입력입니다.")
    String owner,

    @Schema(description = "기업 이메일", example = "d.ddablue@naver.com")
    @Email
    @NotBlank(message = "기업 이메일은 필수 입력입니다.")
    String email,

    @Schema(description = "기업 전화번호", example = "010-1234-5678")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
    @NotBlank(message = "기업 전화번호는 필수 입력입니다.")
    String phoneNumber,

    @Schema(description = "기업 업태명", example = "데이식스")
    @NotBlank(message = "기업 업태명은 필수 입력입니다.")
    String businessType,

    @Schema(description = "기업 종목", example = "행복 전도 밴드")
    @NotBlank(message = "기업 종목은 필수 입력입니다.")
    String industry,

    @Schema(description = "사업자등록 번호", example = "123-45-67890")
    @NotBlank(message = "사업자등록 번호는 필수 입력입니다.")
    String registrationNumber,

    @NotNull(message = "사업자등록증 파일을 첨부해주세요.")
    String registrationCertificateUrl,

    @NotNull(message = "통장 사본 파일을 첨부해주세요.")
    String bankbookCopy,

    @NotNull(message = "회사 소개서 파일을 첨부해주세요.")
    String profileUrl,

    @NotNull(message = "주소는 필수 입력입니다.")
    @Valid
    AddressRegisterRequest addressRegisterRequest
) {}
