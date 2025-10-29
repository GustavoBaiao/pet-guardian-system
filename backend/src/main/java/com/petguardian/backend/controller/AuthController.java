package com.petguardian.backend.controller;

import com.petguardian.backend.dto.LoginRequest;
import com.petguardian.backend.dto.LoginResponse;
import com.petguardian.backend.model.Usuario;
import com.petguardian.backend.repository.UsuarioRepository;
import com.petguardian.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(request.getEmail());
        if(usuarioOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado");
        }

        Usuario usuario = usuarioOptional.get();

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha Invalida");
        }

        String token = jwtUtil.gerarToken(usuario);

        return ResponseEntity.ok(new LoginResponse(token));


    }
}