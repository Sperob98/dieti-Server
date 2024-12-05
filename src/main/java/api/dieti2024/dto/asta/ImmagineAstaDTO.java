package api.dieti2024.dto.asta;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ImmagineAstaDTO(
        int idAsta,
        List<MultipartFile> files
) {
}
