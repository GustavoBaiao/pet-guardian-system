package com.petguardian.backend.repository;

import com.petguardian.backend.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    // Deleta todos os pets de um tutor pelo ID do tutor
    void deleteByTutorId(Long tutorId);

    // Se ainda não tiver, método para listar pets pelo email do tutor
    List<Pet> findByTutorEmail(String email);
}
