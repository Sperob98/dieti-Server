package api.dieti2024.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "asta")
public class Asta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name = "base_asta")
    double baseAsta;

    @Column(name="prezzo_attuale")
    double prezzoAttuale;

    @Column(name = "data_scadenza")
    long dataScadenza;

    @Column(name = "data_inizio")
    long dataInizio;

    @Column(name = "tipo")
    String tipoAsta;

    @Column(name="id_prodotto")
    int idProdotto;

    @Column(name = "utente_creatore")
    String emailUtenteCreatore;

}
