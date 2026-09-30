
package com.example.api_agendamento.dto;

import jakarta.validation.constraints.NotBlank;

public record ProfissionalRequest(
        @NotBlank String nome) {
}