package hanieum.conik.application.chat.required;

import hanieum.conik.domain.chat.entity.ChatRoomMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    // 특정 방에 특정 멤버로 가입된 ChatRoomMember 엔티티 조회
    Optional<ChatRoomMember> findByChatRoom_IdAndMember_Id(Long roomId, Long memberId);

    // 특정 방에 속한 모든 ChatRoomMember 엔티티 조회
    List<ChatRoomMember> findByChatRoom_Id(Long roomId);

    // 특정 방에 속한 멤버 수 조회
    long countByChatRoom_Id(Long roomId);

    // 특정 멤버가 속한 모든 ChatRoomMember 엔티티 조회
    @EntityGraph(attributePaths = {"chatRoom"})
    Page<ChatRoomMember> findByMember_Id(Long memberId, Pageable pageable);

    @EntityGraph(attributePaths = {"member", "chatRoom"})
    List<ChatRoomMember> findByChatRoom_IdIn(List<Long> roomIds);

    // 배치로 방별 lastReadSeq를 한 번에 읽기 위한 Projection
    interface LastReadView {
        Long getRoomId();
        Long getLastReadSeq();
    }

    interface LastReadSeqView {
        Long getMemberId();
        Long getLastReadSeq();
    }

    // 특정 멤버가 여러 방에서 마지막으로 읽은 시퀀스(lastReadSeq)를 한 번에 조회
    @Query("""
        select crm.chatRoom.id as roomId, crm.lastReadSeq as lastReadSeq
        from ChatRoomMember crm
        where crm.member.id = :memberId and crm.chatRoom.id in :roomIds
    """)
    List<LastReadView> findLastReadSeqs(@Param("memberId") Long memberId, @Param("roomIds") List<Long> roomIds);
}
