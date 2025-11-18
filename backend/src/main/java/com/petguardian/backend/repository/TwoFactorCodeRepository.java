package com.petguardian.backend.repository;

import com.petguardian.backend.model.TwoFactorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TwoFactorCodeRepository extends JpaRepository<TwoFactorCode, Long> {
    Optional<TwoFactorCode> findByEmail(String email);
    void deleteByEmail(String email);
}