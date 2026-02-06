package com.authcentral.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.authcentral.authservice.security.JwtAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable()) // API REST → pas de CSRF (indispensable pour tester a l'aide de Postman)
            .formLogin(form -> form.disable()) // Pas de login HTML
            .httpBasic(basic -> basic.disable())
            // Pas de session côté serveur   
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // AJOUT DU FILTRE JWT
            .addFilterBefore(jwtAuthenticationFilter, 
                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)

            .authorizeHttpRequests(auth -> auth
                // Endpoint public : inscription des apps clientes
                .requestMatchers(
                        "/api/clients/register",
                        "/api/auth/token"
                ).permitAll()

                // Tout le reste est protégé
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
  