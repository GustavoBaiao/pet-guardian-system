package com.petguardian.backend.controller;

import com.petguardian.backend.dto.LoginRequest;
import com.petguardian.backend.model.Pet;
import com.petguardian.backend.model.Usuario;
import com.petguardian.backend.repository.UsuarioRepository;
import com.petguardian.backend.service.PetService;
import com.petguardian.backend.security.CustomUserDetailsService;
import com.petguardian.backend.security.JwtUtil;
import com.petguardian.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PetService petService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private EmailService emailService;

    // -----------------------
    // REGISTRO DE USUÁRIO + PETS
    // -----------------------
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> register(@RequestBody Usuario usuarioRequest) {
        try {
            // Verifica se o e-mail já existe
            if (usuarioRepository.findByEmail(usuarioRequest.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("E-mail já cadastrado!");
            }

            // 🔑 Criptografa a senha
            usuarioRequest.setSenha(userDetailsService.encodePassword(usuarioRequest.getSenha()));

            // 🔑 Garantir bidirecionalidade: cada pet aponta para o usuário
            if (usuarioRequest.getPets() != null) {
                for (Pet pet : usuarioRequest.getPets()) {
                    pet.setUsuario(usuarioRequest);
                }
            }

            // 🔑 Salva o usuário com CascadeType.ALL, os pets serão salvos automaticamente
            Usuario usuarioSalvo = usuarioRepository.save(usuarioRequest);

            // Gera token JWT após o registro
            String token = jwtUtil.generateToken(usuarioSalvo.getEmail());

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Usuário e pets registrados com sucesso!");
            resposta.put("usuario", usuarioSalvo);
            resposta.put("token", token);

            return ResponseEntity.ok(resposta);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao registrar usuário: " + e.getMessage());
        }
    }



    // -----------------------
    // LOGIN (1ª e 2ª etapa - 2FA)
    // -----------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("erro", "Usuário não encontrado"));
        }

        Usuario usuario = usuarioOpt.get();

        // Etapa 1️⃣ → autenticação inicial (gera código 2FA)
        if (request.getCodigo2FA() == null || request.getCodigo2FA().isBlank()) {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
                );

                String codigo = String.format("%06d", new Random().nextInt(999999));
                usuario.setCodigo2FA(codigo + ":" + Instant.now().toEpochMilli());
                usuarioRepository.save(usuario);

                emailService.send2FACode(usuario.getEmail(), codigo);

                Map<String, Object> resposta = new HashMap<>();
                resposta.put("requires2FA", true);
                resposta.put("mensagem", "Código de verificação enviado para o e-mail.");
                resposta.put("codigoTeste", codigo); // ⚠️ útil apenas para testes, remova em produção
                return ResponseEntity.ok(resposta);

            } catch (Exception e) {
                return ResponseEntity.status(401).body(Map.of("erro", "Credenciais inválidas"));
            }
        }

        // Etapa 2️⃣ → validação do código 2FA
        if (usuario.getCodigo2FA() == null) {
            return ResponseEntity.status(400).body(Map.of("erro", "Código 2FA não gerado"));
        }

        String[] partes = usuario.getCodigo2FA().split(":");
        String codigoSalvo = partes[0];
        long geradoEm = Long.parseLong(partes[1]);

        if (Instant.now().toEpochMilli() - geradoEm > 300_000) {
            usuario.setCodigo2FA(null);
            usuarioRepository.save(usuario);
            return ResponseEntity.status(401).body(Map.of("erro", "Código expirado"));
        }

        if (!request.getCodigo2FA().equals(codigoSalvo)) {
            return ResponseEntity.status(401).body(Map.of("erro", "Código inválido"));
        }

        usuario.setCodigo2FA(null);
        usuarioRepository.save(usuario);

        String token = jwtUtil.generateToken(usuario.getEmail());

        Map<String, Object> respostaFinal = new HashMap<>();
        respostaFinal.put("token", token);
        respostaFinal.put("usuario", usuario);

        return ResponseEntity.ok(respostaFinal);
    }
}
