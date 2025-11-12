package com.petguardian.backend.controller;

import com.petguardian.backend.model.Usuario;
import com.petguardian.backend.repository.UsuarioRepository;
import com.petguardian.backend.security.JwtUtil;
import com.petguardian.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String senha = request.get("senha");
        String codigo2FA = request.get("codigo2FA"); // opcional

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 1️⃣ valida email/senha
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, senha));

        // 2️⃣ valida 2FA
        if (usuario.getIs2FAEnabled() != null && usuario.getIs2FAEnabled()) {

            // Se o código ainda não foi enviado
            if (codigo2FA == null) {
                String codigoGerado = String.format("%06d", new Random().nextInt(999999));
                usuario.setSecret2FA(codigoGerado);
                usuarioRepository.save(usuario);
                emailService.send2FACode(usuario.getEmail(), codigoGerado);

                // ⚠️ Log no console do backend para testes
                System.out.println("Código 2FA gerado: " + codigoGerado);

                return ResponseEntity.ok(Map.of(
                        "requires2FA", true,
                        "mensagem", "Código enviado para seu e-mail",
                        "codigoTeste", codigoGerado // <-- opcional, só para dev
                ));
            }
            // Valida código enviado
            if (!codigo2FA.trim().equals(usuario.getSecret2FA())) {
                return ResponseEntity.badRequest().body(Map.of("erro", "Código 2FA inválido"));
            }

            // Código válido: limpa
            usuario.setSecret2FA(null);
            usuarioRepository.save(usuario);
        }

        // 3️⃣ gera JWT só depois de validar 2FA
        String token = jwtUtil.generateToken(usuario.getEmail());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "usuario", usuario.getEmail()
        ));
    }
}
