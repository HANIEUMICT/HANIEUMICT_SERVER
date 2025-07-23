package hanieum.conik.domain.member.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    OWNER("기업 대표"),
    STAFF("기업 근로자"),
    INDIVIDUAL("소상공인");

    private final String description;
}
