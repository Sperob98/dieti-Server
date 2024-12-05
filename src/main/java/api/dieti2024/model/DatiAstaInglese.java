package api.dieti2024.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "dati_asta_inglese")
public class DatiAstaInglese {

    @Column(name = "tempo_estensione")
    private Long tempoEstensione;
    @Column(name = "quota_puntata")
    private double quotaFissaPerLaPuntata;

    @Column(name= "asta")
    @Id
    private int astaId;

}
