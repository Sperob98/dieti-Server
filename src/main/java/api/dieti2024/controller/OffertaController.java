package api.dieti2024.controller;

import api.dieti2024.dto.OffertaDto;
import api.dieti2024.dto.OffertaVincenteDto;
import api.dieti2024.model.Offerta;
import api.dieti2024.service.OffertaService;
import api.dieti2024.util.CalendarioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class OffertaController {

    final SimpMessagingTemplate simpleMessagingTemplate;

    final OffertaService offertaService;

    @GetMapping("/public/offerteAstaInglese/{idAssta}")
    public List<Offerta> getInfoProdottoAsta(@PathVariable int idAssta) {
       return offertaService.getOffertePubliche(idAssta);
    }





    //get PrezzoOfferta AstaSilenziosa return list coppia id prezzo
    @GetMapping("/offerte/infoCompleteAstaSilenziosa/{idAssta}")
    public List<Offerta> getInfoCompleteAstaSilenziosa(@PathVariable int idAssta) {
        return offertaService.getInfoCompleteAstaSilenziosa(idAssta);
    }
    @GetMapping("/offerteEffettuate/{idAsta}")
    public List<Offerta> getOfferteEffettuate(@PathVariable int idAsta) {
        return offertaService.getOfferteEffettuate(idAsta);
    }
    @Autowired
    public OffertaController(SimpMessagingTemplate simpleMessagingTemplate, OffertaService offertaService) {
        this.simpleMessagingTemplate = simpleMessagingTemplate;
        this.offertaService = offertaService;
    }

    @PostMapping("/faiOfferta")
    public Offerta faiOfferta(@RequestBody OffertaDto offertaDto) {
        long tempoOfferta = CalendarioUtil.ottieniTempoAttuale();
        Offerta offerta = offertaService.faiOfferta(offertaDto, tempoOfferta);
        return offerta;
    }



    @PostMapping("ConfermaOffertaVincente")
    public Offerta confermaOffertaVincente(@RequestBody OffertaVincenteDto offertaDto) {
        return offertaService.confermaOffertaVincente(offertaDto);
    }
    //get offerta vincente di un asta
    @GetMapping("public/offertaVincente/{idAsta}")
    public Offerta getOffertaVincente(@PathVariable int idAsta) {
        return offertaService.getOffertaVincente(idAsta);
    }

}
