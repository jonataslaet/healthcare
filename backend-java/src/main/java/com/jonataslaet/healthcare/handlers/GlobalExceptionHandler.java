package com.jonataslaet.healthcare.handlers;

import com.jonataslaet.healthcare.controllers.dtos.StandardError;
import com.jonataslaet.healthcare.exceptions.DuplicationException;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicationException.class)
    public ResponseEntity<@NonNull StandardError> handleDuplicationException(
        DuplicationException ex, HttpServletRequest httpServletRequest) {
        StandardError standardError = new StandardError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(HttpStatus.CONFLICT.value());
        standardError.setError("Erro de requisição");
        standardError.setMessage(ex.getMessage());
        standardError.setPath(httpServletRequest.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(standardError);
    }
}
