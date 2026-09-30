package com.example.api_agendamento.repository;

import com.example.api_agendamento.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByAtivo(boolean ativo);
}