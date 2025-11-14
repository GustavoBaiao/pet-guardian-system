package com.petguardian.backend.controller;

import com.petguardian.backend.model.Pet;
import com.petguardian.backend.model.Usuario;
import com.petguardian.backend.security.JwtUtil;
import com.petguardian.backend.service.PetService;
import com.petguardian.backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // ✅ Corrigido: o front envia para /auth/register
@CrossOrigin(origins = "*")
public class RegisterController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PetService petService;

    @Autowired
    private JwtUtil jwtUtil;

    //@PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody Map<String, Object> request) {
        try {
            System.out.println(">>> Requisição recebida: " + request);

            Usuario usuario = new Usuario();
            usuario.setNome((String) request.get("nome"));
            usuario.setEmail((String) request.get("email"));
            usuario.setSenha((String) request.get("senha"));
            usuario.setAceitouTermos(Boolean.TRUE.equals(request.get("aceitouTermos")));

            Usuario novoUsuario = usuarioService.salvarUsuario(usuario);
            System.out.println(">>> Usuário salvo com ID: " + novoUsuario.getId());

            Pet pet = new Pet();
            pet.setNome((String) request.get("petNome"));
            pet.setEspecie((String) request.get("especie"));
            pet.setRaca((String) request.get("raca"));
            pet.setVacinas((String) request.get("vacinas"));
            pet.setUsuario(novoUsuario);

            System.out.println(">>> Pet antes de salvar: " + pet.getNome() + " / tutor ID " + (pet.getUsuario() != null ? pet.getUsuario().getId() : null));

            petService.salvarPet(pet);
            System.out.println(">>> Pet salvo com sucesso!");

            String token = jwtUtil.generateToken(novoUsuario.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("usuario", novoUsuario);
            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

}
