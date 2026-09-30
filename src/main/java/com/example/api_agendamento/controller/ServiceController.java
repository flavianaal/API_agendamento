package com.example.api_agendamento.controller;

import com.example.api_agendamento.dto.ServicoRequest;
import com.example.api_agendamento.entity.Servico;
import com.example.api_agendamento.exception.RecursoNaoEncontradoException;
import com.example.api_agendamento.repository.ServicoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServiceController {

    private final ServicoRepository repo;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Servico criar(@RequestBody @Valid ServicoRequest req) {
        var s = new Servico();
        s.setNome(req.nome());
        s.setDuracaoMinutos(req.duracaoMinutos());
        s.setPreco(req.preco());
        return repo.save(s);
    }

    @GetMapping
    public List<Servico> listar(@RequestParam(required = false) Boolean ativo) {
        return ativo == null ? repo.findAll() : repo.findByAtivo(ativo);
    }

    @GetMapping("/{id}")
    public Servico buscar(@PathVariable Long id) {
        return buscarOuFalhar(id);
    }

    @PutMapping("/{id}")
    public Servico atualizar(@PathVariable Long id, @RequestBody @Valid ServicoRequest req) {
        var s = buscarOuFalhar(id);
        s.setNome(req.nome());
        s.setDuracaoMinutos(req.duracaoMinutos());
        s.setPreco(req.preco());
        return repo.save(s);
    }

    @PatchMapping("/{id}/inativar")
    public Servico inativar(@PathVariable Long id) {
        var s = buscarOuFalhar(id);
        s.setAtivo(false);
        return repo.save(s);
    }

    @PatchMapping("/{id}/ativar")
    public Servico ativar(@PathVariable Long id) {
        var s = buscarOuFalhar(id);
        s.setAtivo(true);
        return repo.save(s);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        repo.delete(buscarOuFalhar(id));
        return ResponseEntity.noContent().build();
    }

    private Servico buscarOuFalhar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado"));
    }
}