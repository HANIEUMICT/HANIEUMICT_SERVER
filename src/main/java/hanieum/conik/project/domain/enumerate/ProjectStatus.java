package hanieum.conik.project.domain.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectStatus {
    PUBLIC("공개"),
    PROTECTED("보호"),
    PRIVATE("비공개");

    private final String description;
}
