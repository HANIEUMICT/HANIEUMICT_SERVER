package hanieum.conik.application.chat.event;

import hanieum.conik.application.chat.required.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatEventListener {
    private final ChatMessageRepository chatMessageRepository;

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
}