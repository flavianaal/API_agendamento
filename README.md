# 💈 API de Agendamento

API REST para gerenciamento de agendamentos de uma barbearia, desenvolvida com **Java 21**, **Spring Boot** e **PostgreSQL**.

O projeto permite cadastrar clientes, profissionais e serviços, e marcar horários com uma regra central: **um profissional não pode ter dois agendamentos sobrepostos**.

## ✨ Funcionalidades

- CRUD completo de **clientes**, **profissionais** e **serviços**
- **Inativação** de registros (sem apagar, preservando o histórico)
- **Agendamentos** com cálculo automático do horário de término pela duração do serviço
- **Bloqueio de conflito de horário** por profissional
- **Cancelar** e **concluir** agendamentos
- Listagem com **filtros** (ativo/inativo, profissional, status)
- Validação dos dados de entrada com mensagens claras
- Tratamento global de erros com códigos HTTP apropriados

## 🛠️ Tecnologias

- Java 21
- Spring Boot 3 (Web, Data JPA, Validation)
- PostgreSQL
- Hibernate / JPA
- Lombok
- Maven
