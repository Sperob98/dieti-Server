package api.dieti2024.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "utente")
public class Utente {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cognome")
    private String cognome;

    @Column(name = "siti")
    private String siti;

    @Column(name = "area_geografica")
    private String areaGeografica;

    @Column(name = "bio", length = 256)
    private String bio;
    @Column (name="metodo_di_registrazione")
    private String metodoDiRegistrazione;
    @Column (name="foto_profilo")
    private String fotoProfilo;
}
