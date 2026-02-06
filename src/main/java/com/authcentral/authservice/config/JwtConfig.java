package com.authcentral.authservice.config;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtConfig {

    // Clé secrète HMAC pour signer le JWT
    private static final String SECRET = "s3cr3tS0uperL0ngPourJWTsAAAAAAAAAAAAAAA"; // 64+ caractères

    @Bean
    public SecretKey jwtSecretKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
}
