package com.petguardian.backend.security;

import com.petguardian.backend.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "chaveSuperSecretaMuitoSegura";

    // Método para gerar token JWT
    public String gerarToken(Usuario usuario) {
        Date agora = new Date();
        Date expiracao = new Date(System.currentTimeMillis() + 30 * 60 * 1000); // 30 min

        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .setIssuedAt(agora)
                .setExpiration(expiracao)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Método para extrair email do token
    public String extrairEmail(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
