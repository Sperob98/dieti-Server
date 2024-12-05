package api.dieti2024.repository;

import api.dieti2024.model.Permesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PermessoRepository extends JpaRepository<Permesso, String> {

    @Query(value = "SELECT rrp.nome_permesso " +
            "FROM relazione_utente_ruolo rur JOIN relazione_ruolo_permesso rrp ON rur.nome_ruolo = rrp.nome_ruolo " +
            "WHERE rur.nome_utente = :emailUtente",nativeQuery = true)
    List<String> getPermessiUtente(@Param("emailUtente") String emailUtente);
}
