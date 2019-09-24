package com.stefanini.desafio.api.service;

import com.stefanini.desafio.api.service.exception.ObraIntegrityViolationException;
import com.stefanini.desafio.api.model.Autor;
import com.stefanini.desafio.api.model.Obra;
import com.stefanini.desafio.api.repository.ObraRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObraService {

    @Autowired
    private ObraRepository obraRepository;

    @Autowired AutorService autorService;

    public Obra insert(Obra obra) {
        validaObra(obra);
        return obraRepository.save(obra);
    }

    public List<Obra> findAll() {
        return obraRepository.findAll();
    }

    public Page<Obra> findAll(String nome, String descricao, Pageable pageable) {
        if (!nome.isEmpty() && !descricao.isEmpty()) {
            return obraRepository.findByNomeIgnoreCaseContainingOrDescricaoIgnoreCaseContaining(nome, descricao, pageable);
        }
        if (!descricao.isEmpty()) {
            return obraRepository.findByDescricaoIgnoreCaseContaining(descricao, pageable);
        }
        return obraRepository.findByNomeIgnoreCaseContaining(nome, pageable);
    }

    public void deleteById(Long codigo) {
        Obra obra = findById(codigo);
        obraRepository.delete(obra);
    }

    public Obra update(Long codigo, Obra obra) {
        validaObra(obra);
        Obra obraSalva = findById(codigo);
        BeanUtils.copyProperties(obra, obraSalva, "codigo");
        return obraRepository.save(obraSalva);
    }

    public Obra findById(Long codigo) {
        return obraRepository.findById(codigo).orElseThrow( () -> new EmptyResultDataAccessException(1));
    }

    private void validaObra(Obra obra) {
        if (obra.getAutores().isEmpty()) {
            throw new ObraIntegrityViolationException("Uma obra deve ter no mínimo um autor");
        }
        if (!isAutoresValidos(obra)){
            throw new ObraIntegrityViolationException("Um ou mais autores inválidos");
        }
        if (!(obra.getDataExposicao() != null ^ obra.getDataPublicacao() != null)){
            throw new ObraIntegrityViolationException("A obra deve ter uma data de exposição ou de publicação mas não ambas");
        }
    }

    private boolean isAutoresValidos(Obra obra) {
        List<Autor> autoresDaObra = obra.getAutores();
        List<Autor> autoresSalvos = autorService.findAll();
        return autoresSalvos.containsAll(autoresDaObra);
    }

}
