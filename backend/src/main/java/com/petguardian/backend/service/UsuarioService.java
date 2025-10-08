package com.petguardian.backend.service;

import com.petguardian.backend.model.Usuario;
import com.petguardian.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<Usuario> listarUsuario() {
        return usuarioRepository.findAll();
    }

    public Usuario salvarUsuario(Usuario usuario) {
        if (usuario.getAceitouTermos() == null || !usuario.getAceitouTermos()) {
            throw new IllegalArgumentException("è nescessario aceitar a Política de Privacidade para se cadastrar.");
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public void deletarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

}
