package hanieum.conik.application.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanieum.conik.adapter.chat.dto.ChatRoomSummary;
import hanieum.conik.domain.chat.dto.ChatMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisPubSubService implements MessageListener {
    private final StringRedisTemplate stringRedisTemplate;
    private final SimpMessageSendingOperations messageTemplate;
    private final ObjectMapper objectMapper;

    public RedisPubSubService(@Qualifier("chatPubSub") StringRedisTemplate stringRedisTemplate, SimpMessageSendingOperations messageTemplate, ObjectMapper objectMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.messageTemplate = messageTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(ChatMessageDto chatMessageDto){
        try {
            String message = objectMapper.writeValueAsString(chatMessageDto);
            stringRedisTemplate.convertAndSend("chat", message); // "chat" 토픽으로 발행
        } catch (JsonProcessingException e) {
            log.error("Redis Publish Error", e);
        }
    }

    public void publishSummary(Long targetMemberId, ChatRoomSummary summary) {
        try {
            String message = objectMapper.writeValueAsString(summary);
            stringRedisTemplate.convertAndSend("chat.summary." + targetMemberId, message);
        } catch (JsonProcessingException e) {
            log.error("Redis Publish Error (Summary)", e);
        }
    }

    @Override
//    pattern에는 topic의 이름의 패턴이 담겨있고, 이 패턴을 기반으로 다이나믹한 코딩
    public void onMessage(Message message, byte[] pattern) {
        try {
            // channelType 예시: "chat" 또는 "chat.summary.100"
            String channelType = new String(message.getChannel());
            String payload = new String(message.getBody());

            // 1. 일반 채팅 메시지
            if ("chat".equals(channelType)) {
                ChatMessageDto dto = objectMapper.readValue(payload, ChatMessageDto.class);
                messageTemplate.convertAndSend("/v1/topic/chat/room/" + dto.roomId(), dto);

                // 2. 개인 알림 (토픽 이름이 "chat.summary."로 시작하는 경우)
            } else if (channelType.startsWith("chat.summary.")) {
                String memberIdStr = channelType.substring("chat.summary.".length());
                Long memberId = Long.parseLong(memberIdStr);

                ChatRoomSummary summary = objectMapper.readValue(payload, ChatRoomSummary.class);

                messageTemplate.convertAndSend(
                        "/v1/topic/user." + memberId + ".room-summary",
                        summary
                );
            }
        } catch (Exception e) {
            log.error("Redis Subscribe Error", e);
        }
    }
}