package com.petguardian.backend.service;

import com.petguardian.backend.model.Usuario;
import com.petguardian.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Retorna todos os usuários
    public List<Usuario> listarUsuario() {
        return usuarioRepository.findAll();
    }

    // Busca usuário pelo ID
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Salva ou atualiza usuário
    public Usuario salvarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Deleta usuário pelo ID
    public void deletarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Busca usuário pelo email
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Extrai email do token JWT
    @Autowired
    private com.petguardian.backend.security.JwtUtil jwtUtil;
    public String getEmailFromToken(String token) {
        return jwtUtil.extractUsername(token);
    }
}
