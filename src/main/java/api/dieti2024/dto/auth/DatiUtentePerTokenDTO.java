package api.dieti2024.dto.auth;

import api.dieti2024.model.Utente;

public record DatiUtentePerTokenDTO(String email) {
    private DatiUtentePerTokenDTO(Utente utenteModel) {
        this(utenteModel.getEmail());
    }

    public static DatiUtentePerTokenDTO fromUserModel(Utente utenteModel) {
        return new DatiUtentePerTokenDTO(utenteModel);
    }
}
