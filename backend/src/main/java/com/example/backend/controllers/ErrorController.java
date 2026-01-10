package com.example.backend.controllers;

import com.example.backend.domain.dtos.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * Global Error Handler
 * Intercepta la excepciones lanzadas por cualquier Rest Controller
 * y las convierte en error response estandarizados.
 */
@RestController
@ControllerAdvice
@Slf4j
public class ErrorController {

    /**
     * Maneja cualquier excepción de la app que no esté controlado y devuelve un error genérico 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {
        log.error("Caught exception", ex);
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja cualquier IllegalArgumentException y devuelve error 400 con el mensaje de la excepción.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse>handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja cualquier IllegalStateException y devuelve error 409 con el mensaje de la excepción.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse>handleIllegalStateException(IllegalStateException ex) {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

}
