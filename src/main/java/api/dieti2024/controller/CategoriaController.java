package api.dieti2024.controller;

import api.dieti2024.dto.CategoriaDTO;
import api.dieti2024.service.CategoriaService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/public")
public class CategoriaController {

    final
    CategoriaService categoriaServizio;

    public CategoriaController(CategoriaService categoriaServizio) {
        this.categoriaServizio = categoriaServizio;
    }

    @GetMapping("/getGerarchiaCategorie")
    public List<CategoriaDTO> getGerarchiaCategorie() {

        return categoriaServizio.costruisciGerarchiaCategorie();
    }
}
