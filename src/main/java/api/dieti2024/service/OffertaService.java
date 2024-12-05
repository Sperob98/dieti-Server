package api.dieti2024.service;

import api.dieti2024.dto.OffertaDto;
import api.dieti2024.dto.OffertaVincenteDto;
import api.dieti2024.exceptions.ApiException;
import api.dieti2024.model.Asta;
import api.dieti2024.model.DatiAstaInglese;
import api.dieti2024.model.Offerta;
import api.dieti2024.repository.AstaRepository;
import api.dieti2024.repository.OffertaRepository;
import api.dieti2024.repository.PermessoRepository;
import api.dieti2024.service.asta.AstaService;
import api.dieti2024.util.CalendarioUtil;
import api.dieti2024.util.ControllerRestUtil;
import api.dieti2024.util.TipoAsta;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OffertaService {


final
OffertaRepository offertaRepository;

final
AstaService astaService;
final
PermessoRepository permessoRepository;

final
AstaRepository astaRepository;

    public OffertaService(OffertaRepository offertaRepository, AstaService astaService, PermessoRepository permessoRepository, AstaRepository astaRepository) {
        this.offertaRepository = offertaRepository;
        this.astaService = astaService;
        this.permessoRepository = permessoRepository;
        this.astaRepository = astaRepository;
    }

    public Offerta faiOfferta(OffertaDto offertaDto,long tempoOfferta) {
        offertaIsValido(offertaDto,tempoOfferta);
        String emailUtente = ControllerRestUtil.getEmailOfUtenteCorrente();
        Offerta offerta = new Offerta();
        offerta.setPrezzoProposto(offertaDto.prezzoProposto());
        offerta.setEmailUtente(emailUtente);
        offerta.setAstaId(offertaDto.idAsta());
        offerta.setTempoOfferta(tempoOfferta);
        return offertaRepository.save(offerta);

    }

    private void offertaIsValido(OffertaDto offertaDto, long tempoOfferta) {
        Asta asta = astaService.getAstaById(offertaDto.idAsta());
        String tipoAsta = asta.getTipoAsta();
        if(!ControllerRestUtil.hasPermeessoDiFareUnOfferta(tipoAsta))
            throw new ApiException("L'utente non ha il permesso di fare un offerta su "+tipoAsta, HttpStatus.FORBIDDEN);

        if(CalendarioUtil.isTempoScaduto(tempoOfferta,asta.getDataScadenza()))
            throw new ApiException("asta scaduta", HttpStatus.BAD_REQUEST);

        if(offertaDto.prezzoProposto() <=0)
            throw new ApiException("Prezzo offerta inferiore o uguale alla base asta", HttpStatus.BAD_REQUEST);


        double baseAsta = asta.getBaseAsta();
        switch (tipoAsta){
            case TipoAsta.INGLESE:
                if (offertaDto.prezzoProposto()<=baseAsta || offertaDto.prezzoProposto()<=asta.getPrezzoAttuale())
                    throw new ApiException("Prezzo offerta inferiore al prezzo attuale", HttpStatus.BAD_REQUEST);
                DatiAstaInglese datiAstaInglese = astaService.getDatiAstaIngleseById(asta.getId());
                double prezzoOffertaValido=datiAstaInglese.getQuotaFissaPerLaPuntata()+asta.getPrezzoAttuale();
                if ( prezzoOffertaValido!= offertaDto.prezzoProposto())
                    throw new ApiException("Prezzo offerta non valido deve essere di: "+prezzoOffertaValido, HttpStatus.BAD_REQUEST);
                break;
            case TipoAsta.SILENZIOSA:
                if (offertaDto.prezzoProposto()<=baseAsta)
                    throw new ApiException("Errore: in un asta silenziosa non puoi fare un offerta piu' bassa della base d'asta\n fai un offerta minore di "+asta.getBaseAsta(), HttpStatus.BAD_REQUEST);
                break;
            case TipoAsta.APPALTO:
                if (offertaDto.prezzoProposto()>baseAsta || offertaDto.prezzoProposto()>asta.getPrezzoAttuale())
                    throw new ApiException("Errore: in un asta inversa l'offerta deve sempre piu' bassa del prezzo attuale  ",HttpStatus.BAD_REQUEST);
                break;
            default:
                throw new ApiException("Tipo asta non valido", HttpStatus.BAD_REQUEST);
        }


    }

    public Offerta confermaOffertaVincente(OffertaVincenteDto offertaDto) {
        Offerta offerta = offertaRepository.findById(offertaDto.idOfferta()).orElseThrow(()->new ApiException("Offerta non trovata", HttpStatus.NOT_FOUND));
        if(offertaDto.idAsta()!=offerta.getAstaId())
            throw new ApiException("Offerta non è assocciata all'asta che hai dato", HttpStatus.BAD_REQUEST);
        Asta asta = astaRepository.findById(offerta.getAstaId()).orElseThrow(()->new ApiException("asta non trovata", HttpStatus.NOT_FOUND));
        if(!asta.getEmailUtenteCreatore().equals(ControllerRestUtil.getEmailOfUtenteCorrente()))
            throw new ApiException("Non sei il creatore dell'asta", HttpStatus.FORBIDDEN);
        if(!asta.getTipoAsta().equals(TipoAsta.SILENZIOSA))
            throw new ApiException("Puoi confermare il vincitore sole delle aste silenziose", HttpStatus.FORBIDDEN);
        if(!CalendarioUtil.isOltreTempoAttuale(asta.getDataScadenza()))
            throw new ApiException("asta scaduta", HttpStatus.BAD_REQUEST);
        offerta.setOffertaVincente(true);
        return offertaRepository.save(offerta);
    }

    public List<Offerta> getOffertePubliche(int idAsta) {

       List<Offerta> offerte = offertaRepository.getOfferteByIdAsta(idAsta);

        Asta asta = astaService.getAstaById(idAsta);
        if(asta.getTipoAsta().equals(TipoAsta.SILENZIOSA))
        {
            offerte.forEach(offerta -> offerta.setPrezzoProposto(0));
        }

            return offerte;
    }


    public Offerta getOffertaVincente(int idAsta) {
        //if asta è scaduta
        boolean isScaduta = astaService.isScaduta(idAsta);
        if(!isScaduta)
            throw new ApiException("asta ancora non si è conclusa", HttpStatus.BAD_REQUEST);
        List<Offerta> offerte = offertaRepository.findByAstaIdAndOffertaVincenteIsTrue(idAsta);
        if(offerte.isEmpty())
            throw new ApiException("L'asta si è conclusa senza vincitore", HttpStatus.NOT_FOUND);
        //aggiorna  dati vincitore
        offertaRepository.aggiornaDatiVincitore(idAsta,offerte.get(0).getId());
        return offerte.get(0);

    }

    public List<Offerta> getInfoCompleteAstaSilenziosa(int idAssta) {
        Asta asta = astaService.getAstaById(idAssta);
        if (!asta.getTipoAsta().equals(TipoAsta.SILENZIOSA))
            throw new ApiException("Asta non è silenziosa", HttpStatus.BAD_REQUEST);

        String emailUtente = ControllerRestUtil.getEmailOfUtenteCorrente();
        if (!asta.getEmailUtenteCreatore().equalsIgnoreCase(emailUtente) )
            throw new ApiException("Non sei il creatore dell'asta", HttpStatus.FORBIDDEN);
        return offertaRepository.getOfferteByIdAsta(idAssta);
    }

    public List<Offerta> getOfferteEffettuate(int idAsta) {
        return offertaRepository.findByAstaIdAndEmailUtente(idAsta,ControllerRestUtil.getEmailOfUtenteCorrente());
    }
}
