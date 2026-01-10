package hanieum.conik.application.chat;

import hanieum.conik.adapter.chat.dto.ChatRoomSummary;
import hanieum.conik.application.chat.provided.ChatFinder;
import hanieum.conik.application.chat.required.*;
import hanieum.conik.application.member.provided.MemberFinder;
import hanieum.conik.domain.chat.dto.ChatMessageDto;
import hanieum.conik.domain.chat.entity.ChatMessage;
import hanieum.conik.domain.chat.entity.ChatRoom;
import hanieum.conik.domain.chat.entity.ChatRoomMember;
import hanieum.conik.domain.chat.enumerate.ChatRoomType;
import hanieum.conik.domain.chat.exception.ChatErrorType;
import hanieum.conik.domain.chat.exception.ChatException;
import hanieum.conik.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatFinderService implements ChatFinder {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberFinder memberFinder;

    private final ChatMessageRepository chatMessageRepository;  // Mongo

    @Override
    public ChatRoom findRoomByRoomId(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ChatErrorType.CHAT_ROOM_NOT_FOUND));
    }

    @Override
    public ChatRoomMember findChatRoomMember(Long roomId, Long memberId){
        return chatRoomMemberRepository.findByChatRoom_IdAndMemberId(roomId, memberId)
                .orElseThrow(() -> new ChatException(ChatErrorType.MEMBER_NOT_IN_CHAT_ROOM));
    }

    @Override
    public List<ChatRoomMember> findRoomMembers(Long roomId){
        return chatRoomMemberRepository.findByChatRoom_Id(roomId);
    }

    @Override
    public Page<ChatRoomSummary> findRoomsByMember(Long memberId, Pageable pageable) {
        Member member = memberFinder.findById(memberId);

        // 1. 내가 참여 중인 채팅방 목록 조회 (페이징)
        Page<ChatRoomMember> page = chatRoomMemberRepository.findByMemberId(member.getId(), pageable);
        if (page.isEmpty()) return Page.empty(pageable);

        List<ChatRoomMember> chatRoomMembers = page.getContent();

        // 2. 조회된 채팅방들의 ID 추출
        List<Long> roomIds = chatRoomMembers.stream()
                .map(crm -> crm.getChatRoom().getId())
                .toList();

        // 3. 각 채팅방의 마지막 메시지/안 읽은 개수 조회 (MongoDB)
        LastMessageInfo info = getLastMessageInfo(memberId, roomIds);

        // 4. 각 채팅방의 '모든 참여자' 정보 조회 (방 제목/썸네일 생성용)
        Map<Long, List<ChatRoomMember>> roomMembersMap = chatRoomMemberRepository.findByChatRoom_IdIn(roomIds)
                .stream()
                .collect(Collectors.groupingBy(crm -> crm.getChatRoom().getId()));

        // 5. 데이터 조합하여 DTO 변환
        List<ChatRoomSummary> summaries = chatRoomMembers.stream()
                .map(crm -> getRoomSummary(crm, info, roomMembersMap))
                .toList();

        return new PageImpl<>(summaries, pageable, page.getTotalElements());
    }

    private LastMessageInfo getLastMessageInfo(Long memberId, List<Long> roomIds) {
        Map<Long, ChatMessageDto> lastMessageMap = chatMessageRepository.findLastMessagesForRooms(roomIds)
                .stream()
                .collect(Collectors.toMap(
                        ChatMessageDto::roomId,
                        dto -> dto,
                        (a, b) -> a
                ));

        Map<Long, Long> myLastReadMap = chatRoomMemberRepository.findLastReadSeqs(memberId, roomIds)
                .stream()
                .collect(Collectors.toMap(
                        ChatRoomMemberRepository.LastReadView::getRoomId,
                        v -> Optional.ofNullable(v.getLastReadSeq()).orElse(0L)
                ));

        return new LastMessageInfo(lastMessageMap, myLastReadMap);
    }

    private record LastMessageInfo(
            Map<Long, ChatMessageDto> lastMessageMap,
            Map<Long, Long> myLastReadMap
    ) {}

    private ChatRoomSummary getRoomSummary(
            ChatRoomMember crm,
            LastMessageInfo info,
            Map<Long, List<ChatRoomMember>> roomMembersMap
    ) {
        ChatRoom room = crm.getChatRoom();
        Long roomId = room.getId();

        List<ChatRoomMember> members = roomMembersMap.getOrDefault(roomId, List.of());
        String roomName = buildRoomNameForMember(room, crm.getMemberId(), members);

        ChatMessageDto last = info.lastMessageMap().get(roomId);

        if (last == null) {
            return ChatRoomSummary.of(room, roomName, 0, null);
        }

        long myLast = info.myLastReadMap().getOrDefault(roomId, 0L);
        long unread = Math.max(0L, last.seq() - myLast);

        return ChatRoomSummary.of(room, roomName, unread, last);
    }

    private String buildRoomNameForMember(ChatRoom room, Long myId, List<ChatRoomMember> members) {
        // memberId -> 표시 이름
        List<Long> memberIds = members.stream()
                .map(ChatRoomMember::getMemberId)
                .toList();
        Map<Long, String> idToName = memberFinder.findAllByIds(memberIds).stream()
                .collect(Collectors.toMap(Member::getId, Member::getName, (a, b) -> a));

        if (room.getType() == ChatRoomType.PRIVATE) {
            // 나 제외 1명
            return members.stream()
                    .map(ChatRoomMember::getMemberId)
                    .filter(id -> !id.equals(myId))
                    .findFirst()
                    .map(idToName::get)
                    .orElse("(알 수 없음)");
        }

        // GROUP: 나 제외 이름들로 "A, B, C 외 n명"
        List<String> others = members.stream()
                .map(ChatRoomMember::getMemberId)
                .filter(id -> !id.equals(myId))
                .map(idToName::get)
                .filter(n -> n != null && !n.isBlank())
                .distinct()
                .sorted()
                .toList();

        return formatGroupRoomName(others, 3);
    }

    private String formatGroupRoomName(List<String> names, int limit) {
        if (names == null || names.isEmpty()) return "그룹 채팅";

        List<String> shown = names.stream().limit(limit).toList();
        int remain = names.size() - shown.size();

        String base = String.join(", ", shown);
        return remain > 0 ? base + " 외 " + remain + "명" : base;
    }

    // 특정 메시지(seq) 이전의 N개 메시지를 가져온다.
    @Override
    public Slice<ChatMessageDto> fetchMessagesBeforeSeq(Long roomId, Long beforeSeq, int size, Long memberId) {
        // 멤버가 채팅방의 구성원인지 확인
        chatRoomMemberRepository.findByChatRoom_IdAndMemberId(roomId, memberId)
                .orElseThrow(() -> new ChatException(ChatErrorType.MEMBER_NOT_IN_CHAT_ROOM));

        // 메시지 조회
        Pageable pageable = PageRequest.of(0, size + 1, Sort.by(DESC, "seq"));
        List<ChatMessage> rawMessages;

        if (beforeSeq == null) {
            rawMessages = chatMessageRepository.findByRoomIdOrderBySeqDesc(roomId, pageable);
        } else {
            rawMessages = chatMessageRepository.findByRoomIdAndSeqLessThanOrderBySeqDesc(roomId, beforeSeq, pageable);
        }

        // 3. 다음 페이지 여부 확인 및 리스트 자르기
        boolean hasNext = rawMessages.size() > size;
        List<ChatMessage> pagedMessages = hasNext ? rawMessages.subList(0, size) : rawMessages;

        // 4. [Exception 방지] 수정 가능한 리스트로 복사 후 역정렬
        // (MongoDB 리턴값이나 subList 뷰는 불변일 수 있어 Collections.reverse 시 에러 발생 가능)
        List<ChatMessage> mutableMessages = new ArrayList<>(pagedMessages);
        Collections.reverse(mutableMessages); // 과거 -> 최신 순으로 정렬 변경

        // 5. [N+1 해결] 메시지 작성자(Sender) ID 일괄 수집
        Set<Long> senderIds = mutableMessages.stream()
                .map(ChatMessage::getSenderId)
                .collect(Collectors.toSet());

        // 6. [N+1 해결] 작성자 정보 일괄 조회
        Map<Long, String> senderNameMap = memberFinder.findAllByIds(new ArrayList<>(senderIds))
                .stream()
                .collect(Collectors.toMap(
                        Member::getId,
                        Member::getName,
                        (a, b) -> a
                ));

        // 7. DTO 변환 (DB 조회 없이 Map에서 이름 매핑)
        List<ChatMessageDto> content = mutableMessages.stream()
                .map(msg -> {
                    String senderName = senderNameMap.getOrDefault(msg.getSenderId(), "(알 수 없음)");
                    return ChatMessageDto.fromEntity(msg, senderName);
                })
                .toList();
        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public boolean isRoomMember(Long roomId, Long memberId) {
        return chatRoomMemberRepository.findByChatRoom_IdAndMemberId(roomId, memberId).isPresent();
    }

    @Override
    public long findLatestMessageSeq(Long roomId) {
        return chatMessageRepository.findTopByRoomIdOrderBySeqDesc(roomId)
                .map(ChatMessage::getSeq)
                .orElse(0L);
    }
}