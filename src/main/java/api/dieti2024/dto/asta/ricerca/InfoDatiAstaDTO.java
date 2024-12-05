package api.dieti2024.dto.asta.ricerca;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity(name = "asta_join_prodotto")
public class InfoDatiAstaDTO {

    @Id
    @Column(name = "id")
    int idProdottoAsta;

    @Column(name = "id_asta")
    int idAsta;

    @Column(name = "base_asta")
    double baseAsta;

    @Column(name = "prezzo_attuale")
    double prezzoAttuale;

    @Column(name = "data_scadenza")
    long dataScadenza;

    @Column(name = "data_inizio")
    long dataInizio;

    @Column(name = "tipo")
    String tipoAsta;

    @Column(name = "utente_creatore")
    String emailUtenteCreatore;

    @Column(name = "nome_prodotto")
    String nome;

    @Column(name = "immagini")
    @ElementCollection
    @CollectionTable(name = "prodotto_immagini", joinColumns = @JoinColumn(name = "prodotto_id"))
    List<String> immagini;

    @Column(name = "descrizione")
    String descrizione;

    @Column(name = "categoria")
    String categoria;
}
