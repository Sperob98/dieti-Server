package api.dieti2024.testfiltri;


import api.dieti2024.dto.asta.ricerca.FiltroDto;
import api.dieti2024.dto.asta.ricerca.InfoDatiAstaDTO;
import api.dieti2024.repository.AstaProdottoRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class ControllerTest {

    final
    AstaProdottoRepository astaProdottoRepository;

    public ControllerTest(AstaProdottoRepository astaProdottoRepository, ProdottoRepositoryImpl prodottoRepository) {
        this.astaProdottoRepository = astaProdottoRepository;
        this.prodottoRepository = prodottoRepository;
    }

    @PostMapping("public/testfiltro")
    public Object searchProducts(@RequestBody List<CoppiaValoriTest> caratteristiche) {
        CaratteristicheTest caratteristicheWrapper = new CaratteristicheTest(caratteristiche);
        return astaProdottoRepository.searchProductsWithSpecificValues(caratteristicheWrapper);
    }

    final
    ProdottoRepositoryImpl prodottoRepository;
    @PostMapping("public/asta/getAllAste")
    public List< InfoDatiAstaDTO>   lanciaQuery(@RequestBody FiltroDto filtroDto){

        return prodottoRepository.getProdottiAstaConFiltroCompleto( filtroDto);
    }
    //public/asta/getNumeroAste
    @PostMapping("public/asta/getNumeroAste")
    public int getNumeroAste(@RequestBody FiltroDto filtroDto){
        return prodottoRepository.getNumeroProdottiAstaConFiltroCompleto( filtroDto);
    }
}
