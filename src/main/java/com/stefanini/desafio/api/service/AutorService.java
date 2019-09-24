package com.stefanini.desafio.api.service;

import com.stefanini.desafio.api.service.exception.AutorIntegrityViolationException;
import com.stefanini.desafio.api.model.Autor;
import com.stefanini.desafio.api.model.Obra;
import com.stefanini.desafio.api.repository.AutorRepository;
import com.stefanini.desafio.api.repository.ObraRepository;
import com.stefanini.desafio.api.service.exception.ObraIntegrityViolationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private ObraRepository obraRepository;

    public Autor findById(Long codigo) {
        return autorRepository.findById(codigo).orElseThrow( () -> new EmptyResultDataAccessException(1));
    }

    @Transactional
    public Autor insert(Autor autor) {
        validaAutor(autor);
        Autor autorSalvo = autorRepository.save(autor);
        return syncObraAddition(autor, autorSalvo);
    }

    public List<Autor> findAll() {
        return autorRepository.findAll();
    }

    public Page<Autor> findAll(Pageable pageable) {
        return autorRepository.findAll(pageable);
    }

    public void deleteById(Long codigo) {
        Autor autor = findById(codigo);
        if (!autor.getObras().isEmpty()) {
            throw new ObraIntegrityViolationException("Não é possível excluir autores que possuem obras");
        }
        autorRepository.delete(autor);
    }

    @Transactional
    public Autor update(Long codigo, Autor autor) {
        validaAutor(autor);
        Autor autorSalvo = findById(codigo);

        if (autor.getObras() != null && !autor.getObras().containsAll(autorSalvo.getObras())) {
            for (Obra o: autorSalvo.getObras()) {
                if (!autor.getObras().contains(o)) {
                    Obra obj = obraRepository.getOne(o.getCodigo());
                    obj.removeAutor(autor);
                    obraRepository.save(obj);
                }
            }
        }

        BeanUtils.copyProperties(autor, autorSalvo, "codigo");
        autorSalvo = autorRepository.save(autorSalvo);
        return syncObraAddition(autor, autorSalvo);
    }

    private void validaAutor(Autor autor) {
        if ((autor.getPaisOrigem().equalsIgnoreCase("Brasil") || autor.getPaisOrigem().equalsIgnoreCase("Brazil"))
                && (autor.getCpf() == null || autor.getCpf().isEmpty())) {
            throw new AutorIntegrityViolationException("CPF deve ser informado");
        }

        if (autor.getCpf() != null) {
            Autor autorComCpf = autorRepository.findByCpf(autor.getCpf());
            if (autorComCpf != null) {
                if (autor.getCodigo() == null || !autorComCpf.equals(autor)) {
                    throw new AutorIntegrityViolationException("CPF já cadastrado");
                }
            }
        }

        if (autor.getEmail() != null) {
            Autor autorComEmail = autorRepository.findByEmail(autor.getEmail());
            if (autorComEmail != null) {
                if (autor.getCodigo() == null || !autorComEmail.equals(autor)) {
                    throw new AutorIntegrityViolationException("E-mail já cadastrado");
                }
            }
        }
    }

    private Autor syncObraAddition(Autor autor, Autor autorSalvo) {
        if (autor.getObras() != null && !autor.getObras().isEmpty()) {
            for (Obra o : autor.getObras()) {
                Obra obj = obraRepository.getOne(o.getCodigo());
                obj.addAutor(autorSalvo);
                obraRepository.save(obj);
            }
        }
        return autorSalvo;
    }

}
