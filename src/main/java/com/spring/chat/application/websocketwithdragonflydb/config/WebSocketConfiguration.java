package com.spring.chat.application.websocketwithdragonflydb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration      // Marks this class as a Spring configuration class
@EnableWebSocketMessageBroker   // Enables and configures WebSocket message handling with a broker (STOMP support)
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    // Registers the STOMP endpoint that clients will use to connect to the WebSocket server
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry. addEndpoint("/chat-app")      // Defines the WebSocket endpoint URL for clients (e.g., ws://ourdomain/chat-app)
                .withSockJS();                          // Enables SockJS fallback for browsers that don't support native WebSocket
    }

    // Configures the message broker (how messages are routed between client and server)
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Enables a simple in-memory broker to carry messages back to clients on destinations prefixed with "/topic/public"
        registry.enableSimpleBroker("/topic/public");

        // Sets the prefix for messages bound for methods annotated with @MessageMapping (controller methods)
        registry.setApplicationDestinationPrefixes("/app");
    }
}
