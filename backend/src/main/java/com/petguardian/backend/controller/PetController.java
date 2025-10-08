package com.petguardian.backend.controller;

import com.petguardian.backend.model.Pet;
import com.petguardian.backend.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pets")
public class PetController {
    @Autowired
    private PetService petService;

    @GetMapping
    public List<Pet> listarPets() {
        return petService.listarPets();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Pet> listarPorUsuario(@PathVariable Long usuarioId) {
        return petService.listarPetsPorUsuario(usuarioId);
    }

    @GetMapping("/{id}")
    public Optional<Pet> buscarPet(@PathVariable Long id) {
        return petService.buscarPorId(id);
    }

    @PostMapping
    public Pet criarPet(@RequestBody Pet pet) {
        return petService.salvarPet(pet);
    }

    @PutMapping("/{id}")
    public Pet atualizarPet(@PathVariable Long id, @RequestBody Pet pet) {
        pet.setId(id);
        return petService.salvarPet(pet);
    }

    @DeleteMapping("/{id}")
    public void deletarPet(@PathVariable Long id) {
        petService.deletarPet(id);
    }
}
