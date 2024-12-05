package api.dieti2024.service.asta;

import api.dieti2024.dto.asta.InputAstaDTO;
import api.dieti2024.dto.asta.ricerca.FiltroDto;
import api.dieti2024.dto.asta.ricerca.InfoDatiAstaDTO;
import api.dieti2024.exceptions.ApiException;
import api.dieti2024.model.Asta;
import api.dieti2024.model.DatiAstaInglese;
import api.dieti2024.repository.AstaProdottoRepository;
import api.dieti2024.repository.AstaRepository;
import api.dieti2024.repository.DatiAstaIngleseRepository;
import api.dieti2024.repository.PermessoRepository;
import api.dieti2024.util.CalendarioUtil;
import api.dieti2024.util.ControllerRestUtil;
import api.dieti2024.util.TipoPermesso;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import api.dieti2024.util.Messages;

import java.util.List;

@Service
public class AstaService {

    private final AstaRepository astaRepository;

    private final PermessoRepository permessoRepository;

    private final AstaProdottoRepository astaProdottoRepository;

    private final DatiAstaIngleseRepository datiAstaIngleseRepository;

    public AstaService(AstaRepository astaRepository, PermessoRepository permessoRepository, AstaProdottoRepository astaProdottoRepository, DatiAstaIngleseRepository datiAstaIngleseRepository) {
        this.astaRepository = astaRepository;
        this.permessoRepository = permessoRepository;
        this.astaProdottoRepository = astaProdottoRepository;
        this.datiAstaIngleseRepository = datiAstaIngleseRepository;
    }

    public void checkDatiInputi(InputAstaDTO inputAstaDTO) {

        if (inputAstaDTO == null) throw new ApiException("Dati asta mancanti", HttpStatus.BAD_REQUEST);
        if (!checkPermessoDiCreazione(inputAstaDTO.tipoAsta())) throw new ApiException("Permesso di creazione asta non valido", HttpStatus.FORBIDDEN);

        if (inputAstaDTO.baseAsta() <= 0) throw new ApiException("Base asta non valida", HttpStatus.BAD_REQUEST);

        long dataInizio = inputAstaDTO.dataInizio();
        long dataScadenza = inputAstaDTO.dataScadenza();
        long dataAttuale = CalendarioUtil.ottieniTempoAttuale();
        if (dataInizio<=dataAttuale) dataInizio=dataAttuale+1;
        String testoDataAttuale= CalendarioUtil.convertIntoString(dataAttuale);
        String testoDataInizio= CalendarioUtil.convertIntoString(dataInizio);
        String testoDataScadenza= CalendarioUtil.convertIntoString(dataScadenza);
        String testoErrore= "Data attuale: "+testoDataAttuale+" Data inizio: "+testoDataInizio+" Data scadenza: "+testoDataScadenza;
        if (!(dataInizio>=dataAttuale && dataInizio<dataScadenza)) throw new ApiException("Tempo asta non valido \n"+testoErrore, HttpStatus.BAD_REQUEST);


    }

    private boolean checkPermessoDiCreazione(String tipoAsta) {
        String emailUtente = ControllerRestUtil.getEmailOfUtenteCorrente();
        List<String> permessiUtente = permessoRepository.getPermessiUtente(emailUtente);
        return TipoPermesso.haPermessoDiCreazione(tipoAsta, permessiUtente) ;
    }

    public int salvaAsta (InputAstaDTO inputAstaDTO,int idProdotto){
        String emailUtenteCreatore = ControllerRestUtil.getEmailOfUtenteCorrente();
        Asta asta = inputAstaDTO.toAsta(idProdotto, emailUtenteCreatore);
        return astaRepository.save(asta).getId();
    }

    public List<InfoDatiAstaDTO> getAllAstaProdotto(FiltroDto filtroDto){

        return astaProdottoRepository.findByCustomWhere(filtroDto, filtroDto.elementiPerPagina(), (filtroDto.pagina() - 1) * filtroDto.elementiPerPagina());
    }

    public int getNumeroTotaleDiAstePerRicerca(FiltroDto filtroDto){

        return astaProdottoRepository.numeroTotaleDiAstePerRicerca(filtroDto);
    }

    public Asta getAstaById(int i) {
        return astaRepository.findById(i).orElseThrow(() -> new ApiException(Messages.AUCTION_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public DatiAstaInglese getDatiAstaIngleseById(int id) {
        return datiAstaIngleseRepository.findById(id).orElseThrow(() -> new ApiException(Messages.AUCTION_NOT_FOUND_ENGLISH, HttpStatus.NOT_FOUND));
    }

    public String getIdUtentebyIdAsta(int idAsta) {
        return  astaRepository.findById(idAsta).orElseThrow(() -> new ApiException(Messages.AUCTION_NOT_FOUND, HttpStatus.NOT_FOUND)).getEmailUtenteCreatore();
    }

    public String getTipoAstaById(int idAssta) {
        return astaRepository.findById(idAssta).orElseThrow(() -> new ApiException(Messages.AUCTION_NOT_FOUND, HttpStatus.NOT_FOUND)).getTipoAsta();
    }

    public boolean isScaduta(int idAsta) {
        Asta asta = astaRepository.findById(idAsta).orElseThrow(() -> new ApiException(Messages.AUCTION_NOT_FOUND, HttpStatus.NOT_FOUND));
        return CalendarioUtil.isTempoScaduto(CalendarioUtil.ottieniTempoAttuale(),asta.getDataScadenza());
    }



}

