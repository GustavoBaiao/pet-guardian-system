package com.petguardian.backend.repository;

import com.petguardian.backend.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying; // ✅ Import necessário
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByUsuarioEmail(String email);

    // ✅ Corrigido: usando @Modifying + @Query explícita
    @Modifying
    @Query("DELETE FROM Pet p WHERE p.usuario.id = :usuarioId")
    void deleteByUsuarioId(@Param("usuarioId") Long usuarioId);
}
