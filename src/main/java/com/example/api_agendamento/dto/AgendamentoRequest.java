package com.example.api_agendamento.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AgendamentoRequest(
        @NotNull Long clienteId,
        @NotNull Long profissionalId,
        @NotNull Long servicoId,
        @NotNull @Future LocalDateTime inicio) {
}