package com.example.api_agendamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ServicoRequest(
        @NotBlank String nome,
        @NotNull @Positive Integer duracaoMinutos,
        @NotNull @Positive BigDecimal preco) {
}