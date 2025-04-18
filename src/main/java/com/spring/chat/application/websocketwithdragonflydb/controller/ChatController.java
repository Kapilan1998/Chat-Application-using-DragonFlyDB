package com.spring.chat.application.websocketwithdragonflydb.controller;

import com.spring.chat.application.websocketwithdragonflydb.dto.ChatMessageDto;
import com.spring.chat.application.websocketwithdragonflydb.enums.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    // send message to client
    @MessageMapping("/chat.sendChatMessage")
    public ChatMessageDto sendChatMessage(@Payload ChatMessageDto chatMessageDto) {
        chatMessageDto.setTimeStamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageDto);
        return chatMessageDto;
    }


    // add users to our application
    @MessageMapping("/chat.addUser")
    public ChatMessageDto addUser(@Payload ChatMessageDto chatMessageDto, SimpMessageHeaderAccessor headerAccessor) {
        // get user name from chatMessage object and add it to websocket session
headerAccessor.getSessionAttributes().put("username",chatMessageDto.getUserName());
chatMessageDto.setMessageType(MessageType.JOIN);
chatMessageDto.setMessage(chatMessageDto.getUserName()+" joined the chat");
chatMessageDto.setTimeStamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
log.info("User joined the chat"+ chatMessageDto.getUserName());

        // send the chat message back to client with Message type as join
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageDto);
        return chatMessageDto;
    }
}
