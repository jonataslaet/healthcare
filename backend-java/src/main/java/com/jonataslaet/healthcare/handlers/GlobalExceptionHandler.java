package com.jonataslaet.healthcare.handlers;

import com.jonataslaet.healthcare.controllers.dtos.StandardError;
import com.jonataslaet.healthcare.exceptions.DuplicationException;
import com.jonataslaet.healthcare.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tools.jackson.databind.exc.InvalidFormatException;

import java.time.Instant;
import java.time.LocalDate;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<@NonNull StandardError> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException ex, HttpServletRequest request) {
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError("Erro de requisição");
        String message = ex.getMessage();
        if (ex.getCause() instanceof InvalidFormatException invalidFormatException) {
            if (invalidFormatException.getTargetType().equals(LocalDate.class)) {
                message = "birthDate must be in format yyyy-MM-dd";
            } else {
                String fieldName = "unknown";

                if (!invalidFormatException.getPath().isEmpty()) {
                    int lastIndex = invalidFormatException.getPath().size() - 1;
                    fieldName = invalidFormatException.getPath().get(lastIndex).getPropertyName();
                }

                Class<?> targetType = invalidFormatException.getTargetType();
                Object invalidValue = invalidFormatException.getValue();

                message = String.format(
                    "Field '%s' expects type %s but received value '%s'",
                    fieldName,
                    targetType.getSimpleName(),
                    invalidValue
                );
            }
        }
        error.setMessage(message);
        error.setPath(request.getRequestURI());

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<@NonNull StandardError> handleResourceNotFoundException(ResourceNotFoundException ex,
        HttpServletRequest httpServletRequest) {
        StandardError standardError = new StandardError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(HttpStatus.NOT_FOUND.value());
        standardError.setError("Recurso não encontrado");
        standardError.setMessage(ex.getMessage());
        standardError.setPath(httpServletRequest.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(standardError);
    }

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
