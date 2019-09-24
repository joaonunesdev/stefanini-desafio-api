package com.stefanini.desafio.api.resources;

import com.stefanini.desafio.api.dto.ObraDTO;
import com.stefanini.desafio.api.event.ResourceCreatedEvent;
import com.stefanini.desafio.api.service.exception.ObraIntegrityViolationException;
import com.stefanini.desafio.api.model.Obra;
import com.stefanini.desafio.api.repository.ObraRepository;
import com.stefanini.desafio.api.service.ObraService;
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
@RequestMapping("/obras")
public class ObraResource {

    @Autowired
    private ObraService obraService;

    @Autowired
    private ObraRepository obraRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping("/resumo")
    public ResponseEntity <List<ObraDTO>> findAll(){
        List<Obra> obras = obraService.findAll();
        List<ObraDTO> obrasDTO = obras.stream().map(obj -> new ObraDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok(obrasDTO);
    }

    @GetMapping
    public Page<Obra> findAll(@RequestParam(value = "nome", defaultValue = "") String nome,
                              @RequestParam(value = "descricao", defaultValue = "") String descricao,
                              Pageable pageable) {
        return obraService.findAll(nome, descricao, pageable);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Obra> findById(@PathVariable Long codigo) {
        return ResponseEntity.ok().body(obraService.findById(codigo));
    }

    @PostMapping
    public ResponseEntity<Obra> insert(@RequestBody Obra obra, HttpServletResponse response) throws ObraIntegrityViolationException {
        Obra obraSalva = obraService.insert(obra);
        publisher.publishEvent(new ResourceCreatedEvent(this, response, obraSalva.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(obraSalva);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Obra> update(@PathVariable Long codigo, @Valid @RequestBody Obra obra) throws ObraIntegrityViolationException {
        return ResponseEntity.ok(obraService.update(codigo, obra));
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long codigo) {
        obraService.deleteById(codigo);
    }
}
