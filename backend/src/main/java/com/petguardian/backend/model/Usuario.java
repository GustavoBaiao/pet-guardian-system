package com.petguardian.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data // gera getters e setters
@NoArgsConstructor // cria um construtor vazio
@AllArgsConstructor // cria um construtor com todos os atributos
@Entity // indica que é uma entidade JPA
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private Boolean aceitouTermos;

    @Column(nullable = false)
    private String versaoTermos = "1.0"; // versão da política aceita

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // data de criação do usuário

    // 🔹 Campo para chave secreta do Google Authenticator
    private String secret2FA;

    // 🔹 Indica se o 2FA está ativado
    private Boolean is2FAEnabled = false;

    // Relacionamento com pets
    @OneToMany(mappedBy = "dono", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Pet> pets;
}