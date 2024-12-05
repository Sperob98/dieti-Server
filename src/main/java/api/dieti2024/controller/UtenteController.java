package api.dieti2024.controller;

import api.dieti2024.dto.ModificaProfiloDTO;
import api.dieti2024.dto.utente.ProfiloUtentePublicoDTO;
import api.dieti2024.exceptions.ApiException;
import api.dieti2024.service.UtenteService;
import api.dieti2024.util.ControllerRestUtil;
import api.dieti2024.util.ImageContainerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
public class UtenteController {

    private final UtenteService utenteService;
    private final ImageContainerUtil imageContainerUtil;

    public UtenteController(UtenteService utenteService, ImageContainerUtil imageContainerUtil) {
        this.utenteService = utenteService;
        this.imageContainerUtil = imageContainerUtil;
    }

    @GetMapping("public/utente/datiProfilo/{email}")
    public ProfiloUtentePublicoDTO getDatiProfilo(@PathVariable String email) {
        return utenteService.getDatiProfilo(email);
    }

    @PutMapping("utente/datiProfilo")
    public ResponseEntity<String> updateDatiProfilo(@RequestBody ProfiloUtentePublicoDTO profiloUtentePublicoDTO) {

        String email = ControllerRestUtil.getEmailOfUtenteCorrente();

        try {
            utenteService.updateDatiProfilo(email, profiloUtentePublicoDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Dati aggiornati\n" + profiloUtentePublicoDTO);
        } catch (ApiException e) {
            String message = e.getMessage();
            HttpStatus status = e.getStatus();
            return ResponseEntity.status(status).body(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


    @PostMapping("utente/modificaProfilo")
    public ResponseEntity<String> salvaImgProfilo(@ModelAttribute ModificaProfiloDTO modificaProfiloDTO) {
        MultipartFile imgFIle = modificaProfiloDTO.immagineProfilo();
        String email = ControllerRestUtil.getEmailOfUtenteCorrente();
        String linkOut="";
        try {
            utenteService.modificaProfilo(email, modificaProfiloDTO);
            String path = "imgProfilo-" + email+".jpg" ;
            linkOut=imageContainerUtil.uploadImage(imgFIle,path) ;
            return ResponseEntity.ok("Immagine salvata con successo: "+linkOut+"\n"+modificaProfiloDTO.toString());



        } catch (ApiException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("utente/isProfiloCompleto")
    public Boolean isProfiloCompleto(){
        String email = ControllerRestUtil.getEmailOfUtenteCorrente();
        return utenteService.isProfiloCompleto(email);
    }






}
