package com.spring.chat.application.websocketwithdragonflydb.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.chat.application.websocketwithdragonflydb.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    // Method triggered whenever a new message is published to the Redis topic
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // Deserialize the raw Redis message body into a String (originally sent by redisTemplate.convertAndSend)
        String publishedMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
        try {
            // Parse the JSON string into a ChatMessageDto object
             ChatMessageDto chatMessageDto = objectMapper.readValue(publishedMessage, ChatMessageDto.class);

            // Send the ChatMessageDto to all WebSocket clients subscribed to '/topic/public'
            simpMessageSendingOperations.convertAndSend("/topic/public", chatMessageDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
