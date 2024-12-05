package api.dieti2024.controller;


import api.dieti2024.util.WebSocketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProvaController {

    @GetMapping("/prova/hello")
    public String hello() {
        return "Hello";
    }

    @Autowired
    WebSocketUtil webSocketUtil;
    @PostMapping("/prova/websocket")
    public String websocket(@RequestBody String url) {
        webSocketUtil.inviaMessaggio("Messaggio di prova al url: "+url, url);
        return "messaggio inviato al url: "+url;
    }

}
