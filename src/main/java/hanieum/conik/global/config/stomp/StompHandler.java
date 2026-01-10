package hanieum.conik.global.config.stomp;

import hanieum.conik.application.chat.provided.ChatFinder;
import hanieum.conik.global.adapter.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StompHandler implements ChannelInterceptor {
    private final ChatFinder chatService;
    private final JwtTokenProvider jwtTokenProvider;

    public StompHandler(ChatFinder chatService, JwtTokenProvider jwtTokenProvider) {
        this.chatService = chatService;
        this.jwtTokenProvider = jwtTokenProvider;
        log.info("✅ StompHandler Bean Created");
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            log.info("[preSend] CONNECT 진입");

            try {
                Authentication authentication = getAuthentication(accessor);
                accessor.getSessionAttributes().put("AUTH", authentication); //
                log.info("[preSend] accessor.getUser(): " + accessor.getUser());
                log.info("[preSend] 토큰 검증 완료");

                return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
            } catch (Exception e) {
                log.info("[preSend] 예외 발생 => " + e.getClass().getName() + " : " + e.getMessage());
                throw new MessagingException("CONNECT 처리 중 오류: " + e.getMessage(), e);
            }
        }

        if(StompCommand.SUBSCRIBE == accessor.getCommand()){
            log.info("[preSend|SUBSCRIBE] SUBSCRIBE 진입");

            log.info("[preSend] accessor.getUser(): " + accessor.getUser());
            Authentication auth = (Authentication) accessor.getUser();

            if (auth == null && accessor.getSessionAttributes() != null) {
                auth = (Authentication) accessor.getSessionAttributes().get("AUTH");
            }
            if (auth == null) {
                auth = getAuthentication(accessor);
                accessor.getSessionAttributes().put("AUTH", auth);
            } else {
                accessor.setUser(auth);
            }
            log.info("[preSend] accessor.getUser(): {}", accessor.getUser());
            final String destination = accessor.getDestination();
            log.info("[preSend|SUBSCRIBE] destination = {}", destination);

            if (destination == null) {
                throw new MessagingException("Missing destination");
            }

            // 1) 개인 토픽: /topic/user.{memberId}.room-summary
            if (destination.startsWith("/topic/user.")) {
                String afterPrefix = destination.substring("/topic/user.".length()); // e.g. "1.room-summary"
                String[] parts = afterPrefix.split("\\.");
                if (parts.length < 1) {
                    throw new MessagingException("Invalid personal topic: " + destination);
                }
                String requestedUserId = parts[0];
                String authenticatedUserId = (auth != null) ? auth.getName() : null;
                if (authenticatedUserId == null) {
                    throw new MessagingException("Unauthenticated user");
                }
                if (!requestedUserId.equals(authenticatedUserId)) {
                    throw new AccessDeniedException("Cannot subscribe to another user's topic");
                }
                return message;
            }

            // 2) 방 토픽: /topic/chat/room/{roomId}
            if (destination.startsWith("/topic/chat/room/")) {
                String[] segs = destination.split("/");
                // ["", "topic", "chat", "room", "{roomId}"]
                if (segs.length < 5) {
                    throw new MessagingException("Invalid room topic: " + destination);
                }
                String roomIdStr = segs[4];

                String authenticatedUserId = (auth != null) ? auth.getName() : null;
                if (authenticatedUserId == null) {
                    throw new MessagingException("Unauthenticated user");
                }

                Long roomId = Long.parseLong(roomIdStr);
                Long memberId = Long.parseLong(authenticatedUserId);

                if (!chatService.isRoomMember(roomId, memberId)) {
                    throw new MessagingException("UNAUTHORIZED_ROOM_MEMBER");
                }
                return message;
            }        }
        return message;
    }

    private Authentication getAuthentication(StompHeaderAccessor accessor) {
        String bearerToken = accessor.getFirstNativeHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            log.info("[preSend] Authorization 헤더 없음 or 형식 잘못됨");
            throw new IllegalArgumentException("Invalid Authorization header");
        }

        String token = bearerToken.substring(7);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        accessor.setUser(authentication);
        return authentication;
    }
}
