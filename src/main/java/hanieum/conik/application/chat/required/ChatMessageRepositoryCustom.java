package hanieum.conik.application.chat.required;

import java.util.Collection;
import java.util.Map;

public interface ChatMessageRepositoryCustom {
    // 여러 채팅방(roomIds)에 대해, 각 방의 가장 최근 메시지 seq 값을 한 번에 조회
    Map<Long, Long> findMaxSeqForRoomIds(Collection<Long> roomIds);
}
