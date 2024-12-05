package api.dieti2024.configuration;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class UtentiConnessi {
    public final ConcurrentMap<String, String> sessioniUtenti;

    public UtentiConnessi() {
        this.sessioniUtenti = new ConcurrentHashMap<>();
    }

    // Aggiunge un utente e il suo ID di sessione
    public void aggiungiUtente(String email, String idSessione) {
        sessioniUtenti.put(idSessione, email);
    }

    // Rimuove un ID di sessione
    public void rimuoviIdSessione(String idSessione) {
        sessioniUtenti.remove(idSessione);
    }

    // Verifica se un utente è connesso (ha sessioni attive)
    public boolean isUtenteConnesso(String email) {
        return sessioniUtenti.containsValue(email);
    }

}