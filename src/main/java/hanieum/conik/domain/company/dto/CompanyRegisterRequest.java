package hanieum.conik.domain.company.dto;

import hanieum.conik.domain.address.dto.AddressRegisterRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CompanyRegisterRequest(
    @NotBlank(message = "기업 이름은 필수 입력입니다.")
    String name,

    @NotBlank(message = "기업 대표 이름은 필수 입력입니다.")
    String owner,

    @Email
    @NotBlank(message = "기업 이메일은 필수 입력입니다.")
    String email,

    @NotBlank(message = "기업 전화번호는 필수 입력입니다.")
    String phoneNumber,

    @NotBlank(message = "기업 업태명은 필수 입력입니다.")
    String businessType,

    @NotBlank(message = "기업 종목은 필수 입력입니다.")
    String industry,

    @NotBlank(message = "사업자등록 번호는 필수 입력입니다.")
    String registrationNumber,

    @NotNull(message = "사업자등록증 파일을 첨부해주세요.")
    String registrationCertificateUrl,

    @NotNull(message = "통장 사본 파일을 첨부해주세요.")
    String bankbookCopy,

    @NotNull(message = "회사 소개서 파일을 첨부해주세요.")
    String profileUrl,

    @NotNull(message = "주소는 필수 입력입니다.")
    AddressRegisterRequest addressRegisterRequest
) {}
