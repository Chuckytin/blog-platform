package com.example.backend.security;

import com.example.backend.services.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro JWT que se ejecuta una vez por request.
 * - Extrae el token JWT que se ejecuta una vez por request.
 * - Valida el token y obtiene los datos del usuario.
 * - Establece la autenticación en el SecurityContext.
 * - Si el token es inválido continúa la request sin autenticar al usuario.
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    /**
     * Procesa cada request HTTP para comprobar si incluye un token JWT válido.
     * Si el token es válido:
     * - Se crea un UsernamePasswordAuthenticationToken.
     * - Se guarda en el SecurityContextHolder.
     * - Se añade el userId al request si el UserDetails lo soporta.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractToken(request);

            if (token != null) {
                UserDetails userDetails = authenticationService.validateToken(token);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                if (userDetails instanceof BlogUserDetails) {
                    request.setAttribute("userId", ((BlogUserDetails) userDetails).getId());
                }
            }
        } catch (Exception e) {
            // not exception, just don't authentication user
            log.warn("Received invalid auth token");
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT del header Authorization sin el prefijo "Bearer "
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
