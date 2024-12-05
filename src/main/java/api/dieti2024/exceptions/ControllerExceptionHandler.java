package api.dieti2024.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<MessaggioDiErrore> gestisciEccezioneDiApiException(ApiException e, WebRequest request) {
        MessaggioDiErrore message = new MessaggioDiErrore(
                e.getStatus().value(),
                new Date(),
                e.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(message, e.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessaggioDiErrore> gestisciEccezioneGlobale(Exception e, WebRequest request) {
        MessaggioDiErrore message = new MessaggioDiErrore(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                e.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}