package com.stefanini.desafio.api.repository;

import com.stefanini.desafio.api.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByCpf(String cpf);
    Autor findByEmail(String email);
}
