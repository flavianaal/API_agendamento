package com.example.api_agendamento.controller;

import com.example.api_agendamento.dto.ProfissionalRequest;
import com.example.api_agendamento.entity.Profissional;
import com.example.api_agendamento.exception.RecursoNaoEncontradoException;
import com.example.api_agendamento.repository.ProfissionalRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profissionais")
@RequiredArgsConstructor
public class ProfissionalController {

    private final ProfissionalRepository repo;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Profissional criar(@RequestBody @Valid ProfissionalRequest req) {
        var p = new Profissional();
        p.setNome(req.nome());
        return repo.save(p);
    }

    @GetMapping
    public List<Profissional> listar(@RequestParam(required = false) Boolean ativo) {
        return ativo == null ? repo.findAll() : repo.findByAtivo(ativo);
    }

    @GetMapping("/{id}")
    public Profissional buscar(@PathVariable Long id) {
        return buscarOuFalhar(id);
    }

    @PutMapping("/{id}")
    public Profissional atualizar(@PathVariable Long id, @RequestBody @Valid ProfissionalRequest req) {
        var p = buscarOuFalhar(id);
        p.setNome(req.nome());
        return repo.save(p);
    }

    @PatchMapping("/{id}/inativar")
    public Profissional inativar(@PathVariable Long id) {
        var p = buscarOuFalhar(id);
        p.setAtivo(false);
        return repo.save(p);
    }

    @PatchMapping("/{id}/ativar")
    public Profissional ativar(@PathVariable Long id) {
        var p = buscarOuFalhar(id);
        p.setAtivo(true);
        return repo.save(p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        repo.delete(buscarOuFalhar(id));
        return ResponseEntity.noContent().build();
    }

    private Profissional buscarOuFalhar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Profissional não encontrado"));
    }
}