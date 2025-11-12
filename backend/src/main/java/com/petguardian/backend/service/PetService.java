package com.petguardian.backend.service;

import com.petguardian.backend.model.Pet;
import com.petguardian.backend.model.Usuario;
import com.petguardian.backend.repository.PetRepository;
import com.petguardian.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 🔹 Listar todos os pets do sistema (geral)
    public List<Pet> listarPets() {
        return petRepository.findAll();
    }

    // 🔹 Buscar pet por ID
    public Optional<Pet> buscarPorId(Long id) {
        return petRepository.findById(id);
    }

    // 🔹 Salvar ou atualizar pet
    public Pet salvarPet(Pet pet) {
        return petRepository.save(pet);
    }

    // 🔹 Deletar pet específico
    public void deletarPet(Long id) {
        petRepository.deleteById(id);
    }

    // 🔹 Listar pets por e-mail do tutor
    public List<Pet> listarPetsPorEmail(String email) {
        System.out.println("listarPetsPorEmail chamado com email: " + email); // debug
        List<Pet> pets = petRepository.findByTutorEmail(email); // supondo que você tenha esse método
        System.out.println("Pets encontrados: " + pets.size()); // mostra quantos pets foram retornados
        return pets;
    }

    // 🔹 Deletar todos os pets de um tutor (por e-mail)
    public void deletarPetsDoTutorPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Tutor não encontrado com o e-mail: " + email));
        petRepository.deleteByTutorId(usuario.getId());
    }
}
