package api.dieti2024.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "permesso")
public class Permesso {

    @Id
    @Column(name = "nome")
    private String nome;

    @Column(name = "descrizione")
    private String descrizione;
}
