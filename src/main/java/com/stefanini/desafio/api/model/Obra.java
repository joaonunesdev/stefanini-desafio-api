package com.stefanini.desafio.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Obra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @NotNull(message = "O nome da obra deve ser informado")
    private String nome;

    @Size(max = 240, message = "A descrição deve conter no máximo 240 caracteres")
    @NotNull(message = "A descrição deve ser informada")
    private String descricao;

    @Column(name = "data_publicacao")
    private LocalDate dataPublicacao;

    @Column(name = "data_exposicao")
    private LocalDate dataExposicao;

    @ManyToMany //(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "obra_autor",
            joinColumns = { @JoinColumn(name = "fk_obra")},
            inverseJoinColumns = { @JoinColumn(name = "fk_autor")})
    @JsonIgnoreProperties("obras")
    private List<Autor> autores = new ArrayList<Autor>();

    public void addAutor(Autor autor) {
        if (!autores.contains(autor)) {
            autores.add(autor);
        }
    }

    public void removeAutor(Autor autor) {
        autores.remove(autor);
        autor.getObras().remove(this);
    }

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public LocalDate getDataExposicao() {
        return dataExposicao;
    }

    public void setDataExposicao(LocalDate dataExposicao) {
        this.dataExposicao = dataExposicao;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Obra obra = (Obra) o;
        return codigo.equals(obra.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
