package api.dieti2024.repository;

import api.dieti2024.model.ValoreSpecificoPerProdotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  ValoreSpecificoRepository extends JpaRepository<ValoreSpecificoPerProdotto, Integer> {

}
