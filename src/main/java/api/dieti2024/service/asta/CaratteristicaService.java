package api.dieti2024.service.asta;

import api.dieti2024.model.Caratteristica;
import api.dieti2024.repository.CaratteristicaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaratteristicaService {

    private final CaratteristicaRepository caratteristicaRepository;

    public CaratteristicaService(CaratteristicaRepository caratteristicaRepository) {
        this.caratteristicaRepository = caratteristicaRepository;
    }

    public List<Caratteristica> getCaratteristicaDaCategoria(String categoria){

        return caratteristicaRepository.getCaratteristicheDaCategoria(categoria);
    }
}
