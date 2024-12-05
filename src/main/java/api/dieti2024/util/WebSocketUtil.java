package api.dieti2024.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketUtil {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketUtil.class);

    final SimpMessagingTemplate simpleMessagingTemplate;

    public WebSocketUtil(SimpMessagingTemplate simpleMessagingTemplate) {
        this.simpleMessagingTemplate = simpleMessagingTemplate;
    }
    //inviaMessaggio a una socket specifica
    public void inviaMessaggio(String messaggio, String destinatario) {
        logger.info("Invio messaggio a topic:  {}\n messaggio: {}", destinatario, messaggio);
        simpleMessagingTemplate.convertAndSend(destinatario, messaggio);
    }
}
