package hanieum.conik.domain.project.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProgressStatus {
    BEFORE("진행 전"),
    IN_PROGRESS("진행 중"),
    COMPLETED("진행 완료");

    private final String description;
}
