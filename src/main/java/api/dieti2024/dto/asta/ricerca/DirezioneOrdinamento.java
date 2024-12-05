package api.dieti2024.dto.asta.ricerca;

public enum DirezioneOrdinamento {
    ASCENDENTE("asc"),
    DISCENDENTE("desc");

    private final String direzione;

    DirezioneOrdinamento(String direzione) {
        this.direzione = direzione;
    }

    public String getDirezione() {
        return direzione;
    }
}
