package com.petguardian.backend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data // gerar getters e sterrs
@NoArgsConstructor // cria um contrutor vazio
@AllArgsConstructor // cria um construtor com todos atributos
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

    // Relacionamento com pets
    @OneToMany(mappedBy = "dono", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets;

}
