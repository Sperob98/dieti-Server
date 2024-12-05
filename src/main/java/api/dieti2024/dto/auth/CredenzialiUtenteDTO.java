package api.dieti2024.dto.auth;

import api.dieti2024.model.Utente;

public record CredenzialiUtenteDTO(String email, String password, String metodoDiRegistrazione) {
    private CredenzialiUtenteDTO(Utente utenteModel) {
        this(utenteModel.getEmail(), utenteModel.getPassword(), utenteModel.getMetodoDiRegistrazione());
    }

    public static CredenzialiUtenteDTO fromUserModel(Utente utenteModel) {
        return new CredenzialiUtenteDTO(utenteModel);
    }
}
