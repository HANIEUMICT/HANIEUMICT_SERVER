package hanieum.conik.global.adapter.security;


import hanieum.conik.global.domain.exception.AuthErrorType;
import hanieum.conik.global.domain.exception.AuthException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class AuthorizeUserAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before("@annotation(authorizeUser)")
    public void authorizeUser(JoinPoint joinPoint, AuthorizeUser authorizeUser) {
        // AuthDetails 찾기
        AuthDetails authDetails = findAuthDetails(joinPoint);
        if (authDetails == null || authorizeUser.sourceType() == AuthSourceType.NONE) {
            return;
        }

        Long authMemberId = authDetails.getMemberId();
        Long targetMemberId = null;

        switch (authorizeUser.sourceType()) {
            case PATH_VARIABLE:
                targetMemberId = extractFromPathVariable(joinPoint, authorizeUser.paramName());
                break;
            case REQUEST_PARAM:
                targetMemberId = extractFromRequestParam(joinPoint, authorizeUser.paramName());
                break;
            case REQUEST_BODY:
                targetMemberId = extractFromRequestBody(joinPoint, authorizeUser.fieldName());
                break;
        }

        if (targetMemberId != null && !authMemberId.equals(targetMemberId)) {
            throw new AuthException(AuthErrorType.UNAUTHORIZED_MEMBER_ACCESS);
        }
    }

    /**
     * AuthDetails 객체를 찾아서 반환합니다.
     */
    private AuthDetails findAuthDetails(JoinPoint joinPoint) {
        // 1. 메서드 인자에서 직접 찾기
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof AuthDetails) {
                return (AuthDetails) arg;
            }
        }

        // 2. SecurityContextHolder에서 찾기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthDetails) {
            return (AuthDetails) authentication.getPrincipal();
        }

        return null;
    }

    /**
     * PathVariable에서 memberId 추출
     */
    private Long extractFromPathVariable(JoinPoint joinPoint, String paramName) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameters.length; i++) {
            PathVariable pathVariable = parameters[i].getAnnotation(PathVariable.class);
            if (pathVariable != null) {
                String name = pathVariable.value().isEmpty() ? parameters[i].getName() : pathVariable.value();
                if (name.equals(paramName) && args[i] instanceof Long) {
                    return (Long) args[i];
                }
            }
        }

        return null;
    }

    /**
     * RequestParam에서 memberId 추출
     */
    private Long extractFromRequestParam(JoinPoint joinPoint, String paramName) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameters.length; i++) {
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                String name = requestParam.value().isEmpty() ? parameters[i].getName() : requestParam.value();
                if (name.equals(paramName) && args[i] instanceof Long) {
                    return (Long) args[i];
                }
            }
        }

        return null;
    }

    /**
     * RequestBody에서 memberId 필드 추출
     */
    private Long extractFromRequestBody(JoinPoint joinPoint, String fieldName) {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg != null && !isSimpleType(arg.getClass())) {
                try {
                    // 필드 접근 시도
                    java.lang.reflect.Field field = findField(arg.getClass(), fieldName);
                    if (field != null) {
                        field.setAccessible(true);
                        Object value = field.get(arg);
                        if (value instanceof Long) {
                            return (Long) value;
                        } else if (value instanceof Number) {
                            return ((Number) value).longValue();
                        } else if (value instanceof String) {
                            return Long.parseLong((String) value);
                        }
                    }
                } catch (Exception e) {
                    // 필드 접근 실패시 무시
                }
            }
        }

        return null;
    }

    private java.lang.reflect.Field findField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return findField(superClass, fieldName);
            }
            return null;
        }
    }

    private boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz.equals(String.class) ||
                Number.class.isAssignableFrom(clazz) ||
                Boolean.class.equals(clazz);
    }
}