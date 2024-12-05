package api.dieti2024.dto.asta.ricerca;

import java.util.List;

public record FiltroCaratteristicheDTO(
        int idCaratteristica,
                List<String> valoriSelezionati
) {
}
