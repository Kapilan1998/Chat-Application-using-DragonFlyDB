package com.spring.chat.application.websocketwithdragonflydb.dto;

import com.spring.chat.application.websocketwithdragonflydb.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    private String message;
    private String timeStamp;
    private String userName;
    private MessageType messageType;
}
