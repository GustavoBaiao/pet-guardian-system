package com.petguardian.backend.service;

import com.petguardian.backend.model.Usuario;
import com.petguardian.backend.repository.UsuarioRepository;
import com.petguardian.backend.security.JwtUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    // 🔹 Etapa 1 - Login inicial
    public Map<String, Object> iniciarLogin(String email, String senha) throws MessagingException {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();
        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta.");
        }

        // Gera código 2FA (6 dígitos)
        String codigo2FA = String.format("%06d", new Random().nextInt(999999));
        usuario.setCodigo2FA(codigo2FA);
        usuarioRepository.save(usuario);

        // Envia e-mail com código
        emailService.enviarEmail(
                usuario.getEmail(),
                "Código de acesso - PetGuardian",
                "Seu código de verificação é: " + codigo2FA
        );

        Map<String, Object> response = new HashMap<>();
        response.put("requires2FA", true);
        response.put("mensagem", "Código de verificação enviado ao seu e-mail.");
        response.put("codigoTeste", codigo2FA); // 🔹 útil para testes locais
        return response;
    }

    // 🔹 Etapa 2 - Valida 2FA e gera token
    public Map<String, Object> finalizarLogin(String email, String senha, String codigo2FA) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();
        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta.");
        }

        if (!codigo2FA.equals(usuario.getCodigo2FA())) {
            throw new RuntimeException("Código de verificação inválido.");
        }

        // Gera token JWT
        String token = jwtUtil.generateToken(usuario.getEmail());
        usuario.setCodigo2FA(null); // limpa o código após login
        usuarioRepository.save(usuario);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("usuario", usuario);
        return response;
    }
}
