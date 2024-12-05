package api.dieti2024.service.asta;

import api.dieti2024.dto.asta.InfoProdottoPerCreazioneDTO;
import api.dieti2024.exceptions.ApiException;
import api.dieti2024.model.Prodotto;
import api.dieti2024.model.ValoreSpecificoPerProdotto;
import api.dieti2024.repository.ProdottoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdottoService {

    private final ProdottoRepository prodottoRepository;
    private final ValoreSpecificoService valoreSpecificoService;

    public ProdottoService(ProdottoRepository prodottoRepository, ValoreSpecificoService valoreSpecificoService) {
        this.prodottoRepository = prodottoRepository;
        this.valoreSpecificoService = valoreSpecificoService;
    }

    public List<Prodotto> getAllPrdotti(){

        return prodottoRepository.findAll();
    }


    public void checkDatiInputi(InfoProdottoPerCreazioneDTO datiDto) {
        if (datiDto == null) {
            throw new ApiException("Dati asta mancanti", HttpStatus.BAD_REQUEST);
        }
    }

    public int salvaProdotto(InfoProdottoPerCreazioneDTO datiDTO) {
        Prodotto prodotto = datiDTO.toProdotto();
        int idProdotto =prodottoRepository.save(prodotto).getId();
        List<ValoreSpecificoPerProdotto> lista= datiDTO.toListValoreSpecificoPerProdotto(idProdotto);
        try{
            valoreSpecificoService.saveAll(lista);
        }catch (Exception e){
            // Blocco catch vuoto: ignora l'eccezione
        }

        return idProdotto;
    }

    public void aggiornaPathImmagini(int idProdotto, List<String> links) {
        Prodotto prodotto = prodottoRepository.findById(idProdotto).orElseThrow(() -> new ApiException("Prodotto non trovato", HttpStatus.NOT_FOUND));
        prodotto.setImmagini(links);
        prodottoRepository.save(prodotto);
    }


}
