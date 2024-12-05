package api.dieti2024.websockettest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class HeartbeatController {

    private static final Logger logger = LoggerFactory.getLogger(HeartbeatController.class);

    @MessageMapping("/heartbeat")
    @SendToUser("/queue/heartbeat")
    public String handleHeartbeat(String message) {

        logger.error("Heartbeat ricevuto: {}", message);
        // Aggiungi eventuale logica necessaria qui, se necessario
        return "EHLO";
    }
}
