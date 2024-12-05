package api.dieti2024.dto.asta.ricerca;

public enum CampoOrdinamento {
NOME_PRODOTTO("nome_prodotto"),
    PREZZO_ATTUALE("prezzo_attuale"),
    DATA_INIZIO("data_inizio");
    private final String campo;

    CampoOrdinamento(String campo) {
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }

}
