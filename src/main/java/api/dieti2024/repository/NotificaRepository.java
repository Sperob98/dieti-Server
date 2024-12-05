package api.dieti2024.repository;

import api.dieti2024.model.Notifica;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificaRepository extends JpaRepository<Notifica, Integer> {

    int countByVisualizzatoFalseAndUtente(String utente);
    int countByUtente(String utente);
    Page<Notifica> findByUtente(String utente, Pageable pageable);

    Page<Notifica> findByUtenteAndVisualizzatoFalse(String utente, Pageable pageable);
    Optional<Notifica> findByIdAndAndUtente(int id, String utente);
}


