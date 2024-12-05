package api.dieti2024.dto;

import lombok.Builder;

@Builder
public record NotificaDTO(int id,String emailUtente,long dataUnixTimeMilliseconds, String oggettoDellaNotifica,String messaggio, int AstaId, boolean visualizzato) {

}
