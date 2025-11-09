package com.petguardian.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "chaveSuperSecretaParaJWTDoPetGuardianComMaisDe32Caracteres";
    private static final long EXPIRATION_TIME = 30 * 60 * 1000; // 30 minutos

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Gera o token JWT
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extrai o nome de usuário (email)
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    // Valida o token
    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Método auxiliar para parsing seguro
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // (opcional) retorna a data de expiração
    public Date extractExpiration(String token) {
        return parseClaims(token).getExpiration();
    }
}