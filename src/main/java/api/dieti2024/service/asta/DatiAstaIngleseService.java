package api.dieti2024.service.asta;

import api.dieti2024.dto.asta.InputAstaDTO;
import api.dieti2024.exceptions.ApiException;
import api.dieti2024.model.DatiAstaInglese;
import api.dieti2024.repository.DatiAstaIngleseRepository;
import api.dieti2024.util.JsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DatiAstaIngleseService {
    final
    DatiAstaIngleseRepository datiAstaIngleseRepository;

    public DatiAstaIngleseService(DatiAstaIngleseRepository datiAstaIngleseRepository) {
        this.datiAstaIngleseRepository = datiAstaIngleseRepository;
    }

    public void salvaDatiAstaInglese(InputAstaDTO inputAstaDTO,int idAsta) {
        DatiAstaInglese datiAstaInglese = getDatiAstaInglese(inputAstaDTO, idAsta);
        datiAstaIngleseRepository.save(datiAstaInglese);
    }

    private static DatiAstaInglese getDatiAstaInglese(InputAstaDTO inputAstaDTO, int idAsta) {
        DatiAstaInglese datiAstaInglese;
        try{
            datiAstaInglese = JsonUtil.fromJson(inputAstaDTO.datiExtraJson(), DatiAstaInglese.class);
        }catch (Exception e){
            throw new ApiException("Dati asta inglese non validi", HttpStatus.BAD_REQUEST);
        }
        datiAstaInglese.setAstaId(idAsta);
        return datiAstaInglese;
    }

    public String getDatiExtraInfoSingolaAsta(int idAssta) {
        DatiAstaInglese datiAstaInglese = datiAstaIngleseRepository.findById(idAssta).orElse(null);
        if(datiAstaInglese==null) throw new ApiException("Dati asta inglese non trovati", HttpStatus.NOT_FOUND);
        return JsonUtil.toJson(datiAstaInglese);
    }
}
