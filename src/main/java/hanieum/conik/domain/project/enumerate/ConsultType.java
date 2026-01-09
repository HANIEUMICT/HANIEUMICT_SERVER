package hanieum.conik.domain.project.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConsultType {
    BID_ONLY("견적서를 보낸 공장만 상담 가능"),
    ALL_FACTORY("모든 공장이 상담 가능");

    private final String description;
}
