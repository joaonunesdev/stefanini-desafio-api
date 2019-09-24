package com.stefanini.desafio.api.dto;

import com.stefanini.desafio.api.model.Autor;

public class AutorDTO {

    private Long codigo;

    private String nome;

    public AutorDTO(Autor autor){
        codigo = autor.getCodigo();
        nome = autor.getNome();
    }

    public Long getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

}
