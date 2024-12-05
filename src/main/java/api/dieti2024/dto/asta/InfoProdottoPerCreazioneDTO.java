package api.dieti2024.dto.asta;

import api.dieti2024.model.Prodotto;
import api.dieti2024.model.ValoreSpecificoPerProdotto;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public record InfoProdottoPerCreazioneDTO(
        String nomeProdotto,
        String descrizioneProdotto,
        List<MultipartFile> immagini,
        String categoriaProdotto,
        List<CoppiaCaratteristicaValoreDTO> caratteristicheProdotto) {
    public Prodotto toProdotto() {
        Prodotto prodotto = new Prodotto();
        prodotto.setNome(nomeProdotto);
        prodotto.setDescrizione(descrizioneProdotto);
        prodotto.setImmagini(immagini.stream().map(MultipartFile::getOriginalFilename).toList());
        prodotto.setCategoria(categoriaProdotto);
        return prodotto;
    }

    public List<ValoreSpecificoPerProdotto> toListValoreSpecificoPerProdotto(int idProdotto) {
        ArrayList<ValoreSpecificoPerProdotto> lista = new ArrayList<>();
        for (CoppiaCaratteristicaValoreDTO coppia : caratteristicheProdotto) {
            ValoreSpecificoPerProdotto valore = new ValoreSpecificoPerProdotto();
            valore.setIdProdotto(idProdotto);
            valore.setIdCaratteristica(coppia.idCaratteristica());
            valore.setValore(coppia.valore());
            lista.add(valore);
        }
        return lista;
    }
}
