package hanieum.conik.application.chat.required;

import hanieum.conik.domain.chat.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String>, ChatMessageRepositoryCustom {
    // 최근 메시지 N건: roomId 일치, seq DESC, limit(pageable)
    List<ChatMessage> findByRoomIdOrderBySeqDesc(Long roomId, Pageable pageable);

    // 최신 1건 조회
    Optional<ChatMessage> findTopByRoomIdOrderBySeqDesc(Long roomId);

    // 특정 방의 모든 메시지 삭제
    long deleteByRoomId(Long roomId);

    // 특정 seq 이전 N건 조회
    List<ChatMessage> findByRoomIdAndSeqLessThanOrderBySeqDesc(Long roomId, Long beforeSeq, Pageable pageable);

    // 현재 채팅방의 마지막 메시지 번호를 찾기
    default long findMaxSeqByRoomId(Long roomId) {
        return findTopByRoomIdOrderBySeqDesc(roomId)
                .map(ChatMessage::getSeq)
                .orElse(0L);
    }
}
