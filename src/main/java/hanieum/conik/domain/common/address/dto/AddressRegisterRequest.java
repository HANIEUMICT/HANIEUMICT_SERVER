package hanieum.conik.domain.common.address.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressRegisterRequest(
        @NotBlank(message = "우편번호는 필수 입력입니다.")
        String addressPostalCode,

        @NotBlank(message = "도로명 주소는 필수 입력입니다.")
        String addressStreetAddress,

        @NotBlank(message = "상세 주소는 필수 입력입니다.")
        String addressDetailAddress
) {}
