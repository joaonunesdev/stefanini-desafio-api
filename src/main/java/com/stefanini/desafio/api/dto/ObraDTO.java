package com.stefanini.desafio.api.dto;

import com.stefanini.desafio.api.model.Obra;

public class ObraDTO {

    private Long codigo;

    private String nome;

    public ObraDTO(Obra obra) {
       codigo = obra.getCodigo();
       nome = obra.getNome();
    }

    public Long getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }
}
