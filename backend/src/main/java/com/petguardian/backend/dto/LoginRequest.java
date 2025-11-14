package com.petguardian.backend.dto;

public class LoginRequest {
    private String email;
    private String senha;
    private String codigo2FA; // campo opcional para segunda etapa

    // Getters e Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCodigo2FA() {
        return codigo2FA;
    }

    public void setCodigo2FA(String codigo2FA) {
        this.codigo2FA = codigo2FA;
    }
}
