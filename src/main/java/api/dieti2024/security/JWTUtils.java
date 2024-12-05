package api.dieti2024.security;


import api.dieti2024.dto.auth.DatiUtentePerTokenDTO;
import api.dieti2024.repository.PermessoRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JWTUtils {

    final
    PermessoRepository permessoRepository;
    private final Algorithm hmac512 ;
    private final JWTVerifier verifier;

    public JWTUtils (@Value("${secretKey}") String secretKey, PermessoRepository permessoRepository)  {

        this.hmac512 = Algorithm.HMAC256(secretKey);
        this.verifier = JWT.require(this.hmac512).build();
        this.permessoRepository = permessoRepository;
    }
    private  static  final long EXPIRATION_TIME = 86400000; //24hours or 86400000 milisecs

    public String generateToken(DatiUtentePerTokenDTO datiTokenDTO){
        return JWT.create()
                .withSubject(datiTokenDTO.email())
                .withClaim("permessi" ,permessoRepository.getPermessiUtente(datiTokenDTO.email()))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(hmac512);
    }

    /**
     * Restituisce un nome utente presente nel token.
     * @param token token valido
     * @return nome utente estratto dal token, oppure null se non è possibile estrarlo correttamente
     */
    public String getUsername(String token){
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getSubject();
        } catch (JWTDecodeException e) {
            // In caso di errore durante la decodifica del token
            return null;
        }
    }
    public List<String> getPermessi(String token){
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim("permessi").asList(String.class);
        } catch (JWTDecodeException e) {
            // In caso di errore durante la decodifica del token
            return Collections.emptyList(); // Restituisci lista vuota in caso di errore

        }
    }


    public boolean isTokenValid(final String token){
        try {
             verifier.verify(token);
             return true;
        } catch (final JWTVerificationException verificationEx) {
            return false;
        }
    }



}
