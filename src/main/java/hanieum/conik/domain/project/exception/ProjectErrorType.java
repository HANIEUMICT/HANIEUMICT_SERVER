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
    PROJECT_BID_STATUS_UPDATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "프로젝트 입찰 상태 업데이트 중 오류가 발생했습니다."),
    PROJECT_EXPIRED(HttpStatus.BAD_REQUEST, "입찰 가능 기한이 지났습니다."),
    PROJECT_ALREADY_IN_PROGRESS(HttpStatus.BAD_REQUEST, "거래 중인 프로젝트는 삭제할 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    PROJECT_ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "프로젝트의 주소를 찾을 수 없습니다.")
    ;

    private final HttpStatus status;

    private final String message;
}
