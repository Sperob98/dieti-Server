package api.dieti2024.controller;

import api.dieti2024.model.Caratteristica;
import api.dieti2024.service.asta.CaratteristicaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/public")
public class CaratteristicaController {

    private final CaratteristicaService caratteristicaService;

    public CaratteristicaController(CaratteristicaService caratteristicaService) {
        this.caratteristicaService = caratteristicaService;
    }

    @GetMapping("/getCaratteristicheDaCategoria")
    public List<Caratteristica> getCaratteristicheDaCategoria(@RequestParam("categoria") String categoria) {

        return caratteristicaService.getCaratteristicaDaCategoria(categoria);
    }
}
