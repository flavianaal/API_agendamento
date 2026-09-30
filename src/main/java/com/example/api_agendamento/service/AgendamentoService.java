package com.example.api_agendamento.service;

import com.example.api_agendamento.dto.AgendamentoRequest;
import com.example.api_agendamento.dto.AgendamentoResponse;
import com.example.api_agendamento.entity.Agendamento;
import com.example.api_agendamento.entity.StatusAgendamento;
import com.example.api_agendamento.exception.ConflitoHorarioException;
import com.example.api_agendamento.exception.RecursoNaoEncontradoException;
import com.example.api_agendamento.exception.RegraDeNegocioException;
import com.example.api_agendamento.repository.AgendamentoRepository;
import com.example.api_agendamento.repository.ClienteRepository;
import com.example.api_agendamento.repository.ProfissionalRepository;
import com.example.api_agendamento.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepo;
    private final ClienteRepository clienteRepo;
    private final ProfissionalRepository profissionalRepo;
    private final ServicoRepository servicoRepo;

    @Transactional
    public AgendamentoResponse criar(AgendamentoRequest req) {
        var cliente = clienteRepo.findById(req.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));
        var profissional = profissionalRepo.findById(req.profissionalId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Profissional não encontrado"));
        var servico = servicoRepo.findById(req.servicoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado"));

        if (!cliente.isAtivo()) {
            throw new RegraDeNegocioException("Cliente inativo");
        }
        if (!profissional.isAtivo()) {
            throw new RegraDeNegocioException("Profissional inativo");
        }
        if (!servico.isAtivo()) {
            throw new RegraDeNegocioException("Serviço inativo");
        }

        var inicio = req.inicio();
        var fim = inicio.plusMinutes(servico.getDuracaoMinutos());

        boolean conflito = agendamentoRepo
                .existsByProfissionalIdAndStatusAndInicioLessThanAndFimGreaterThan(
                        profissional.getId(), StatusAgendamento.AGENDADO, fim, inicio);
        if (conflito) {
            throw new ConflitoHorarioException("Profissional já possui agendamento nesse horário");
        }

        var ag = new Agendamento();
        ag.setCliente(cliente);
        ag.setProfissional(profissional);
        ag.setServico(servico);
        ag.setInicio(inicio);
        ag.setFim(fim);
        ag.setStatus(StatusAgendamento.AGENDADO);
        agendamentoRepo.save(ag);

        return toResponse(ag);
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listar(Long profissionalId, StatusAgendamento status) {
        return agendamentoRepo.filtrar(profissionalId, status).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse buscar(Long id) {
        return toResponse(buscarOuFalhar(id));
    }

    @Transactional
    public AgendamentoResponse cancelar(Long id) {
        var ag = buscarOuFalhar(id);
        if (ag.getStatus() != StatusAgendamento.AGENDADO) {
            throw new RegraDeNegocioException("Só é possível cancelar agendamentos com status AGENDADO");
        }
        ag.setStatus(StatusAgendamento.CANCELADO);
        return toResponse(ag);
    }

    @Transactional
    public void excluir(Long id) {
        agendamentoRepo.delete(buscarOuFalhar(id));
    }

    @Transactional
    public AgendamentoResponse concluir(Long id) {
        var ag = buscarOuFalhar(id);
        if (ag.getStatus() != StatusAgendamento.AGENDADO) {
            throw new RegraDeNegocioException("Só é possível concluir agendamentos com status AGENDADO");
        }
        ag.setStatus(StatusAgendamento.CONCLUIDO);
        return toResponse(ag);
    }

    private Agendamento buscarOuFalhar(Long id) {
        return agendamentoRepo.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado"));
    }

    private AgendamentoResponse toResponse(Agendamento ag) {
        return new AgendamentoResponse(
                ag.getId(),
                ag.getCliente().getNome(),
                ag.getProfissional().getNome(),
                ag.getServico().getNome(),
                ag.getInicio(),
                ag.getFim(),
                ag.getStatus());
    }
}