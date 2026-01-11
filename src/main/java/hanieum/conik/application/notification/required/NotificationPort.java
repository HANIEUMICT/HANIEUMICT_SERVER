package hanieum.conik.application.notification.required;

import hanieum.conik.domain.notification.dto.NotificationPayload;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationPort {
    void send(Long memberId, NotificationPayload payload);

    SseEmitter subscribe(Long memberId);
}
