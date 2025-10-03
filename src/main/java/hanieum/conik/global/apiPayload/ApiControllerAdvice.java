package hanieum.conik.global.apiPayload;

import hanieum.conik.global.apiPayload.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import hanieum.conik.global.apiPayload.exception.GlobalErrorType;
import hanieum.conik.global.apiPayload.response.ApiResponse;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("Exception : {}", e.getMessage(), e);
        return new ResponseEntity<>(ApiResponse.error(GlobalErrorType.INTERNAL_ERROR), GlobalErrorType.INTERNAL_ERROR.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException : {}", e.getMessage(), e);

        var fieldError = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .orElse(null);

        GlobalErrorType type = GlobalErrorType.FAILED_REQUEST_VALIDATION;

        if (fieldError != null) {
            String key = fieldError.getField() + ":" + fieldError.getCode();
            type = VALIDATION_MAP.getOrDefault(key, GlobalErrorType.FAILED_REQUEST_VALIDATION);
        }

        return new ResponseEntity<>(ApiResponse.error(type), type.getStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException : {}", e.getMessage(), e);
        return new ResponseEntity<>(ApiResponse.error(GlobalErrorType.INVALID_REQUEST_ARGUMENT), GlobalErrorType.INVALID_REQUEST_ARGUMENT.getStatus());
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(GlobalException e) {
        log.error("CoreException : {}", e.getMessage(), e);
        return new ResponseEntity<>(ApiResponse.error(e.getErrorType()), e.getErrorType().getStatus());
    }

    private static final Map<String, GlobalErrorType> VALIDATION_MAP = Map.of(
            "email:NotBlank", GlobalErrorType.EMAIL_REQUIRED,
            "email:Email",    GlobalErrorType.EMAIL_INVALID_FORMAT
    );
}