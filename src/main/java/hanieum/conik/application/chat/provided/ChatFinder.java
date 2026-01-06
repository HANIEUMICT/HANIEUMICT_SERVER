package hanieum.conik.application.chat.provided;

import hanieum.conik.adapter.chat.dto.ChatRoomSummary;
import hanieum.conik.domain.chat.dto.ChatMessageDto;
import hanieum.conik.domain.chat.entity.ChatRoom;
import hanieum.conik.domain.chat.entity.ChatRoomMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ChatFinder {
    ChatRoom findRoomByRoomId(Long roomId);

    ChatRoomMember findChatRoomMember(Long roomId, Long memberId);

    List<ChatRoomMember> findRoomMembers(Long roomId);

    // 내가 속한 채팅방 목록
    Page<ChatRoomSummary> findRoomsByMember(Long memberId, Pageable pageable);

    // 특정 seq 이전 N건 조회하기
    Slice<ChatMessageDto> fetchMessagesBeforeSeq(Long roomId, Long beforeSeq, int size, Long memberId);

    // 채팅방에 존재하는 멤버인지
    boolean isRoomMember(Long roomId, Long memberId);

    // 특정 채팅방의 최신 메시지 seq 조회
    long findLatestMessageSeq(Long roomId);
}
