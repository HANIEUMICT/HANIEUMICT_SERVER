package hanieum.conik.adapter.chat.webapi;

import hanieum.conik.adapter.chat.dto.ChatRoomSummary;
import hanieum.conik.application.chat.provided.ChatFinder;
import hanieum.conik.application.chat.provided.ChatSaver;
import hanieum.conik.domain.chat.dto.ChatMessageDto;
import hanieum.conik.global.adapter.security.AuthDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatSaver chatSaver;
    private final ChatFinder chatFinder;

    // 개인 채팅방 개설 또는 기존 roomId return
    @Operation(
            summary = "개인 채팅방 개설",
            description = "다른 멤버와 1:1 개인 채팅방을 개설합니다. 이미 개인 채팅방이 존재하는 경우 기존 roomId를 반환합니다."
    )
    @PostMapping("/private")
    public Long createPrivateChatRoom(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestParam Long otherMemberId) {
        return chatSaver.createPrivateRoom(authDetails.getMemberId(), otherMemberId);
    }

    // 내 채팅방 목록 조회 : roomId, roomName, 그룹채팅여부, 메시지읽음개수
    @Operation(
            summary = "내 채팅방 목록 조회",
            description = """
                    내가 참여 중인 채팅방 목록을 페이징하여 조회합니다.
                    
                    **[응답 정보]**
                    - 채팅방 기본 정보 (ID, 이름, 썸네일 등)
                    - **안 읽은 메시지 개수 (unreadCount)**
                    - 마지막으로 수신된 메시지 내용 및 시간
                    """
    )
    @GetMapping("/rooms/me")
    public Page<ChatRoomSummary> getMyChatRooms(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return chatFinder.findRoomsByMember(authDetails.getMemberId(), pageable);
    }

    // 메시지 조회
    @Operation(
            summary = "채팅 메시지 조회 (무한 스크롤)",
            description = """
                    특정 채팅방의 대화 내역을 조회합니다. **커서 기반 페이지네이션(No-Offset)**을 사용합니다.
                    
                    **[파라미터 설명]**
                    - `roomId`: 조회할 채팅방 ID
                    - `beforeSeq`: **이 값보다 작은(오래된)** 메시지를 불러옵니다. 
                       - 채팅방 최초 진입 시: `null` (가장 최신 메시지부터 조회)
                       - 스크롤 올릴 시: 현재 화면의 **가장 상단 메시지의 seq** 값
                    - `size`: 가져올 메시지 개수 (기본값 50)
                    """
    )
    @GetMapping("/rooms/{roomId}/messages")
    public Slice<ChatMessageDto> getMessages(
            @PathVariable Long roomId,
            @RequestParam(required = false) Long beforeSeq,
            @RequestParam(defaultValue = "50") int size,
            @AuthenticationPrincipal AuthDetails authDetails
    ) {
        return chatFinder.fetchMessagesBeforeSeq(roomId, beforeSeq, size, authDetails.getMemberId());
    }

    // 채팅 메시지 읽음 처리
    @Operation(
            summary = "채팅 메시지 읽음 처리 (Sync)",
            description = """
                    해당 채팅방의 **'마지막 읽은 위치(lastReadSeq)'**를 현재 시점의 가장 최신 메시지로 갱신합니다.
                    
                    **[필수 호출 시점]**
                    1. **채팅방 입장 시**: 쌓여있는 메시지 일괄 읽음 처리
                    2. **메시지 수신 시 (보고 있을 때)**: 실시간 수신된 메시지 읽음 처리
                    3. **채팅방 퇴장/구독해제 시**: 마지막으로 본 위치 저장
                    """
    )
    @PostMapping("/rooms/{roomId}/read")
    public void markMessagesAsRead(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long roomId) {
        chatSaver.updateLastRead(roomId, authDetails.getMemberId(), chatFinder.findLatestMessageSeq(roomId));
    }

    // 채팅방 나가기
    @Operation(
            summary = "채팅방 나가기 (방 떠나기)",
            description = """
                    특정 채팅방에서 완전히 나갑니다. 
                    """
    )
    @DeleteMapping("/rooms/{roomId}")
    public void leaveChatRoom(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long roomId) {
        chatSaver.leaveChatRoom(roomId, authDetails.getMemberId());
    }
}
