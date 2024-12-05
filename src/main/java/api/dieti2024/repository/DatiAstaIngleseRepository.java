package api.dieti2024.repository;

import api.dieti2024.model.DatiAstaInglese;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatiAstaIngleseRepository extends JpaRepository<DatiAstaInglese, Integer>{
}
