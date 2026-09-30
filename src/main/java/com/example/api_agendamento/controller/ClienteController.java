package com.example.api_agendamento.controller;

import com.example.api_agendamento.dto.ClienteRequest;
import com.example.api_agendamento.entity.Cliente;
import com.example.api_agendamento.exception.RecursoNaoEncontradoException;
import com.example.api_agendamento.repository.ClienteRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteRepository repo;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente criar(@RequestBody @Valid ClienteRequest req) {
        var c = new Cliente();
        c.setNome(req.nome());
        c.setEmail(req.email());
        c.setTelefone(req.telefone());
        return repo.save(c);
    }

    // GET /clientes  ou  GET /clientes?ativo=true
    @GetMapping
    public List<Cliente> listar(@RequestParam(required = false) Boolean ativo) {
        return ativo == null ? repo.findAll() : repo.findByAtivo(ativo);
    }

    @GetMapping("/{id}")
    public Cliente buscar(@PathVariable Long id) {
        return buscarOuFalhar(id);
    }

    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable Long id, @RequestBody @Valid ClienteRequest req) {
        var c = buscarOuFalhar(id);
        c.setNome(req.nome());
        c.setEmail(req.email());
        c.setTelefone(req.telefone());
        return repo.save(c);
    }

    @PatchMapping("/{id}/inativar")
    public Cliente inativar(@PathVariable Long id) {
        var c = buscarOuFalhar(id);
        c.setAtivo(false);
        return repo.save(c);
    }

    @PatchMapping("/{id}/ativar")
    public Cliente ativar(@PathVariable Long id) {
        var c = buscarOuFalhar(id);
        c.setAtivo(true);
        return repo.save(c);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        repo.delete(buscarOuFalhar(id));
        return ResponseEntity.noContent().build();
    }

    private Cliente buscarOuFalhar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));
    }
}