package hanieum.conik.domain.project.exception;

import hanieum.conik.global.apiPayload.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProjectErrorType implements ErrorType {
    PROJECT_DRAWING_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "프로젝트 도면 파일 저장 중 오류가 발생했습니다."),
    PROJECT_INITIATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "프로젝트 초기화 중 오류가 발생했습니다."),
    PROJECT_DRAFT_SAVE_ERROR(HttpStatus.BAD_REQUEST, "프로젝트 초안 저장 중 오류가 발생했습니다."),
    PROJECT_FINAL_SAVE_ERROR(HttpStatus.BAD_REQUEST, "최종 프로젝트 저장 중 오류가 발생했습니다."),
    PROJECT_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "프로젝트 저장 중 오류가 발생했습니다."),
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."),
    ;

    private final HttpStatus status;

    private final String message;
}
