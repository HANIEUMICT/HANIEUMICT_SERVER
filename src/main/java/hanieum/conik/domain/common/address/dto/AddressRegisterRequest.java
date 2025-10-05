package hanieum.conik.domain.common.address.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AddressRegisterRequest(
        @Schema(description = "배송지명", example = "우리집")
        @NotBlank(message = "배송지명은 필수 입력입니다.")
        String addressName,

        @Schema(description = "수령인", example = "홍길동")
        @NotBlank(message = "수령인은 필수 입력입니다.")
        String recipient,

        @Schema(description = "전화번호", example = "010-1234-5678")
        @NotBlank(message = "전화번호는 필수 입력입니다.")
        String phoneNumber,

        @Schema(description = "우편번호", example = "12345")
        @NotBlank(message = "우편번호는 필수 입력입니다.")
        String postalCode,

        @Schema(description = "도로명 주소", example = "경기도 수원시 행복구 행복동")
        @NotBlank(message = "도로명 주소는 필수 입력입니다.")
        String streetAddress,

        @Schema(description = "상세 주소", example = "행복호")
        @NotBlank(message = "상세 주소는 필수 입력입니다.")
        String detailAddress,

        @Schema(description = "기본 배송지 여부", example = "false")
        @JsonProperty("default")
        @NotNull
        boolean isDefault
) {}
