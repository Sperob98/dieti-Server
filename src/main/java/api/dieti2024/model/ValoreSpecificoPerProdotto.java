package api.dieti2024.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "valore_specifico_di_un_prodotto")
public class ValoreSpecificoPerProdotto {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

@Column(name = "id_prodotto")
    int idProdotto;
@Column(name = "id_caratteristica")
    int idCaratteristica;

    @Column(name = "valore")
    String valore;

}
