package com.example.api_agendamento.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

public class ConflitoHorarioException extends RuntimeException {

    public ConflitoHorarioException(String mensagem) {
        super(mensagem);
    }
}


