package com.petguardian.backend.service;

import com.petguardian.backend.model.Usuario;
import com.petguardian.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 🔹 Listar todos os usuários
    public List<Usuario> listarUsuario() {
        return usuarioRepository.findAll();
    }

    // 🔹 Buscar por ID
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // 🔹 Buscar por e-mail (usado pelo /me e pela autenticação)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // 🔹 Criar novo usuário (com verificação dos termos)
    public Usuario salvarUsuario(Usuario usuario) {
        if (usuario.getAceitouTermos() == null || !usuario.getAceitouTermos()) {
            throw new IllegalArgumentException("É necessário aceitar a Política de Privacidade para se cadastrar.");
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    // 🔹 Deletar usuário por ID
    public void deletarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
