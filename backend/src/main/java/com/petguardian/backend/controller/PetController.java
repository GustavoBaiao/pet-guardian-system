package com.petguardian.backend.controller;

import com.petguardian.backend.model.Pet;
import com.petguardian.backend.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pets")
@CrossOrigin(origins = "*") // caso o front esteja em outro domínio
public class PetController {

    @Autowired
    private PetService petService;

    // 🔹 Listar todos os pets
    @GetMapping
    public ResponseEntity<List<Pet>> listarPets() {
        List<Pet> pets = petService.listarPets();
        return ResponseEntity.ok(pets);
    }

    // 🔹 Listar pets por email do tutor
    @GetMapping("/tutor/{email}")
    public ResponseEntity<List<Pet>> listarPorEmail(@PathVariable String email) {
        List<Pet> pets = petService.listarPetsPorEmail(email);
        return ResponseEntity.ok(pets);
    }

    // 🔹 Buscar pet por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pet> buscarPorId(@PathVariable Long id) {
        Optional<Pet> pet = petService.buscarPorId(id);
        return pet.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Criar novo pet
    @PostMapping
    public ResponseEntity<Pet> criarPet(@RequestBody Pet pet) {
        Pet novoPet = petService.salvarPet(pet);
        return ResponseEntity.ok(novoPet);
    }

    // 🔹 Atualizar pet existente
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

    // 🔹 Deletar todos os pets de um tutor pelo email
    @DeleteMapping("/tutor/{email}")
    public ResponseEntity<?> deletarPetsDoTutor(@PathVariable String email) {
        try {
            petService.deletarPetsDoTutorPorEmail(email);
            return ResponseEntity.ok("Todos os pets do tutor foram deletados com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao deletar pets do tutor: " + e.getMessage());
        }
    }

    @GetMapping("/meus-pets")
    public ResponseEntity<List<Pet>> listarMeusPets(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername(); // email do tutor logado
        List<Pet> pets = petService.listarPetsPorEmail(email);
        return ResponseEntity.ok(pets);
    }
}
