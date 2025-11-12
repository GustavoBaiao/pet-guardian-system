package com.petguardian.backend.controller;

import com.petguardian.backend.model.Usuario;
import com.petguardian.backend.model.Pet;
import com.petguardian.backend.security.JwtUtil;
import com.petguardian.backend.service.UsuarioService;
import com.petguardian.backend.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/register")
@CrossOrigin(origins = "*")
public class RegisterController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PetService petService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> registrarUsuario(@RequestBody Map<String, Object> request) {
        try {
            // 🔹 Criar usuário
            Usuario usuario = new Usuario();
            usuario.setNome((String) request.get("nome"));
            usuario.setEmail((String) request.get("email"));
            usuario.setSenha((String) request.get("senha"));
            usuario.setAceitouTermos((Boolean) request.get("aceitouTermos"));

            Usuario novoUsuario = usuarioService.salvarUsuario(usuario);

            // 🔹 Criar pet associado ao usuário
            Pet pet = new Pet();
            pet.setNome((String) request.get("petNome"));
            pet.setEspecie((String) request.get("especie"));
            pet.setRaca((String) request.get("raca"));
            pet.setIdade((Integer) request.get("idade"));
            pet.setVacinas((String) request.get("vacinas"));
            pet.setTutor(novoUsuario); // ⚠️ associa o pet ao usuário

            petService.salvarPet(pet);

            // 🔹 Gerar token JWT
            String token = jwtUtil.generateToken(novoUsuario.getEmail());

            // 🔹 Retornar usuário e token no mesmo objeto
            Map<String, Object> response = new HashMap<>();
            response.put("usuario", novoUsuario);
            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }
}
