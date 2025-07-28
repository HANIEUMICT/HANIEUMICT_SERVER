package hanieum.conik.domain.address;

import hanieum.conik.domain.address.dto.AddressRegisterRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

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

    public static Address register(AddressRegisterRequest request) {
        return new Address(request.addressPostalCode(), request.addressStreetAddress(), request.addressDetailAddress());
    }

    public void update(AddressRegisterRequest request) {
        this.addressPostalCode = request.addressPostalCode();
        this.addressStreetAddress = request.addressStreetAddress();
        this.addressDetailAddress = request.addressDetailAddress();
    }
}
