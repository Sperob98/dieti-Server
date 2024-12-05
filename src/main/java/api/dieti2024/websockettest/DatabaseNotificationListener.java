        package api.dieti2024.websockettest;

import api.dieti2024.exceptions.ApiException;
import api.dieti2024.model.Offerta;
import api.dieti2024.model.Notifica;
import api.dieti2024.util.JsonUtil;
import api.dieti2024.util.WebSocketUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class DatabaseNotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseNotificationListener.class);

    @Autowired
    private DataSource dataSource;

    private PGConnection pgConnection;
    private Connection connection;

    @Autowired
    private WebSocketUtil webSocketUtil;

    private ScheduledExecutorService executorService;

    @PostConstruct
    public void listen() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        try {
            initConnection();
            executorService.scheduleWithFixedDelay(this::processNotifications, 0, 500, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("Errore durante l'inizializzazione della connessione: {}", e.getMessage());
        }
    }

    private void initConnection() throws SQLException {
        connection = dataSource.getConnection();
        pgConnection = connection.unwrap(PGConnection.class);
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("LISTEN offerta_insert_channel");
            stmt.execute("LISTEN notifica_insert_channel");
        }
    }

    public void processNotifications() {
        try {
            PGNotification[] notifications = pgConnection.getNotifications();
            if (notifications != null) {
                for (PGNotification notification : notifications) {
                    processNotification(notification);
                }
            }
        } catch (SQLException e) {
            logger.error("Errore SQL nella lettura delle notifiche, tentativo di riconnessione...");
            reconnect();
        }
    }

    private void processNotification(PGNotification notification) {
        String channel = notification.getName();
        String payload = notification.getParameter();

        try {
            switch (channel) {
                case "offerta_insert_channel":
                    handleOffertaNotification(payload);
                    break;
                case "notifica_insert_channel":
                    handleNotificaNotification(payload);
                    break;
                default:
                    logger.warn("Canale di notifica sconosciuto: {}", channel);
            }
        } catch (ApiException e) {
            logger.error("Errore ApiException: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Errore durante il processamento della notifica: {}", e.getMessage());
        }
    }

    private void handleOffertaNotification(String payload) throws ApiException {
        Offerta offerta = JsonUtil.fromJson(payload, Offerta.class);
        inviaNotificaOfferta(offerta);
    }

    private void handleNotificaNotification(String payload) throws ApiException {
        Notifica notifica = JsonUtil.fromJson(payload, Notifica.class);
        inviaNotificaPersonale(notifica);
    }


    private void reconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
            initConnection();
        } catch (SQLException e) {
            logger.error("Errore durante il tentativo di riconnessione: {}",e.getMessage());
        }
    }

    @PreDestroy
    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("Errore durante la chiusura della connessione: {}", e.getMessage());
        }
    }

    private void inviaNotificaOfferta(Offerta offerta) {
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("message", "Un utente ha fatto un'offerta:");
        messageMap.put("offerta", offerta);

        inviaMessaggioWebSocket(messageMap, "/asta/" + offerta.getAstaId());
    }

    private void inviaNotificaPersonale(Notifica notifica) {
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("message", "Hai una nuova notifica personale:");
        messageMap.put("notifica", notifica);

        inviaMessaggioWebSocket(messageMap, "/notifichePersonali/" + notifica.getUtente());
    }

    private void inviaMessaggioWebSocket(Map<String, Object> messageMap, String destination) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonMessage = objectMapper.writeValueAsString(messageMap);
            webSocketUtil.inviaMessaggio(jsonMessage, destination);
        } catch (JsonProcessingException e) {
            logger.error("Errore nella formattazione del messaggio JSON per WebSocket");
        }
    }
}
