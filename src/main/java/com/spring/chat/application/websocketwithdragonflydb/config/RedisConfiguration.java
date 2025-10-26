package com.spring.chat.application.websocketwithdragonflydb.config;

import com.spring.chat.application.websocketwithdragonflydb.listener.RedisMessageSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration      // Marks this class as a Spring configuration class that will define beans
@RequiredArgsConstructor    // Generates a constructor with required fields
public class RedisConfiguration {

    // Defines a RedisTemplate bean used to interact with Redis server
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // Set the connection factory to enable Redis connection
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // Serialize the keys as simple Strings (e.g., for easier readability and debugging)
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // Serialize the values as JSON strings using Jackson
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }


    // Defines a ChannelTopic bean, representing the Redis Pub/Sub topic for chat messages
    @Bean
    public ChannelTopic channelTopic() {
        // 'chat' is the name of the topic/channel used for publishing and subscribing messages
        return new ChannelTopic("chat");
    }

    // Defines a RedisMessageListenerContainer bean, which listens for messages published to Redis channels
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory,
                                                                       MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        // Set the connection factory so the listener can connect to Redis
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);

        // Add the message listener and bind it to the specified chat topic
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter,channelTopic());
        return redisMessageListenerContainer;
    }

    // Defines a MessageListenerAdapter bean, which adapts our custom subscriber to Spring's Redis Pub/Sub system
    @Bean
    public MessageListenerAdapter messageListenerAdapter(RedisMessageSubscriber redisMessageSubscriber) {
        // Automatically maps incoming Redis messages to the appropriate method of RedisMessageSubscriber
        return new MessageListenerAdapter(redisMessageSubscriber);
    }
}
