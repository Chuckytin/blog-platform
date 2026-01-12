package com.example.backend.config;

import com.example.backend.domain.entities.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.security.BlogUserDetailsService;
import com.example.backend.security.JwtAuthenticationFilter;
import com.example.backend.services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración principal de Spring Security.
 * - Beans para la autenticación (AuthenticationManager, PasswordEncoder).
 * - UserDetailsService personalizado.
 * - Filtro JWT y su integración en la cadena de filtros.
 * - Reglas de acceso a los endpoints.
 */
@Configuration
public class SecurityConfig {

    /**
     * Filtro JWT encargado de interceptar cada request y validar el token de autenticación.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationService authenticationService) {
        return new JwtAuthenticationFilter(authenticationService);
    }

    /**
     * Implementación de UserDetailsService basada en la BBDD para cargar usuarios por email.
     * - Crea un usuario de prueba si no existe (DEV)
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        BlogUserDetailsService blogUserDetailsService = new BlogUserDetailsService(userRepository);

        String name = "Test User";
        String email = "user@gmail.com";
        String password = "12345678a!";

        userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .name(name)
                    .email(email)
                    .password(passwordEncoder().encode(password))
                    .build();
            return userRepository.save(newUser);
        });

        return blogUserDetailsService;
    }

    /**
     * Configura la cadena de filtros de seguridad HTTP.
     * Define que endpoints son públicos y cuáles requieren autenticación,
     * desactiva CSRF (porque usamos JWT)
     * y la sesión es STATELESS - cada request es independiente.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/tags/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Codifica y verifica las contraseñas.
     * Se usa tanto al registrar usuarios como al autenticar.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Permite autenticar a los usuarios mediante el username y el password en login.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
