package api.dieti2024.dto;


import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ModificaProfiloDTO(
    String nome,
    String cognome,
    String indirizzo,
    String bio,
    List<String> sitiSocial,

    MultipartFile immagineProfilo
) {
}
