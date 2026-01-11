package hanieum.conik.adapter.notification.webapi;

import hanieum.conik.adapter.notification.dto.request.NotificationTestRequest;
import hanieum.conik.application.notification.provided.NotificationUseCase;
import hanieum.conik.domain.notification.dto.NotificationPayload;
import hanieum.conik.global.adapter.security.AuthDetails;
import hanieum.conik.global.apiPayload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("/v1/notifications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "NOTIFICATION", description = "SSE 알림 구독 및 테스트 API")
public class NotificationController {
    private final NotificationUseCase notificationUseCase;

    @Operation(summary = "알림 SSE 구독", description = """
    ## 사용자 알림을 SSE로 구독합니다.
    - 인증된 사용자만 구독할 수 있습니다.
    - 다중 접속(여러 탭/디바이스)을 허용합니다.
    """)
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
            @AuthenticationPrincipal AuthDetails authDetails
    ) {
        log.info("SSE subscribe requested (memberId={})", authDetails.getMemberId());
        SseEmitter emitter = notificationUseCase.subscribe(authDetails.getMemberId());
        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("ok", MediaType.TEXT_PLAIN));
            log.info("SSE subscribe connected (memberId={})", authDetails.getMemberId());
        } catch (IOException ex) {
            log.warn("SSE subscribe failed (memberId={})", authDetails.getMemberId(), ex);
            emitter.completeWithError(ex);
        }
        return emitter;
    }

    @Operation(summary = "알림 테스트 전송", description = """
    ## 알림 전송이 정상 동작하는지 확인합니다.
    - 개발 확인용 엔드포인트입니다.
    """)
    @PostMapping("/test")
    public ApiResponse<String> sendTestNotification(
            @RequestBody NotificationTestRequest request
    ) {
        NotificationPayload payload = new NotificationPayload(
                request.type(),
                request.title(),
                request.message(),
                request.referenceId()
        );
        notificationUseCase.send(request.targetMemberId(), payload);
        return ApiResponse.success("알림 테스트 전송이 완료되었습니다.");
    }
}
