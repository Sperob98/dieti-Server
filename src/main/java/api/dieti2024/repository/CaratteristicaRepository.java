package api.dieti2024.repository;

import api.dieti2024.model.Caratteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaratteristicaRepository extends JpaRepository<Caratteristica, Integer> {

    @Query(value = "SELECT * FROM caratteristica WHERE categoria ILIKE ?1", nativeQuery = true)
    List<Caratteristica> getCaratteristicheDaCategoria(String categoria);
}
