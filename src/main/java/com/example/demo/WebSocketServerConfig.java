package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Copyright (c) 2019 FedEx. All Rights Reserved.<br>
 * <br>
 * Theme - Core Retail Peripheral Services<br>
 * Feature - Peripheral Services - Design and Architecture<br>
 * Description - This class holds the configuration of web-socket server. The client will connect to the give registry
 * end point.
 * 
 * @author Abhishek Singhal [3692173]
 * @version 1.0.0
 * @since Jul 15, 2019
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketServerConfig implements WebSocketMessageBrokerConfigurer {


    /**
     * This method enables receiving of messages on end points which have prefix as queue
     * 
     * @param config {@link MessageBrokerRegistry}
     * @since Jul 15, 2019
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue");
    }

    /**
     * This method adds an end point for web socket server on which clients will make connection to it. Also added
     * support of sockJS for browsers that do not support native web-socket client
     * 
     * @param registry {@link StompEndpointRegistry}
     * @since Jul 15, 2019
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/peripheral-service").setAllowedOrigins("*");
        registry.addEndpoint("/peripheral-service").setAllowedOrigins("*").withSockJS();
    }

}
