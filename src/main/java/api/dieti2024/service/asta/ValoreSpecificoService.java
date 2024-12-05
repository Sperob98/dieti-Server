package api.dieti2024.service.asta;

import api.dieti2024.model.ValoreSpecificoPerProdotto;
import api.dieti2024.repository.ValoreSpecificoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValoreSpecificoService {
    private final ValoreSpecificoRepository valoreSpecificoRepository;

    public ValoreSpecificoService(ValoreSpecificoRepository valoreSpecificoRepository) {
        this.valoreSpecificoRepository = valoreSpecificoRepository;
    }


    public void saveAll(List<ValoreSpecificoPerProdotto> lista) {
        if (lista == null || lista.isEmpty()) {
            return;
        }
        for (ValoreSpecificoPerProdotto nodo : lista) {
            try {
                valoreSpecificoRepository.save(nodo);
            } catch (Exception e) {
                // Blocco catch vuoto: ignora l'eccezione
            }
        }
    }
}
