package api.dieti2024.dto.utente;

import api.dieti2024.model.Utente;

public record ProfiloUtentePublicoDTO(String nome, String cognome, String siti, String areaGeografica, String bio,String foto_profilo,Boolean isVenditore) {
    private ProfiloUtentePublicoDTO(Utente utenteModel, boolean isVenditore) {
        this(utenteModel.getNome(), utenteModel.getCognome(), utenteModel.getSiti(), utenteModel.getAreaGeografica(), utenteModel.getBio(),utenteModel.getFotoProfilo(),isVenditore);
    }

    public static ProfiloUtentePublicoDTO fromUserModel(Utente utenteModel,boolean isVenditore) {
        return new ProfiloUtentePublicoDTO(utenteModel , isVenditore);
    }
}
