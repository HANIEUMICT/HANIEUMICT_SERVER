package hanieum.conik.domain.common.address;

import hanieum.conik.domain.common.address.dto.AddressRegisterRequest;
import hanieum.conik.domain.common.address.exception.AddressErrorType;
import hanieum.conik.domain.common.address.exception.AddressException;
import hanieum.conik.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressBase extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String streetAddress;

    @Column(nullable = false)
    private String detailAddress;

    protected AddressBase(String postalCode, String streetAddress, String detailAddress) {
        this.postalCode = postalCode;
        this.streetAddress = streetAddress;
        this.detailAddress = detailAddress;
    }

    /**
     * 주소 정보를 업데이트 한다.
     */
    public void update(AddressRegisterRequest request) {
        if (request.postalCode() == null || request.postalCode().isBlank()) {
            throw new AddressException(AddressErrorType.INVALID_POSTAL_CODE);
        }
        if (request.streetAddress() == null || request.streetAddress().isBlank()) {
            throw new AddressException(AddressErrorType.INVALID_STREET_ADDRESS);
        }
        if (request.detailAddress() == null || request.detailAddress().isBlank()) {
            throw new AddressException(AddressErrorType.INVALID_DETAIL_ADDRESS);
        }
        this.postalCode = request.postalCode();
        this.streetAddress = request.streetAddress();
        this.detailAddress = request.detailAddress();
    }
}
