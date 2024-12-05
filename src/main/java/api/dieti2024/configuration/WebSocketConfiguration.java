package api.dieti2024.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Autowired
    JwtHandshakeInterceptor jwtHandshakeInterceptor;
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
       registration.interceptors( jwtHandshakeInterceptor);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/asta","notifichePersonali" )
                .setHeartbeatValue(new long[]{10000, 10000}) // Imposta heartbeat server ogni 10 secondi
                .setTaskScheduler(taskScheduler()); // Associa TaskScheduler per gestire gli heartbeat
        registry.setApplicationDestinationPrefixes("/app");
    }

    // Definisci un bean TaskScheduler per gestire gli heartbeat
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1); // Imposta la dimensione del pool
        scheduler.setThreadNamePrefix("wss-heartbeat-thread-"); // Nome del thread per gli heartbeat
        scheduler.initialize();
        return scheduler;
    }
}
