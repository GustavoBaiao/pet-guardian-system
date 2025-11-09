package com.petguardian.backend.service;

import com.petguardian.backend.model.Pet;
import com.petguardian.backend.repository.PetRepository;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;

    public List<Pet> listarPets() {
        return petRepository.findAll();
    }

    public List<Pet> listarPetsPorUsuario(Long usuarioId) {
        return petRepository.findByDonoId(usuarioId);
    }

    public Optional<Pet> buscarPorId(Long id) {
        return petRepository.findById(id);
    }

    public Pet salvarPet(Pet pet) {
        return petRepository.save(pet);
    }

    public void deletarPet(Long id) {
        petRepository.deleteById(id);
    }

    /**
     * Serviço simples para TOTP (Google Authenticator).
     * - gera secret (base32)
     * - monta URL otpauth:// para QR code
     * - verifica códigos TOTP
     */
    @Service
    public static class TwoFactorAuthService {

        private final GoogleAuthenticator gAuth;

        public TwoFactorAuthService() {
            this.gAuth = new GoogleAuthenticator();
        }

        /**
         * Gera uma nova chave secreta (Base32) para o usuário.
         */
        public String generateSecretKey() {
            GoogleAuthenticatorKey key = gAuth.createCredentials();
            return key.getKey();
        }

        /**
         * Retorna a URL no formato otpauth:// usada por apps (Google Authenticator).
         * O frontend ou cliente pode transformar essa URL em QR Code (ex: usando uma lib JS ou um serviço).
         *
         * Exemplo de retorno:
         * otpauth://totp/PetGuardian:usuario@example.com?secret=JBSWY3DPEHPK3PXP&issuer=PetGuardian&period=30
         */
        public String getQRBarcodeURL(String userEmail, String secret) {
            // issuer e label
            String issuer = "PetGuardian";
            String label = issuer + ":" + userEmail;

            // Otpauth URL padrão
            return String.format(
                    "otpauth://totp/%s?secret=%s&issuer=%s&period=30",
                    urlEncode(label),
                    secret,
                    urlEncode(issuer)
            );
        }

        /**
         * Verifica se o código TOTP informado é válido para o secret.
         * Código esperado: 6 dígitos (ex: 123456).
         */
        public boolean verifyCode(String secret, int code) {
            return gAuth.authorize(secret, code);
        }

        // pequena utilitária para evitar caracteres inválidos na label/issuer
        private String urlEncode(String value) {
            try {
                return java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8.toString())
                        .replace("+", "%20");
            } catch (Exception e) {
                return value;
            }
        }
    }
}
