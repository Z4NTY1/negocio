package com.prog.negocio.config;

import org.springframework.http.HttpStatus;

/**
 * Constantes usadas en la API (códigos HTTP, prefijos, etc.).
 */
public final class ApiConstants {

    /** Código HTTP para errores de validación de entrada (Bad Request). */
    public static final int CODIGO_BAD_REQUEST = HttpStatus.BAD_REQUEST.value();

    private ApiConstants() {
    }
}
