package com.stefanini.desafio.api.repository;

import com.stefanini.desafio.api.model.Obra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObraRepository extends JpaRepository<Obra, Long> {

    Page<Obra> findByNomeIgnoreCaseContainingOrDescricaoIgnoreCaseContaining(String nome, String descricao, Pageable pageRequest);
    Page<Obra> findByNomeIgnoreCaseContaining(String nome, Pageable pageRequest);
    Page<Obra> findByDescricaoIgnoreCaseContaining(String descricao, Pageable pageRequest);

}
