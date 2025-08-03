package hanieum.conik.domain.address;

import hanieum.conik.domain.address.dto.AddressRegisterRequest;
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
        this.addressPostalCode = request.addressPostalCode();
        this.addressStreetAddress = request.addressStreetAddress();
        this.addressDetailAddress = request.addressDetailAddress();
    }
}
