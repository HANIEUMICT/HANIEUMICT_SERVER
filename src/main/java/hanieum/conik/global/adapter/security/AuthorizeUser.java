package hanieum.conik.global.adapter.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizeUser {
    // 어떤 방식으로 사용자 ID를 받는지 지정
    AuthSourceType sourceType() default AuthSourceType.PATH_VARIABLE;

    // 파라미터 이름 (PathVariable, RequestParam의 경우)
    String paramName() default "memberId";

    // RequestBody에서 사용자 ID를 추출하는 경우의 필드 이름
    String fieldName() default "memberId";
}