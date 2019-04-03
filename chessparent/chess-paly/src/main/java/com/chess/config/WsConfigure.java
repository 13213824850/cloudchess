package com.chess.config;


import com.chess.play.GameStartWs;
import com.chess.play.WsHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@Slf4j
public class WsConfigure implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        log.info("请求连接");
        registry.addHandler(myHandler(), "/index/").setAllowedOrigins("*");
        registry.addHandler(myHandler(), "/start/").setAllowedOrigins("*");
    }

    @Bean
    public WsHandler myHandler() {
        return new WsHandler();
    }

    @Bean
    public GameStartWs gameStartWs(){
        return new GameStartWs();
    }
}
