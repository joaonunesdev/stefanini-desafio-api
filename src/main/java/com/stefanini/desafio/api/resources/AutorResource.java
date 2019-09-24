package com.stefanini.desafio.api.resources;

import com.stefanini.desafio.api.dto.AutorDTO;
import com.stefanini.desafio.api.event.ResourceCreatedEvent;
import com.stefanini.desafio.api.model.Autor;
import com.stefanini.desafio.api.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/autores")
public class AutorResource {

    @Autowired
    private AutorService autorService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping("/resumo")
    public ResponseEntity<List<AutorDTO>> list(){
        List<Autor> autores = autorService.findAll();
        List<AutorDTO> autoresDTO = autores.stream().map(obj -> new AutorDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok(autoresDTO);
    }

    @GetMapping
    public Page<Autor> findAll(Pageable pageable) {
        return autorService.findAll(pageable);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Autor> findById(@PathVariable Long codigo) {
        return ResponseEntity.ok().body(autorService.findById(codigo));
    }

    @PostMapping
    public ResponseEntity<Autor> insert(@RequestBody Autor autor, HttpServletResponse response) {
        Autor autorSalvo = autorService.insert(autor);
        publisher.publishEvent(new ResourceCreatedEvent(this, response, autorSalvo.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(autorSalvo);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Autor> update(@PathVariable Long codigo, @Valid @RequestBody Autor autor) {
        return ResponseEntity.ok(autorService.update(codigo, autor));
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long codigo) {
        autorService.deleteById(codigo);
    }
}
