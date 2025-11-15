package com.spring.chat.application.websocketwithdragonflydb.controller;

import com.spring.chat.application.websocketwithdragonflydb.dto.ChatMessageDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;


@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @InjectMocks
    ChatController chatController;


    @Test
    void sendChatMessage() {
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatController.sendChatMessage(chatMessageDto);
    }

    @Test
    void addUser() {
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        SimpMessageHeaderAccessor headerAccessor = Mockito.mock(SimpMessageHeaderAccessor.class);
        chatController.addUser(chatMessageDto, headerAccessor);
    }
}