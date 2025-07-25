package hanieum.conik.domain.project.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileStatus {
    TEMPORARY("임시 저장"),
    FINALIZED("최종 저장");

    private final String description;
}
