package hanieum.conik.domain.common.address;

import hanieum.conik.domain.common.address.dto.AddressRegisterRequest;
import hanieum.conik.domain.common.address.exception.AddressErrorType;
import hanieum.conik.domain.common.address.exception.AddressException;
import jakarta.mail.Address;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String addressPostalCode;

    @Column(nullable = false)
    private String addressStreetAddress;

    @Column(nullable = false)
    private String addressDetailAddress;

    protected AddressBase(String addressPostalCode, String addressStreetAddress, String addressDetailAddress) {
        this.addressPostalCode = addressPostalCode;
        this.addressStreetAddress = addressStreetAddress;
        this.addressDetailAddress = addressDetailAddress;
    }

    /**
     * 주소 정보를 업데이트 한다.
     */
    public void update(AddressRegisterRequest request) {
        if (request.addressPostalCode() == null || request.addressPostalCode().isBlank()) {
            throw new AddressException(AddressErrorType.INVALID_POSTAL_CODE);
        }
        if (request.addressStreetAddress() == null || request.addressStreetAddress().isBlank()) {
            throw new AddressException(AddressErrorType.INVALID_STREET_ADDRESS);
        }
        if (request.addressDetailAddress() == null || request.addressDetailAddress().isBlank()) {
            throw new AddressException(AddressErrorType.INVALID_DETAIL_ADDRESS);
        }
        this.addressPostalCode = request.addressPostalCode();
        this.addressStreetAddress = request.addressStreetAddress();
        this.addressDetailAddress = request.addressDetailAddress();
    }
}
