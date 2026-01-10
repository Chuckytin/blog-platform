package com.example.backend.controllers;

import com.example.backend.domain.dtos.AuthResponse;
import com.example.backend.domain.dtos.LoginRequest;
import com.example.backend.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @Value("${jwt.expired-in}")
    private long expiredIn;

    /**
     * Endpoint para iniciar sesión.
     * - Autentica al usuario y obtiendo sus detalles.
     * - Genera el token JWT basado en esos detalles del usuario.
     * - Construye la respuesta con token y tiempo de expiración.
     */
    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        UserDetails userDetails = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        String tokenValue = authenticationService.generateToken(userDetails);

        AuthResponse authResponse = AuthResponse.builder()
                .token(tokenValue)
                .expiredIn(expiredIn)
                .build();

        return ResponseEntity.ok(authResponse);
    }

}
