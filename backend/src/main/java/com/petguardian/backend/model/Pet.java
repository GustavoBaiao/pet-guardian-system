package com.petguardian.backend.model;

import jakarta.persistence.*;

@Entity
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String especie;   // <-- adicione este campo
    private String raca;
    private String vacinas;
    private Integer idade;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario tutor;

    // GETTERS E SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEspecie() { return especie; }  // <-- getter
    public void setEspecie(String especie) { this.especie = especie; } // <-- setter

    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }

    public String getVacinas() { return vacinas; }
    public void setVacinas(String vacinas) { this.vacinas = vacinas; }

    public Integer getIdade() { return idade; }
    public void setIdade(Integer idade) { this.idade = idade; }

    public Usuario getTutor() { return tutor; }
    public void setTutor(Usuario tutor) { this.tutor = tutor; }
}


