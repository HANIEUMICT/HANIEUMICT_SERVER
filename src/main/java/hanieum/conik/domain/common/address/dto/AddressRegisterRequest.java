package hanieum.conik.domain.common.address.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AddressRegisterRequest(
        @Schema(description = "우편번호", example = "12345")
        @NotBlank(message = "우편번호는 필수 입력입니다.")
        String addressPostalCode,

        @Schema(description = "도로명 주소", example = "경기도 수원시 행복구 행복동")
        @NotBlank(message = "도로명 주소는 필수 입력입니다.")
        String addressStreetAddress,

        @Schema(description = "상세 주소", example = "행복호")
        @NotBlank(message = "상세 주소는 필수 입력입니다.")
        String addressDetailAddress
) {}
