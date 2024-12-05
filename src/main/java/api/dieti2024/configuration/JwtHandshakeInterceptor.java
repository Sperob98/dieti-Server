package api.dieti2024.configuration;


import api.dieti2024.exceptions.ApiException;
import api.dieti2024.security.JWTUtils;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JwtHandshakeInterceptor implements ChannelInterceptor {

    final
    JWTUtils jwtUtils;
    final
    UtentiConnessi utentiConnessi;

    public JwtHandshakeInterceptor(JWTUtils jwtUtils, UtentiConnessi utentiConnessi) {
        this.jwtUtils = jwtUtils;
        this.utentiConnessi = utentiConnessi;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        // Check if the command is SUBSCRIBE
        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            handleSubscribe(headerAccessor);
        }
        // Check if the command is DISCONNECT
        else if (StompCommand.DISCONNECT.equals(headerAccessor.getCommand())) {
            handleDisconnect(headerAccessor);
        }

        // If all checks pass, invoke the superclass method
        return ChannelInterceptor.super.preSend(message, channel);
    }

    private void handleSubscribe(StompHeaderAccessor headerAccessor) {
        if (!headerAccessor.getDestination().contains("notifichePersonali")) {
            return;
        }

        String sessionID = headerAccessor.getSessionId();
        String authorizationHeader = headerAccessor.getFirstNativeHeader("Authorization");

        validateAuthorizationHeader(authorizationHeader);

        String token = extractToken(authorizationHeader);

        validateToken(token);

        validateUserAccess(headerAccessor, token);

        // Add user to the connected users list
        utentiConnessi.aggiungiUtente(jwtUtils.getUsername(token), sessionID);
    }

    private void handleDisconnect(StompHeaderAccessor headerAccessor) {
        utentiConnessi.rimuoviIdSessione(headerAccessor.getSessionId());
    }

    private void validateAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ApiException("Errore nell'intestazione 'Authorization': assicurati di includere un token valido che inizi con 'Bearer ' e che non sia scaduto.", HttpStatus.BAD_REQUEST);
        }
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

    private void validateToken(String token) {
        if (!jwtUtils.isTokenValid(token)) {
            throw new ApiException("Il token fornito non è valido. Controlla se è scaduto o se è stato modificato.", HttpStatus.UNAUTHORIZED);
        }
    }

    private void validateUserAccess(StompHeaderAccessor headerAccessor, String token) {
        String usernameFromDestination = getEmailFromDestination(headerAccessor);
        String usernameFromToken = jwtUtils.getUsername(token);

        if (!usernameFromToken.equalsIgnoreCase(usernameFromDestination)) {
            throw new ApiException("Utente non autorizzato alla sottoscrizione alla destinazione specificata.", HttpStatus.FORBIDDEN);
        }
    }


    private String getEmailFromDestination(StompHeaderAccessor headerAccessor) {
        String input = headerAccessor.getDestination();

        // Espressione regolare per trovare l'email
        String emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group();

        } else {
            return "";
        }
    }


}
