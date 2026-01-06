package hanieum.conik.adapter.chat.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import hanieum.conik.adapter.chat.dto.ChatMessageRequest;
import hanieum.conik.application.chat.provided.ChatSaver;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompController {
    private final ChatSaver chatSaver;

    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId,  ChatMessageRequest chatMessageRequest) throws JsonProcessingException {
        chatSaver.sendMessage(roomId, chatMessageRequest);
    }
}
