package hanieum.conik.domain.project.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubmitStatus {
    INITIALIZE("템플릿 생성됨"),
    TEMPORARY_SAVE("임시 저장됨"),
    SUBMIT("작성 완료됨"),
    ;
    private final String description;
}