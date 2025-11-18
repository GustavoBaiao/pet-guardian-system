package com.petguardian.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;

    private Boolean aceitouTermos = false;

    private String codigo2FA;
    private String tokenEmail;
    private LocalDateTime validadeTokenEmail;
    private Boolean emailConfirmado = false;

    // ✅ Corrigido: mappedBy = "usuario"
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonManagedReference
    private List<Pet> pets = new ArrayList<>();

    // === Getters e Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Boolean getAceitouTermos() { return aceitouTermos; }
    public void setAceitouTermos(Boolean aceitouTermos) { this.aceitouTermos = aceitouTermos; }

    public String getCodigo2FA() { return codigo2FA; }
    public void setCodigo2FA(String codigo2FA) { this.codigo2FA = codigo2FA; }

    public String getTokenEmail() { return tokenEmail; }
    public void setTokenEmail(String tokenEmail) { this.tokenEmail = tokenEmail; }

    public LocalDateTime getValidadeTokenEmail() { return validadeTokenEmail; }
    public void setValidadeTokenEmail(LocalDateTime validadeTokenEmail) { this.validadeTokenEmail = validadeTokenEmail; }

    public Boolean getEmailConfirmado() { return emailConfirmado; }
    public void setEmailConfirmado(Boolean emailConfirmado) { this.emailConfirmado = emailConfirmado; }

    public List<Pet> getPets() { return pets; }
    public void setPets(List<Pet> pets) { this.pets = pets; }
}
