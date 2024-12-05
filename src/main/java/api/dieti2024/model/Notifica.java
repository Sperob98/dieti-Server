package api.dieti2024.model;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "notifica")
public class Notifica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "messaggio", length = 512)
    private String messaggio;

    @Column(name = "oggetto_messaggio", length = 128)
    private String oggettoMessaggio;

    @Column(name = "utente", length = 256)
    private String utente;

    @Column(name = "asta")
    private int asta;

    @Column(name = "visualizzato", nullable = false)
    private boolean visualizzato = false;


    @Column(name = "dataunixtimemilliseconds", nullable = false)
    private long dataUnixTimeMilliseconds;

}