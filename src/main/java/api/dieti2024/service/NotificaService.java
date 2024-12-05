package api.dieti2024.service;

import api.dieti2024.dto.NotificaDTO;
import api.dieti2024.model.Notifica;
import api.dieti2024.repository.NotificaRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificaService {

    private final NotificaRepository notificaRepository;

    public NotificaService(NotificaRepository notificaRepository) {
        this.notificaRepository = notificaRepository;
    }

    public int getNumeroNotificheNonVisualizzate(String email) {
        return notificaRepository.countByVisualizzatoFalseAndUtente(email);
    }

    public int getNumeroNotificheTotali(String email) {
        return notificaRepository.countByUtente(email);
    }

   public List<NotificaDTO> getNotificheUtente(String email, int numeroElementi, int numeroPagina) {
       return convertiInNotificheDTO(notificaRepository.findByUtente(email, PageRequest.of(numeroPagina, numeroElementi)));
}
    public List<NotificaDTO> getNotificheNonVisualizzateUtente(String email, int numeroElementi, int numeroPagina) {
        return convertiInNotificheDTO(notificaRepository.findByUtenteAndVisualizzatoFalse(email, PageRequest.of(numeroPagina, numeroElementi)));
    }

    @NotNull
    private List<NotificaDTO> convertiInNotificheDTO(Page<Notifica> listaDiNotifiche) {

        List<NotificaDTO> listaDiNotificheDTO = new ArrayList<>();

        listaDiNotifiche.forEach(notifica -> {
            NotificaDTO notificaDTO = NotificaDTO.builder()
                    .id(notifica.getId())
                    .emailUtente(notifica.getUtente())
                    .dataUnixTimeMilliseconds(notifica.getDataUnixTimeMilliseconds())
                    .oggettoDellaNotifica(notifica.getOggettoMessaggio())
                    .messaggio(notifica.getMessaggio())
                    .AstaId(notifica.getAsta())
                    .visualizzato(notifica.isVisualizzato())
                    .build();
            listaDiNotificheDTO.add(notificaDTO);
        });
        return listaDiNotificheDTO;
    }

public void segnaNotificheVisualizzate(String email, List<Integer> idNotifiche) {
    idNotifiche.forEach(idNotifica -> notificaRepository.findByIdAndAndUtente(idNotifica, email)
            .ifPresent(notifica -> {
                notifica.setVisualizzato(true);
                notificaRepository.save(notifica);
            }));
}
}