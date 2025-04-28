package com.spring.chat.application.websocketwithdragonflydb.listener;

import com.spring.chat.application.websocketwithdragonflydb.dto.ChatMessageDto;
import com.spring.chat.application.websocketwithdragonflydb.enums.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    private final RedisTemplate<String,Object> redisTemplate;

    // Listens for WebSocket session connect events
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection");
    }

    // Listens for WebSocket session disconnect events
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // Access WebSocket session headers and attributes
        SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());

        // Retrieve the username stored in WebSocket session attributes during the connection
        String userName = (String) simpMessageHeaderAccessor.getSessionAttributes().get("username");

        // If a user was identified in the session
        if(userName != null) {
            // Create a ChatMessageDto to notify that the user has left because this function will execute when user left the session
            ChatMessageDto chatMessageDto = new ChatMessageDto();
            chatMessageDto.setMessage(userName+" has left the chat");
            chatMessageDto.setUserName(userName);
            chatMessageDto.setMessageType(MessageType.LEAVE);
            log.info("User disconnected from the chat "+userName);

            // Publish the leave message to the Redis channel ("chat")
             redisTemplate.convertAndSend("chat",chatMessageDto);
        }
    }
}

