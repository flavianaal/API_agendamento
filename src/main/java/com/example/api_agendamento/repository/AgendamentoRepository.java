package com.example.api_agendamento.repository;

import com.example.api_agendamento.entity.Agendamento;
import com.example.api_agendamento.entity.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    boolean existsByProfissionalIdAndStatusAndInicioLessThanAndFimGreaterThan(
            Long profissionalId,
            StatusAgendamento status,
            LocalDateTime fimNovo,
            LocalDateTime inicioNovo);

    @Query("""
            select a from Agendamento a
            where (:profissionalId is null or a.profissional.id = :profissionalId)
              and (:status is null or a.status = :status)
            order by a.inicio
            """)
    List<Agendamento> filtrar(@Param("profissionalId") Long profissionalId,
                              @Param("status") StatusAgendamento status);
}