package api.dieti2024.repository;

import api.dieti2024.dto.asta.ricerca.InfoDatiAstaDTO;
import api.dieti2024.model.Asta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AstaRepository extends JpaRepository<Asta, Integer> {

    @Query(value = "SELECT * FROM asta_join_prodotto", nativeQuery = true)
    List<InfoDatiAstaDTO> getAllAsteDTO();
}
