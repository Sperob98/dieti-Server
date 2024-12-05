package api.dieti2024.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @Builder
public class OffertaPubblicaDTO {


    String emailUtente;

    double prezzoProposto;

    long tempoOfferta;

    public OffertaPubblicaDTO(String utente, double prezzoOfferto, long tempo) {
        this.emailUtente = utente;
        this.prezzoProposto = prezzoOfferto;
        this.tempoOfferta = tempo;
    }

}
