package hanieum.conik.application.notification;

import hanieum.conik.application.notification.provided.NotificationUseCase;
import hanieum.conik.application.notification.required.NotificationPort;
import hanieum.conik.domain.notification.dto.NotificationPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationUseCase {
    private final NotificationPort notificationPort;

    @Override
    public void send(Long memberId, NotificationPayload payload) {
        notificationPort.send(memberId, payload);
    }

    @Override
    public SseEmitter subscribe(Long memberId) {
        return notificationPort.subscribe(memberId);
    }
}
