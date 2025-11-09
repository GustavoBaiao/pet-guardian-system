package com.petguardian.backend.controller;

import com.petguardian.backend.model.PasswordResetToken;
import com.petguardian.backend.model.Usuario;
import com.petguardian.backend.repository.PasswordResetTokenRepository;
import com.petguardian.backend.repository.UsuarioRepository;
import com.petguardian.backend.security.JwtUtil;
import com.petguardian.backend.service.EmailService;
import com.petguardian.backend.service.TwoFactorAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TwoFactorAuthService twoFactorAuthService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    /**
     * Endpoint para login: autentica o usuário e gera o token JWT
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String senha = request.get("senha");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, senha)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Se o usuário tiver 2FA habilitado, valida o código TOTP
            if (usuario.getSecret2FA() != null) {
                String codigo2FA = request.get("codigo2FA");
                if (codigo2FA == null || !twoFactorAuthService.verifyCode(usuario.getSecret2FA(), Integer.parseInt(codigo2FA))) {
                    return ResponseEntity.badRequest().body(Map.of("erro", "Código 2FA inválido"));
                }
            }

            String token = jwtUtil.generateToken(userDetails.getUsername());

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", token);
            responseBody.put("mensagem", "Login realizado com sucesso!");
            return ResponseEntity.ok(responseBody);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    /**
     * Endpoint para habilitar o 2FA (gera o secret e o QR code)
     */
    @PostMapping("/enable-2fa")
    public ResponseEntity<?> enableTwoFactor(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String secret = twoFactorAuthService.generateSecretKey();
        usuario.setSecret2FA(secret);
        usuarioRepository.save(usuario);

        String qrCodeUrl = twoFactorAuthService.getQrCodeUrl(email, secret);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("mensagem", "2FA habilitado com sucesso!");
        responseBody.put("qrCodeUrl", qrCodeUrl);
        responseBody.put("secret", secret);

        return ResponseEntity.ok(responseBody);
    }

    /**
     * Endpoint simples para validar o token JWT
     */
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        try {
            String username = jwtUtil.extractUsername(token);

            if (jwtUtil.isTokenValid(token)) { // ✅ Agora só um argumento
                return ResponseEntity.ok(Map.of(
                        "valido", true,
                        "usuario", username
                ));
            } else {
                return ResponseEntity.status(401).body(Map.of(
                        "valido", false,
                        "erro", "Token inválido"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of(
                    "valido", false,
                    "erro", e.getMessage()
            ));
        }
    }

    // Solicitar recuperação de senha
    @PostMapping("/recover-password")
    public ResponseEntity<?> recoverPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        if (usuario == null) {
            return ResponseEntity.badRequest().body("E-mail não encontrado");
        }

        // Gera token de redefinição
        String token = java.util.UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUsuario(usuario);
        resetToken.setExpirationDate(LocalDateTime.now().plusMinutes(15));
        passwordResetTokenRepository.save(resetToken);

        String resetLink = "http://localhost:8080/auth/reset-password?token=" + token;
        emailService.enviarEmail(
                usuario.getEmail(),
                "Recuperação de Senha - PetGuardian",
                "Olá, clique no link abaixo para redefinir sua senha:\n" + resetLink
        );

        return ResponseEntity.ok("E-mail de recuperação enviado");
    }

    // Redefinir senha
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String novaSenha = request.get("novaSenha");

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElse(null);
        if (resetToken == null || resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token inválido ou expirado");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setSenha(novaSenha);
        usuarioRepository.save(usuario);

        passwordResetTokenRepository.delete(resetToken); // invalida token após uso

        return ResponseEntity.ok("Senha redefinida com sucesso");
    }
}