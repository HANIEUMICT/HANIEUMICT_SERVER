package hanieum.conik.global.config.stomp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// 스프링과 Stomp는 기본적으로 세션을 자동(내부적)으로 처리한다.
// 연결/해제 이벤트를 기록, 연결된 세션 수를 실시간으로 확인할 목적으로 이벤트 리스너를 생성
@Slf4j
@Component
public class StompEventListener {
    private final Set<String> sessions = ConcurrentHashMap.newKeySet();

    // connect 요청이 발생했을때, 이 eventListener 메서드가 실행된다.
    @EventListener
    public void connectHandle(SessionConnectEvent event) { // event 안에 사용자의 요청 정보가 담겨있다.
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        sessions.add(accessor.getSessionId());
        log.info("Stomp connected. Current session count: {}", sessions.size());
        log.info("Connected session ID: {}", accessor.getSessionId());
    }

    @EventListener
    public void onSubscribe(SessionSubscribeEvent e) {
        log.info("🔔 SUB: user={}, session={}, dest={}",
                e.getUser() != null ? e.getUser().getName() : "null",
                e.getMessage().getHeaders().get("simpSessionId"),
                e.getMessage().getHeaders().get("simpDestination"));
    }

    @EventListener
    public void disconnectHandle(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        sessions.remove(accessor.getSessionId());
        log.info("Stomp disconnected. Current session count: {}", sessions.size());
        log.info("Disconnected session ID: {}", accessor.getSessionId());
    }
}
