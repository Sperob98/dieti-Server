package api.dieti2024.dto.asta;

import api.dieti2024.model.Asta;

public record InputAstaDTO(
                double baseAsta,
                long dataScadenza,
                long dataInizio,
                String tipoAsta,
                String datiExtraJson) {
        public Asta toAsta(int idProdotto, String emailUtenteCreatore) {
                Asta asta = new Asta();
                asta.setIdProdotto(idProdotto);
                asta.setEmailUtenteCreatore(emailUtenteCreatore);
                asta.setBaseAsta(baseAsta);
                asta.setPrezzoAttuale(baseAsta);
                asta.setDataScadenza(dataScadenza);
                asta.setDataInizio(dataInizio);
                asta.setTipoAsta(tipoAsta);

                return asta;
        }
}
