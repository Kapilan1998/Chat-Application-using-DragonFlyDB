package com.spring.chat.application.websocketwithdragonflydb.controller;

import com.spring.chat.application.websocketwithdragonflydb.dto.ChatMessageDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @InjectMocks
    ChatController chatController;

    @Mock
    ChannelTopic channelTopic;
    @Mock
    RedisTemplate<String, Object> redisTemplate;


    @Test
    void sendChatMessage() {
        ChatMessageDto messageDto = new ChatMessageDto();
        messageDto.setMessage("test");
        when(channelTopic.getTopic()).thenReturn("topic");

        ChatMessageDto result = chatController.sendChatMessage(messageDto);
        assertNotNull(result);

        verify(redisTemplate, times(1)).convertAndSend("topic", result);
    }

    @Test
    void addUser() {
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        SimpMessageHeaderAccessor headerAccessor = Mockito.mock(SimpMessageHeaderAccessor.class);
        when(channelTopic.getTopic()).thenReturn("topic2");
         ChatMessageDto result =chatController.addUser(chatMessageDto, headerAccessor);
        assertNotNull(result);
        verify(redisTemplate, times(1)).convertAndSend("topic2", chatMessageDto);

    }
}