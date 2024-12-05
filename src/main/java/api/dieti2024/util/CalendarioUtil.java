package api.dieti2024.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


public class CalendarioUtil {

    // Costruttore privato per impedire l'istanziazione
    private CalendarioUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Verifica se un tempo specifico si trova all'interno di un intervallo di tempo.
     * @param inizioIntervallo il tempo di inizio dell'intervallo
     * @param fineIntervallo il tempo di fine dell'intervallo
     * @return true se il tempo specifico è compreso nell'intervallo, altrimenti false
     */
    public static boolean isTempoCorrenteNellIntervallo(long inizioIntervallo, long fineIntervallo) {
        long tempoCorrente = ottieniTempoAttuale();
        return inizioIntervallo<=tempoCorrente && tempoCorrente < fineIntervallo;
    }


    /**
     * Verifica se un tempo specifico è scaduto rispetto a una data limite.
     * @param tempoInput il tempo da confrontare con la data di scadenza
     * @param dataLimite la data limite da confrontare
     * @return true se il tempo Inserito  è scaduto rispetto alla data limite, altrimenti false
     */
    public static boolean isTempoScaduto(long tempoInput, long dataLimite) {
        return tempoInput > dataLimite;
    }

    /**
     * Ottiene il tempo attuale in millisecondi dal sistema.
     * @return il tempo attuale in millisecondi
     */
    public static long ottieniTempoAttuale() {
        return  Instant.now().toEpochMilli();
    }


    /**
     * Restituisce il tempo in millisecondi di una data
     * @param giorno il giorno
     * @param mese il mese
     * @param anno l'anno
     * @param ora l'ora
     * @param minuti i minuti
     * @return il tempo in millisecondi
     */
    public static long ottieniTempoAttuale(int giorno, int mese, int anno, int ora, int minuti) {
        LocalDateTime dataOra = LocalDateTime.of(anno, mese, giorno, ora, minuti);
        return dataOra.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static boolean isOltreTempoAttuale(long tempo) {
        return tempo > ottieniTempoAttuale();
    }

    public static String convertIntoString(long data) {
        //converti in stringa la data in giorno mese anno ora minuti e secondi e fuso orario
        LocalDateTime dataOra = LocalDateTime.ofInstant(Instant.ofEpochMilli(data), ZoneOffset.UTC);

        return dataOra.toString();
    }
}

