package hanieum.conik.domain.deal.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {
    PREPARING("배송 준비 중"),
    IN_DELIVERY("배송 중"),
    DELIVERED("배송 완료");

    private final String description;
}
