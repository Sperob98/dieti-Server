package api.dieti2024.dto.auth;

import api.dieti2024.model.Utente;

public record UserDetailsDto(String email, String password) {
    private UserDetailsDto(Utente utenteModel) {
        this(utenteModel.getEmail(), utenteModel.getPassword());
    }

    public static UserDetailsDto fromUserModel(Utente userModel) {
        return new UserDetailsDto(userModel);
    }
}