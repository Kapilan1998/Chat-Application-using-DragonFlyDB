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


@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @InjectMocks
    ChatController chatController;

    @Mock
    ChannelTopic channelTopic;


    @Test
    void sendChatMessage() {
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        Mockito.when(channelTopic.getTopic()).thenReturn("sendChatMessage");
        assertNotNull(chatController.sendChatMessage(chatMessageDto));
    }

    @Test
    void addUser() {
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        SimpMessageHeaderAccessor headerAccessor = Mockito.mock(SimpMessageHeaderAccessor.class);
        Mockito.when(channelTopic.getTopic()).thenReturn("addUser");
        assertNotNull(chatController.addUser(chatMessageDto, headerAccessor));
    }
}