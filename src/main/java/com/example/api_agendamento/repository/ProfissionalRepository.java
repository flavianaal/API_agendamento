package com.example.api_agendamento.repository;

import com.example.api_agendamento.entity.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    List<Profissional> findByAtivo(boolean ativo);
}