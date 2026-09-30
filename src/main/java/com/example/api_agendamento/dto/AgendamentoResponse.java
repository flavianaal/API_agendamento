package com.example.api_agendamento.dto;

import com.example.api_agendamento.entity.StatusAgendamento;

import java.time.LocalDateTime;

public record AgendamentoResponse(
        Long id,
        String cliente,
        String profissional,
        String servico,
        LocalDateTime inicio,
        LocalDateTime fim,
        StatusAgendamento status) {
}