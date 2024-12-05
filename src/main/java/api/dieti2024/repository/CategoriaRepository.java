package api.dieti2024.repository;

import api.dieti2024.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, String> {

    //Restituisce tutte le categorie che non hanno padre (Le macro categorie)
    @Query("SELECT nome FROM Categoria WHERE padre IS NULL")
    List<String> getCategorieRadice();

    //Restituisce tutte le categorie figlie della categoria passata come parametro
    @Query("SELECT nome FROM Categoria WHERE padre = ?1")
    List<String> getCategorieFiglie(String categoria);

    //Restituisce il numero di figli della categoria passata come parametro
    @Query("SELECT COUNT(*) FROM Categoria WHERE padre = ?1")
    int getNumeroFigli(String categoria);
}

