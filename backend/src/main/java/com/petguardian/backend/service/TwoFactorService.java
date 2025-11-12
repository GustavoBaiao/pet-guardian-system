package com.petguardian.backend.service;

import com.petguardian.backend.model.TwoFactorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.petguardian.backend.repository.TwoFactorCodeRepository;


import java.time.LocalDateTime;
import java.util.Random;

@Service
public class TwoFactorService {

    @Autowired
    private TwoFactorCodeRepository repository;

    public String generateCode(String email) {
        repository.deleteByEmail(email);

        String code = String.format("%06d", new Random().nextInt(999999));
        TwoFactorCode twoFactor = new TwoFactorCode();
        twoFactor.setEmail(email);
        twoFactor.setCode(code);
        twoFactor.setExpirationTime(LocalDateTime.now().plusMinutes(5));
        repository.save(twoFactor);

        return code;
    }

    public boolean validateCode(String email, String code) {
        return repository.findByEmail(email)
                .filter(c -> c.getCode().equals(code) && c.getExpirationTime().isAfter(LocalDateTime.now()))
                .isPresent();
    }
}

