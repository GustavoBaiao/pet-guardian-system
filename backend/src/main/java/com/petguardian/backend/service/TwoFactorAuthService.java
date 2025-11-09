package com.petguardian.backend.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.stereotype.Service;

@Service
public class TwoFactorAuthService {

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    /**
     * Gera uma nova chave secreta (usada para configurar o 2FA)
     */
    public String generateSecretKey() {
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    /**
     * Verifica se o código informado pelo usuário é válido
     */
    public boolean verifyCode(String secret, int code) {
        return gAuth.authorize(secret, code);
    }

    /**
     * Gera a URL para criar o QR Code no app do Google Authenticator
     */
    public String getQrCodeUrl(String userEmail, String secret) {
        return String.format(
                "otpauth://totp/PetGuardian:%s?secret=%s&issuer=PetGuardian",
                userEmail, secret
        );
    }
}