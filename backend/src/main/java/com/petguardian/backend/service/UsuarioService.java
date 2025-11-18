package com.petguardian.backend.service;

import com.petguardian.backend.model.Usuario;
import com.petguardian.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Salva usuário (usado pelo RegisterController)
    public Usuario salvarUsuario(Usuario usuario) {
        // Criptografa a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    // Verifica se o e-mail já existe
    public boolean emailExiste(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    // Cria usuário e gera token de e-mail (usado na confirmação)
    public Usuario registrarUsuario(String email, String senha) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));
        gerarTokenEmail(usuario);
        return usuarioRepository.save(usuario);
    }

    // Gera token aleatório de confirmação de e-mail
    public void gerarTokenEmail(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        usuario.setTokenEmail(token);
        usuario.setValidadeTokenEmail(LocalDateTime.now().plusMinutes(10)); // token válido por 10 min
    }

    // Valida token enviado pelo usuário
    public boolean confirmarEmail(String email, String token) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email);
        if (optionalUsuario.isEmpty()) return false;

        Usuario usuario = optionalUsuario.get();

        if (usuario.getTokenEmail() == null || !usuario.getTokenEmail().equals(token)) return false;
        if (usuario.getValidadeTokenEmail().isBefore(LocalDateTime.now())) return false;

        usuario.setEmailConfirmado(true);
        usuario.setTokenEmail(null);
        usuario.setValidadeTokenEmail(null);

        usuarioRepository.save(usuario);
        return true;
    }

    // Busca usuário por e-mail
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}
