package hanieum.conik.domain.project.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectStatus {
    PUBLIC("공개"),
    PROTECTED("선택적공개"),
    PRIVATE("비공개"),;

    private final String description;
}
