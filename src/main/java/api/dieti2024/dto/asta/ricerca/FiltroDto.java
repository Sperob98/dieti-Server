package api.dieti2024.dto.asta.ricerca;

import java.util.List;
import java.util.stream.Collectors;

public record FiltroDto(
        String categoria,
        List<String> tipoAsta,
        String nomeProdotto,
        Double prezzoMin,
        Double prezzoMax,
        int pagina,
        int elementiPerPagina,
        List<FiltroCaratteristicheDTO> caratteristicheSelezionate,
        CampoOrdinamento campoOrdinamento,
        DirezioneOrdinamento direzioneOrdinamento
        ){

    public String toWhere() {
        StringBuilder stringBuilder = new StringBuilder();

        // Aggiungi condizioni per categoria, tipoAsta e nomeProdotto solo se non sono
        // nulli o vuoti
        if (categoria != null && !categoria.isEmpty()) {
            stringBuilder.append("categoria = '").append(categoria).append("' AND ");
        }
        // Aggiungi condizioni per tipoAsta solo se non è vuoto
        if (!tipoAsta.isEmpty()) {
            String tipiAstaCondition = tipoAsta.stream()
                    .map(tipo -> "tipo = '" + tipo + "'")
                    .collect(Collectors.joining(" OR "));
            stringBuilder.append("(").append(tipiAstaCondition).append(") AND ");
        }

        if (nomeProdotto != null && !nomeProdotto.isEmpty()) {
            stringBuilder.append("nome_prodotto LIKE '%").append(nomeProdotto).append("%' AND ");
        }

        // Trova l'ultima occorrenza di "AND" e rimuovi l'eventuale "AND" finale
        int lastIndex = stringBuilder.lastIndexOf("AND");
        if (lastIndex != -1) {
            stringBuilder.delete(lastIndex, stringBuilder.length());
        }

        return stringBuilder.toString();
    }

}
