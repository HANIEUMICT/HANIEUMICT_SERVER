package hanieum.conik.domain.address;

import hanieum.conik.domain.address.dto.AddressRegisterRequest;
import hanieum.conik.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String addressPostalCode;

    @Column(nullable = false)
    private String addressStreetAddress;

    @Column(nullable = false)
    private String addressDetailAddress;

    private Address(String addressPostalCode, String addressStreetAddress, String addressDetailAddress) {
        this.addressPostalCode = addressPostalCode;
        this.addressStreetAddress = addressStreetAddress;
        this.addressDetailAddress = addressDetailAddress;
    }

    /**
     * 주소를 등록한다
     */
    public static Address register(AddressRegisterRequest request) {
        return new Address(request.addressPostalCode(), request.addressStreetAddress(), request.addressDetailAddress());
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
