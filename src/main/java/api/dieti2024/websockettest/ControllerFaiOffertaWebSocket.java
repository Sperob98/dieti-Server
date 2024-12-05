package api.dieti2024.websockettest;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping
public class ControllerFaiOffertaWebSocket {

    final
    SimpMessagingTemplate simpleMessagingTemplate;

    public ControllerFaiOffertaWebSocket(SimpMessagingTemplate simpleMessagingTemplate) {
        this.simpleMessagingTemplate = simpleMessagingTemplate;
    }

    @GetMapping("public/inviamsg/{astaid}")
    public String faiOfferta(@RequestParam String messaggio,@PathVariable String astaid) {
        String topic = "/asta/"+astaid;
        simpleMessagingTemplate.convertAndSend(topic, "un tizio ha detto: "+messaggio);
        return "qualcosa ho fatto poi sti cazzi";
    }

}
