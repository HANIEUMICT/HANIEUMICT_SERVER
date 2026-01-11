package hanieum.conik.application.chat.event;

import hanieum.conik.application.chat.required.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatEventListener {
    private final ChatMessageRepository chatMessageRepository;

    @Async
    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDeleteChatRoomMessages(ChatRoomDeletedEvent event) {
        Long roomId = event.roomId();

        try {
            long deleted = chatMessageRepository.deleteByRoomId(roomId);
            log.info("[CHAT] Transaction Committed. Deleted MongoDB messages for room {} = {}", roomId, deleted);
        } catch (Exception e) {
            log.error("[CHAT] Failed to delete MongoDB messages for room {}", roomId, e);
        }
    }

    /**
     * @Recover: 위 메서드가 3번 다 실패했을 때 최종적으로 실행되는 메서드 (try-catch의 catch 역할)
     */
    @Recover
    public void recover(Exception e, ChatRoomDeletedEvent event) {
        log.error("[CHAT] Failed to delete MongoDB messages for room {} after retries.", event.roomId(), e);
        // 여기서 나중에 배치 처리를 위한 로그를 남기거나, 개발자에게 알림을 보낼 수도 있습니다.
    }
}