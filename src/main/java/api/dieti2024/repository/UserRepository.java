package api.dieti2024.repository;

import api.dieti2024.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Utente,String> {

    @Query("SELECT is_venditore(?1) ")
    boolean isVenditore(String email);

    @Query("SELECT u.password FROM Utente u WHERE u.email = ?1")
    String getPasswordByEmail(String email);

    @Query("SELECT is_profilo_completo(?1)")
    Boolean isProfiloCompleto(String email);
}
