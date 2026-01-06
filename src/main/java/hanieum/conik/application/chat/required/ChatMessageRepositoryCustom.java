package hanieum.conik.application.chat.required;

import hanieum.conik.domain.chat.dto.ChatMessageDto;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ChatMessageRepositoryCustom {
    // 여러 채팅방(roomIds)에 대해, 각 방의 가장 최근 메시지를 한 번에 조회
    List<ChatMessageDto> findLastMessagesForRooms(Collection<Long> roomIds);
}
