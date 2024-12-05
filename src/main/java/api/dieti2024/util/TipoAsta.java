package api.dieti2024.util;

public class TipoAsta {
    // Costruttore privato per impedire l'istanziazione
    private TipoAsta() {
        throw new IllegalStateException("Utility class");
    }
    public static final String INGLESE = "asta_inglese";

    public static final String SILENZIOSA = "asta_silenziosa";

    public static final String APPALTO = "asta_inversa";

}
