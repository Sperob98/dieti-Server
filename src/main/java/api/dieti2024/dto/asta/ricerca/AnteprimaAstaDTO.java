package api.dieti2024.dto.asta.ricerca;

public record AnteprimaAstaDTO(
                int idProdotto,
                String urlCopertinaImmagine,
                String nomeProdotto,
                String CategoriaPrincipale,
                String TipoAsta,
                double prezzoAttuale,
                long dataScadenzaUnixTime) {
}
