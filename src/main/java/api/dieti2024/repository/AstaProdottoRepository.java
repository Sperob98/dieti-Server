package api.dieti2024.repository;

import api.dieti2024.dto.asta.ricerca.FiltroDto;
import api.dieti2024.dto.asta.ricerca.InfoDatiAstaDTO;
import api.dieti2024.testfiltri.CaratteristicheTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AstaProdottoRepository extends JpaRepository<InfoDatiAstaDTO,Integer> {

    @Query(value = "SELECT * FROM asta_join_prodotto " +
            "WHERE (:#{#filtro.tipoAsta().isEmpty()} OR tipo = ANY( :#{#filtro.tipoAsta().toArray(new String[0])} )) " +
            "AND (:#{#filtro.categoria().equals('tutte')} OR  categoria In (SELECT * FROM TrovaFigliCategoria(:#{#filtro.categoria()}))  ) " +
            "AND (:#{#filtro.nomeProdotto()} IS NULL OR nome_prodotto LIKE CONCAT('%', :#{#filtro.nomeProdotto()}, '%')) " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<InfoDatiAstaDTO> findByCustomWhere(FiltroDto filtro, int limit, int offset);

    @Query(value = "SELECT COUNT(*) FROM asta_join_prodotto " +
            "WHERE (:#{#filtro.tipoAsta().isEmpty()} OR tipo = ANY( :#{#filtro.tipoAsta().toArray(new String[0])} )) " +
            "AND (:#{#filtro.categoria().equals('tutte')} OR  categoria In (SELECT * FROM TrovaFigliCategoria(:#{#filtro.categoria()}))  ) " +
            "AND (:#{#filtro.nomeProdotto()} IS NULL OR nome_prodotto LIKE CONCAT('%', :#{#filtro.nomeProdotto()}, '%')) ",nativeQuery = true)
    int numeroTotaleDiAstePerRicerca(FiltroDto filtro);

    @Query(value = "SELECT * FROM ricerca_prodotti_con_valori_specifici(:caratteristiche)", nativeQuery = true)
    Object searchProductsWithSpecificValues(CaratteristicheTest caratteristiche);


    @Query(value = "SELECT inserisci_immagini_asta(:idAsta, :#{#pathImmaginiSalvate.toArray(new String[0])})", nativeQuery = true)
    void updatePathImmagini( int idAsta, List<String> pathImmaginiSalvate);

    @Query(value = "SELECT id from asta_join_prodotto p where p.id_asta = ?1", nativeQuery = true)
    Integer findIdProdottoByIdAsta(int idAsta);

    List<InfoDatiAstaDTO> findByEmailUtenteCreatore(String emailUtenteCreatore);
    List<InfoDatiAstaDTO> findByEmailUtenteCreatore(String emailUtenteCreatore, Pageable pageable);
    int countByEmailUtenteCreatore(String emailUtenteCreatore);


}
