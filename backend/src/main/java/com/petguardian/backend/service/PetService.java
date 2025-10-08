package com.petguardian.backend.service;

import com.petguardian.backend.model.Pet;
import com.petguardian.backend.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;

    public List<Pet> listarPets() {
        return petRepository.findAll();
    }

    public List<Pet> listarPetsPorUsuario(Long usuarioId) {
        return petRepository.findByDonoId(usuarioId);
    }

    public Optional<Pet> buscarPorId(Long id) {
        return petRepository.findById(id);
    }

    public Pet salvarPet(Pet pet) {
        return petRepository.save(pet);
    }

    public void deletarPet(Long id) {
        petRepository.deleteById(id);
    }
}
