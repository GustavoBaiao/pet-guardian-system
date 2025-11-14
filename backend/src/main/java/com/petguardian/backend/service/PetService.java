package com.petguardian.backend.service;

import com.petguardian.backend.model.Pet;
import com.petguardian.backend.model.Usuario;
import com.petguardian.backend.repository.PetRepository;
import com.petguardian.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ✅ Import necessário

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 🔹 Listar todos os pets
    public List<Pet> listarPets() {
        return petRepository.findAll();
    }

    // 🔹 Buscar pet por ID
    public Optional<Pet> buscarPorId(Long id) {
        return petRepository.findById(id);
    }

    // 🔹 Salvar ou atualizar pet
    @Transactional
    public Pet salvarPet(Pet pet) {
        if (pet.getUsuario() == null || pet.getUsuario().getId() == null) {
            throw new RuntimeException("Usuário não está definido para este Pet");
        }

        // Garantir que o usuário está gerenciado pelo JPA
        Usuario usuarioGerenciado = usuarioRepository.findById(pet.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado no banco"));
        pet.setUsuario(usuarioGerenciado);

        return petRepository.save(pet);
    }

    // 🔹 Deletar pet específico
    public void deletarPet(Long id) {
        petRepository.deleteById(id);
    }

    // 🔹 Listar pets por e-mail do tutor
    public List<Pet> listarPetsPorEmail(String email) {
        return petRepository.findByUsuarioEmail(email);
    }

    // 🔹 Deletar todos os pets do tutor pelo e-mail
    @Transactional // ✅ Garante que a exclusão ocorra dentro de uma transação
    public void deletarPetsDoTutorPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Tutor não encontrado com o e-mail: " + email));
        petRepository.deleteByUsuarioId(usuario.getId());
    }

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }
}
