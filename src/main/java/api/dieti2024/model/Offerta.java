package api.dieti2024.model;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "offerta")
public class Offerta {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "tempo_offerta")
    private long tempoOfferta;

    @Column(name = "prezzo_proposto")
    private double prezzoProposto;

    @Column(name = "utente")
    private String emailUtente;

    @Column(name = "asta")
    private int astaId;
    @Column(name = "offerta_vincente",columnDefinition = "boolean default false")
    private boolean offertaVincente;

}
