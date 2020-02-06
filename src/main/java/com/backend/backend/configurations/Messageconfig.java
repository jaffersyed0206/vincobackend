package com.backend.backend.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class Messageconfig implements WebSocketMessageBrokerConfigurer{

    @Override 
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/chat");
        config.setApplicationDestinationPrefixes("/vincoapp");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/mainchat-socket").withSockJS();
    }
}