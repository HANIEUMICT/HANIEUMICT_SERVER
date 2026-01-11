package hanieum.conik.application.chat.required;

import hanieum.conik.domain.chat.entity.ChatRoom;
import hanieum.conik.domain.chat.enumerate.ChatRoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("""
        select r
        from ChatRoom r
        where r.type = :type
          and exists (
            select 1 from ChatRoomMember m
            where m.chatRoom = r and m.memberId = :memberAId
          )
          and exists (
            select 1 from ChatRoomMember m
            where m.chatRoom = r and m.memberId = :memberBId
          )
    """)
    Optional<ChatRoom> findPrivateRoomBetweenMembers(
            @Param("memberAId") Long memberAId,
            @Param("memberBId") Long memberBId,
            @Param("type") ChatRoomType type
    );
}
