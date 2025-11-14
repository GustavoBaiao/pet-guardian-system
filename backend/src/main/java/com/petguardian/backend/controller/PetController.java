package com.petguardian.backend.controller;

import com.petguardian.backend.model.Pet;
import com.petguardian.backend.model.Usuario;
import com.petguardian.backend.repository.UsuarioRepository;
import com.petguardian.backend.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/pets")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") // ✅ Ajuste CORS
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 🔹 Método para obter o usuário autenticado via Spring Security
    private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String email = authentication.getName(); // normalmente o username é o email
        return usuarioRepository.findByEmail(email).orElse(null);
    }


    // 🔹 Listar pets do usuário autenticado
    @GetMapping("/meus-pets")
    public ResponseEntity<List<Pet>> listarMeusPets(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("🔔 Entrou no método listarMeusPets");

        if (userDetails == null) {
            System.out.println("⚠️ userDetails está nulo!");
            return ResponseEntity.status(403).build();
        }

        String email = userDetails.getUsername();
        System.out.println("📩 Usuário autenticado: " + email);

        List<Pet> pets = petService.listarPetsPorEmail(email);
        System.out.println("🐶 Pets encontrados: " + pets.size());

        return ResponseEntity.ok(pets);
    }


    // 🔹 Criar novo pet associado ao usuário autenticado
    @PostMapping("/criar-pets")
    public ResponseEntity<Pet> criarPet(@RequestBody Pet pet, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Usuario tutor = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não encontrado"));

        if (tutor.getId() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Usuário não possui ID no banco");
        }

        pet.setUsuario(tutor); // garante que o objeto tutor está gerenciado

        // >>> ADICIONE OS LOGS AQUI <<<
        System.out.println("ID do tutor: " + tutor.getId());
        System.out.println("Pet usuario ID: " + pet.getUsuario().getId());

        Pet novoPet = petService.salvarPet(pet);

        return ResponseEntity.ok(novoPet);
    }


    @DeleteMapping("/delete")
    @Transactional
    public ResponseEntity<?> deleteUserWithPets() {

        Usuario usuario = getUsuarioAutenticado();

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário não autenticado.");
        }

        // Apenas deletar o usuário já apaga os pets por cascade
        usuarioRepository.delete(usuario);

        return ResponseEntity.ok("Usuário e pets deletados com sucesso!");
    }

    // 🔹 Buscar pet por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pet> buscarPorId(@PathVariable Long id) {
        Optional<Pet> pet = petService.buscarPorId(id);
        return pet.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Atualizar pet existente (mantendo vacinas)
    @PutMapping("/{id}")
    public ResponseEntity<Pet> atualizarPet(@PathVariable Long id, @RequestBody Pet pet) {
        pet.setId(id);
        Pet petAtualizado = petService.salvarPet(pet);
        return ResponseEntity.ok(petAtualizado);
    }

    // 🔹 Deletar pet pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarPet(@PathVariable Long id) {
        try {
            petService.deletarPet(id);
            return ResponseEntity.ok("Pet deletado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao deletar pet: " + e.getMessage());
        }
    }

    // 🔹 Deletar todos os pets do usuário autenticado
    @DeleteMapping("/deletar-pets")
    public ResponseEntity<?> deletarMeusPets(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(403).build();
        }

        String email = userDetails.getUsername();
        try {
            petService.deletarPetsDoTutorPorEmail(email);
            return ResponseEntity.ok("Todos os pets do usuário foram deletados com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao deletar pets: " + e.getMessage());
        }
    }
}
