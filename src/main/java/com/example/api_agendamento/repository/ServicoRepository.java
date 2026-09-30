package com.example.api_agendamento.repository;

import com.example.api_agendamento.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

    List<Servico> findByAtivo(boolean ativo);
}