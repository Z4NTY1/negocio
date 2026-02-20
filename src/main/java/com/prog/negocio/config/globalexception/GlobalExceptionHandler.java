package com.prog.negocio.config.globalexception;

import com.prog.negocio.exceptions.BcExceptionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BcExceptionFactory.class)
    public ResponseEntity<Map<String, Object>> handleBcBusinessException(BcExceptionFactory ex) {
        Map<String, Object> body = Map.of(
                "errorCode", ex.getErrorCode(),
                "message", ex.getMessage()
        );
        // 409 Conflict para "ya existe", ajusta según tu código
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}
