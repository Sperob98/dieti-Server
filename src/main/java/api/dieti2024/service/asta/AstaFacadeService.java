package api.dieti2024.service.asta;

import api.dieti2024.dto.asta.CreaAstaDTO;
import api.dieti2024.dto.asta.ImmagineAstaDTO;
import api.dieti2024.dto.asta.InputAstaDTO;
import api.dieti2024.dto.asta.ricerca.InfoDatiAstaDTO;
import api.dieti2024.exceptions.ApiException;
import api.dieti2024.repository.AstaProdottoRepository;
import api.dieti2024.util.ControllerRestUtil;
import api.dieti2024.util.ImageContainerUtil;
import api.dieti2024.util.TipoAsta;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class AstaFacadeService {
    final
    ProdottoService prodottoService;
    final
    AstaService astaService;
    final
    DatiAstaIngleseService datiAstaIngleseService;
    final
    AstaProdottoRepository astaProdottoRepository;
    final
    ImageContainerUtil imageContainerUtil;

    public AstaFacadeService(ProdottoService prodottoService, AstaService astaService, DatiAstaIngleseService datiAstaIngleseService, AstaProdottoRepository astaProdottoRepository, ImageContainerUtil imageContainerUtil) {
        this.prodottoService = prodottoService;
        this.astaService = astaService;
        this.datiAstaIngleseService = datiAstaIngleseService;
        this.astaProdottoRepository = astaProdottoRepository;
        this.imageContainerUtil = imageContainerUtil;
    }

    @Transactional
    public int creaAsta(CreaAstaDTO datiInput) {
            checkDatiInputValidi(datiInput);
            int idProdotto = prodottoService.salvaProdotto(datiInput.datiProdotto());
            int idAsta=astaService.salvaAsta(datiInput.datiAsta(), idProdotto);
            saveDatiExtraAsta(datiInput.datiAsta(),idAsta);
            salvaImmaginiProdotto(datiInput.datiProdotto().immagini(),idAsta);
            return idAsta;
    }

    private void saveDatiExtraAsta(InputAstaDTO inputAstaDTO,int idAsta) {
        if(!TipoAsta.INGLESE.equals(inputAstaDTO.tipoAsta())) return;
        datiAstaIngleseService.salvaDatiAstaInglese(inputAstaDTO, idAsta);
    }

    private void salvaImmaginiProdotto(List<MultipartFile> immagini, int idAsta) {
        //nome utente /idasta/numeroimmagine
        String identificativoUtente = ControllerRestUtil.getEmailOfUtenteCorrente();
        String path = "%s/%d".formatted(identificativoUtente, idAsta);
        List<String> pathImmaginiSalvate = new ArrayList<>();
        int countImmaginiSalvateConSuccesso=0;
        for (MultipartFile multipartFile : immagini) {

            if (uploadImage(multipartFile, path + "/" + countImmaginiSalvateConSuccesso)) {
                countImmaginiSalvateConSuccesso++;
                pathImmaginiSalvate.add(path);
            }
        }
        astaProdottoRepository.updatePathImmagini(idAsta,pathImmaginiSalvate);
    }
    public boolean uploadImage(MultipartFile file, String path) {
        try {
            imageContainerUtil.uploadImage(file,path);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private void checkDatiInputValidi(CreaAstaDTO datiInput) {
        if (datiInput == null) {
            throw new ApiException("Dati input mancanti", HttpStatus.BAD_REQUEST);
        }
        //TODO aggiungere controlli per vedere se utente ha il permesso di creare asta
        astaService.checkDatiInputi(datiInput.datiAsta());
        prodottoService.checkDatiInputi(datiInput.datiProdotto());
    }


    public String aggiornaImmaginiAsta(ImmagineAstaDTO immagineAstaDTO) {
        List<MultipartFile> files = immagineAstaDTO.files();
        int idAsta = immagineAstaDTO.idAsta();
        String identificativoUtente = ControllerRestUtil.getEmailOfUtenteCorrente();
        String propritarioAsta = astaService.getIdUtentebyIdAsta(idAsta);
        if (!identificativoUtente.equals(propritarioAsta)) {
            throw new ApiException("Non hai i permessi per modificare quest'asta", HttpStatus.FORBIDDEN);
        }
        StringBuilder message = new StringBuilder();
        message.append("Immagini caricate con successo! \necco i link:\n");
        List<String> links = new ArrayList<>();
        int counterErroriCaricamentoImg=0;
        for(int i=0;i<files.size();i++){

            try{
            String link;
                link = imageContainerUtil.uploadImage(files.get(i), idAsta+"-"+(i-counterErroriCaricamentoImg)+"."+files.get(i).getOriginalFilename().split("\\.")[1]);
                message.append(link).append("\n");
            links.add(link);
            }catch (Exception e){
                counterErroriCaricamentoImg++;
                message.append("Errore: file non caricato\n");
            }

        }

        Integer idProdotto = astaProdottoRepository.findIdProdottoByIdAsta(idAsta);
        prodottoService.aggiornaPathImmagini(idProdotto,links);
        return message.toString();

    }

    public InfoDatiAstaDTO getInfoSingolaAsta(int idAsta) {
        Integer idProdotto = astaProdottoRepository.findIdProdottoByIdAsta(idAsta);
        return  astaProdottoRepository.findById(idProdotto).orElseThrow(() -> new ApiException("Dettagli asta non trovata", HttpStatus.NOT_FOUND));
    }

    public String getExtraInfoSingolaAsta(int idAssta) {
        //scopri il tipo d'asta
        String tipoAsta = astaService.getTipoAstaById(idAssta);
        if (TipoAsta.INGLESE.equals(tipoAsta)) {
            return datiAstaIngleseService.getDatiExtraInfoSingolaAsta(idAssta);
        }else {
            return "Nessun dato extra per questo tipo di asta";
        }
    }
    public List<InfoDatiAstaDTO> getAsteCreate(String emailUtente, Pageable pageable) {
        if (pageable.isUnpaged()) {
            return astaProdottoRepository.findByEmailUtenteCreatore(emailUtente);
        }
        return astaProdottoRepository.findByEmailUtenteCreatore(emailUtente, pageable);
    }

    public int getNumeroAsteCreate(String emailUtente) {
        return  astaProdottoRepository.countByEmailUtenteCreatore(emailUtente);
    }
}
