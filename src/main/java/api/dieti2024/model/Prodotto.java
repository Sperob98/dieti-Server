package api.dieti2024.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "prodotto")
public class Prodotto {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome_prodotto")
    private String nome;


    @ElementCollection
    private List<String> immagini;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "categoria")
    private String categoria;
}
