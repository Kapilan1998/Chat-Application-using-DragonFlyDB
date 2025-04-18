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

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String userName = (String) simpMessageHeaderAccessor.getSessionAttributes().get("username");

        if(userName != null) {
            ChatMessageDto chatMessageDto = new ChatMessageDto();
            chatMessageDto.setMessage(userName+" has left the chat");
            chatMessageDto.setUserName(userName);
            chatMessageDto.setMessageType(MessageType.LEAVE);
            log.info("User disconnected from the chat "+userName);
            redisTemplate.convertAndSend("chat",chatMessageDto);
        }
    }
}

