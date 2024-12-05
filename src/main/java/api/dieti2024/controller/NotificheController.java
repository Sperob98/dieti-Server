package api.dieti2024.controller;

import api.dieti2024.dto.NotificaDTO;
import api.dieti2024.service.NotificaService;
import api.dieti2024.util.ControllerRestUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class NotificheController {

    private final NotificaService notificaService;

    public NotificheController(NotificaService notificaService) {
        this.notificaService = notificaService;
    }


    @GetMapping("/numeroNotificheTotali")
    public int getNumeroNotificheTotali() {
        String email = ControllerRestUtil.getEmailOfUtenteCorrente();
        return notificaService.getNumeroNotificheTotali(email);
    }
    @GetMapping("/numeroNotificheNonVisualizzate")
    public int getNumeroNotificheNonVisualizzate() {
       String email = ControllerRestUtil.getEmailOfUtenteCorrente();
        return notificaService.getNumeroNotificheNonVisualizzate(email);
    }
    @GetMapping("/notificheTotali")
    public List<NotificaDTO> getNotificheTotali(@RequestParam int numeroElementi, @RequestParam int numeroPagina) {
        String email = ControllerRestUtil.getEmailOfUtenteCorrente();
        return notificaService.getNotificheUtente(email, numeroElementi, numeroPagina);

    }
    @GetMapping("/notificheNonVisualizzate")
    public List<NotificaDTO> getNotificheNonVisualizzate(@RequestParam int numeroElementi, @RequestParam int numeroPagina) {
        String email = ControllerRestUtil.getEmailOfUtenteCorrente();
        return notificaService.getNotificheNonVisualizzateUtente(email, numeroElementi, numeroPagina);
    }

    //todo implementare il metodo per segnare le notifiche come visualizzate
    @PostMapping("/segnaNotificheVisualizzate")
    public void segnaNotificheVisualizzate(@RequestParam("idNotifiche") List<Integer> idNotifiche){
        String email = ControllerRestUtil.getEmailOfUtenteCorrente();
        notificaService.segnaNotificheVisualizzate(email, idNotifiche);
    }
}
