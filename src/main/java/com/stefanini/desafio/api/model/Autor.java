package com.stefanini.desafio.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stefanini.desafio.api.model.enums.Sexo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
//@Table(uniqueConstraints={@UniqueConstraint(columnNames={"email"}), @UniqueConstraint(columnNames={"cpf"})})
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @NotNull(message = "O nome do autor deve ser informado")
    private String nome;

    @Enumerated(value = EnumType.STRING)
    private Sexo sexo;

    @Column(unique = true)
    private String email;

    @NotNull(message = "O campo data de nascimento é obrigatório")
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @NotNull
    @Column(name = "pais_origem")
    private String paisOrigem;

    @Column(unique = true)
    private String cpf;

    @ManyToMany(mappedBy = "autores")
    @JsonIgnoreProperties("autores")
    private List<Obra> obras = new ArrayList<Obra>();

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getPaisOrigem() {
        return paisOrigem;
    }

    public void setPaisOrigem(String paisOrigem) {
        this.paisOrigem = paisOrigem;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<Obra> getObras() {
        return obras;
    }

    public void setObras(List<Obra> obras) {
        this.obras = obras;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autor autor = (Autor) o;
        return codigo.equals(autor.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
