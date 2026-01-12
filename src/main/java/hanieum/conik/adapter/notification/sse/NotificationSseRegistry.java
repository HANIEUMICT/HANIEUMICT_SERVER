package hanieum.conik.adapter.notification.sse;

import hanieum.conik.application.notification.required.NotificationPort;
import hanieum.conik.domain.notification.dto.NotificationPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class NotificationSseRegistry implements NotificationPort {
    private static final long DEFAULT_TIMEOUT = 0L;

    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter addEmitter(Long memberId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitters.computeIfAbsent(memberId, ignored -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(memberId, emitter));
        emitter.onTimeout(() -> removeEmitter(memberId, emitter));
        emitter.onError(ignored -> removeEmitter(memberId, emitter));

        return emitter;
    }

    @Override
    public SseEmitter subscribe(Long memberId) {
        return addEmitter(memberId);
    }

    @Override
    public void send(Long memberId, NotificationPayload payload) {
        List<SseEmitter> memberEmitters = emitters.get(memberId);
        if (memberEmitters == null || memberEmitters.isEmpty()) {
            log.info("SSE push skipped: no active emitters (memberId={})", memberId);
            return;
        }

        for (SseEmitter emitter : memberEmitters) {
            try {
                safeSend(emitter, SseEmitter.event()
                        .name("notification")
                        .data(payload, MediaType.APPLICATION_JSON));
                log.info("SSE push sent (memberId={}, type={}, title={})",
                        memberId, payload.type(), payload.title());
            } catch (IOException ex) {
                log.warn("SSE push failed, removing emitter (memberId={})", memberId, ex);
                removeEmitter(memberId, emitter);
            }
        }
    }

    @Scheduled(fixedDelayString = "${notification.sse.heartbeat-ms:30000}")
    public void sendHeartbeat() {
        for (Map.Entry<Long, List<SseEmitter>> entry : emitters.entrySet()) {
            Long memberId = entry.getKey();
            List<SseEmitter> memberEmitters = entry.getValue();
            if (memberEmitters == null || memberEmitters.isEmpty()) {
                emitters.remove(memberId);
                continue;
            }
            for (SseEmitter emitter : memberEmitters) {
                try {
                    safeSend(emitter, SseEmitter.event()
                            .name("ping")
                            .data("ok", MediaType.TEXT_PLAIN));
                } catch (IOException ex) {
                    log.debug("SSE heartbeat failed, removing emitter (memberId={})", memberId, ex);
                    removeEmitter(memberId, emitter);
                }
            }
        }
    }

    private void removeEmitter(Long memberId, SseEmitter emitter) {
        List<SseEmitter> memberEmitters = emitters.get(memberId);
        if (memberEmitters == null) {
            return;
        }
        memberEmitters.remove(emitter);
        if (memberEmitters.isEmpty()) {
            emitters.remove(memberId);
        }
    }

    private void safeSend(SseEmitter emitter, SseEmitter.SseEventBuilder event) throws IOException {
        synchronized (emitter) {
            emitter.send(event);
        }
    }
}
