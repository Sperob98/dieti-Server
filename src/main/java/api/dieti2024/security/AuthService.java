package api.dieti2024.security;

import api.dieti2024.dto.auth.CredenzialiUtenteDTO;
import api.dieti2024.dto.auth.DatiUtentePerTokenDTO;
import api.dieti2024.exceptions.ApiException;
import api.dieti2024.model.Utente;
import api.dieti2024.repository.UserRepository;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    private final UserRepository utenteRepo;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    private static final String METODO_REGISTRAZIONE_DIETI = "dieti";

    public AuthService(UserRepository utenteRepo, JWTUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.utenteRepo = utenteRepo;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }


    public String registrazione(CredenzialiUtenteDTO credenzialiUtenteDTO) {
        try {
            if (utenteRepo.existsById(credenzialiUtenteDTO.email()) &&
                    credenzialiUtenteDTO.metodoDiRegistrazione().equals(METODO_REGISTRAZIONE_DIETI)) {
                throw new ApiException("Utente già presente", HttpStatus.CONFLICT);
            }

            Utente utenteModel = new Utente();
            utenteModel.setEmail(credenzialiUtenteDTO.email());
            if (credenzialiUtenteDTO.metodoDiRegistrazione().equals(METODO_REGISTRAZIONE_DIETI)) {
                utenteModel.setMetodoDiRegistrazione(METODO_REGISTRAZIONE_DIETI);
                utenteModel.setPassword(passwordEncoder.encode(credenzialiUtenteDTO.password()));
            } else {
                utenteModel.setMetodoDiRegistrazione("auth0");
            }

            utenteRepo.save(utenteModel);

            return jwtUtils.generateToken(DatiUtentePerTokenDTO.fromUserModel(utenteModel));
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("Errore nella registrazione", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *
     * @param credenzialiUtenteDTO credenziali dell'utente
     * @return restituisce il token in caso di successo
     */
    public String login(final CredenzialiUtenteDTO credenzialiUtenteDTO){
        try {

            //se l'utente non è presente nel database lo registra
            if (!utenteRepo.existsById(credenzialiUtenteDTO.email()) && credenzialiUtenteDTO.metodoDiRegistrazione().equals("auth0") ) {
               return registrazione(credenzialiUtenteDTO);
            }
            Utente utenteRecuperatoTramiteEmail = verificaUtente(credenzialiUtenteDTO);

            DatiUtentePerTokenDTO datiUtentePerTokenDTO = DatiUtentePerTokenDTO.fromUserModel(utenteRecuperatoTramiteEmail);
            return jwtUtils.generateToken(datiUtentePerTokenDTO);
        }catch(ApiException e){
            throw e;
        }catch (Exception e) {
            throw new ApiException("Utente non trovato", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Verifica l'utente abbia inserito le credenziali corrette
     * @param credenzialiUtenteDTO credenziali dell'utente
     * @return restituisce l'utente in caso di successo
     * @throws ApiException se le credenziali non sono corrette
     */
    private Utente verificaUtente(CredenzialiUtenteDTO credenzialiUtenteDTO) {
        String email = credenzialiUtenteDTO.email();
        Utente utenteRecuperatoTramiteEmail= utenteRepo.findById(email).orElseThrow();

        if(credenzialiUtenteDTO.metodoDiRegistrazione().equals(METODO_REGISTRAZIONE_DIETI)){
            matchPassword(
                    credenzialiUtenteDTO.password(),
                    utenteRecuperatoTramiteEmail.getPassword()
            );
        }else{
            verificaTokenAuth0(credenzialiUtenteDTO);
        }
        return utenteRecuperatoTramiteEmail;
    }

    private void verificaTokenAuth0(CredenzialiUtenteDTO credenzialiUtenteDTO) {
        String tokenDiAccessoAuth0 = credenzialiUtenteDTO.password();

        try{
            // Configura il provider JWK
            String issuer = "https://dev-bmqxc24leqwhyhec.eu.auth0.com/";
            JwkProvider provider = new JwkProviderBuilder(issuer)
                    .cached(10, 24, TimeUnit.HOURS) // Cache locale delle chiavi pubbliche
                    .build();
            // Decodifica il token (senza verifica della firma)
            DecodedJWT jwt = JWT.decode(tokenDiAccessoAuth0);

            // Recupera la chiave pubblica associata
            RSAPublicKey publicKey = (RSAPublicKey) provider.get(jwt.getKeyId()).getPublicKey();

            // Verifica la firma del token
            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
            algorithm.verify(jwt);

            // Controlla se il token è scaduto
            if (jwt.getExpiresAt() != null && jwt.getExpiresAt().getTime() < System.currentTimeMillis()) {

                throw new ApiException("Token scaduto",HttpStatus.UNAUTHORIZED);
            }

            // Recupera il claim "email"
            String email = jwt.getClaim("email").asString();

            if(!email.equals(credenzialiUtenteDTO.email())){

                throw new ApiException("Email non coincide con l'e-mail del token, tentativo di furto accoutn rilevato!",HttpStatus.UNAUTHORIZED);
            }

        } catch (JWTVerificationException e){

            throw new ApiException("Firma non valida, token manipolato",HttpStatus.UNAUTHORIZED);

        } catch (JwkException e){

            throw new ApiException("Errore durante il recupero della chiave pubblica associata",HttpStatus.UNAUTHORIZED);
        }
    }


    /**
         * Verifica che la password ricevuta corrisponda a quella salvata nel database
         * @param passwordRicevuta password ricevuta
         * @param passworSalvataNelDatabase password salvata nel database
         * @throws ApiException se la password non corrisponde
         **/
    private void matchPassword(String passwordRicevuta, String passworSalvataNelDatabase) {
        if (!passwordEncoder.matches(passwordRicevuta, passworSalvataNelDatabase)){
                throw new ApiException("Password errata", HttpStatus.UNAUTHORIZED);
        }
    }



}
