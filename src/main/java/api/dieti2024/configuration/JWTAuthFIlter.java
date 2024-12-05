package api.dieti2024.configuration;

import api.dieti2024.dto.auth.UserDetailsDto;
import api.dieti2024.security.JWTUtils;
import api.dieti2024.service.UtenteService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


@Component
public class JWTAuthFIlter extends OncePerRequestFilter {
    private final JWTUtils jwtUtils;
    private final UtenteService utenteService;

    public JWTAuthFIlter(JWTUtils jwtUtils, UtenteService utenteService) {
        this.jwtUtils = jwtUtils;
        this.utenteService = utenteService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain chain) throws ServletException, IOException {
        final String token = getToken(request, response, chain);
        if (token == null) return;

        if (isTokenNonVaido( token)){
            chain.doFilter(request, response);
            return;
        }

        final String username = jwtUtils.getUsername(token);
        final List<String> permessi = jwtUtils.getPermessi(token);
        // set user details in  spring security context

        // Carica gli user details
        UserDetailsDto userDetails = utenteService.getUserDetails(username);

        // Crea l'oggetto Authentication
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,permessi , Collections.emptyList());
        // Imposta il dettaglio dell'autenticazione
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Imposta il contesto di sicurezza di Spring
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continua con la catena di filtri
        chain.doFilter(request, response);
    }

    private boolean isTokenNonVaido( String token){
        return !jwtUtils.isTokenValid(token);
    }

    private static String getToken(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // recupero e controllo se esiste la il token jwt nel head della richiesta http
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return null;
        }

        return header.substring(7);
    }


}
