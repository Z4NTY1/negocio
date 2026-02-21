package com.prog.negocio.exceptions;

import com.prog.negocio.business.ErrorCodesNegocio;

/**
 * Fábrica para crear excepciones de negocio de forma estandarizada
 */
public final class BcExceptionFactory {

    private BcExceptionFactory() {
    }

    /**
     * Crea una excepción a partir de un código de error
     */
    public static BusinessException create(int errorCode) {
        String messageKey = getMessageKey(errorCode);
        return new BusinessException(errorCode, messageKey);
    }

    /**
     * Crea una excepción con parámetros para el mensaje
     */
    public static BusinessException create(int errorCode, Object... params) {
        String messageKey = getMessageKey(errorCode);
        return new BusinessException(errorCode, messageKey, params);
    }

    /**
     * Mapea el código de error a la clave del mensaje
     * La convención es: error.<NOMBRE_CONSTANTE>
     */
    private static String getMessageKey(int errorCode) {
        return switch (errorCode) {
            case ErrorCodesNegocio.RANGO_CIERRE_EXISTE -> "error.RANGO_CIERRE_EXISTE";
            case ErrorCodesNegocio.FECHAS_NULAS -> "error.FECHAS_NULAS";
            case ErrorCodesNegocio.FECHA_INICIO_MAYOR -> "error.FECHA_INICIO_MAYOR";
            case ErrorCodesNegocio.ERROR_GENERANDO_REPORTE -> "error.ERROR_GENERANDO_REPORTE";
            case ErrorCodesNegocio.GASTO_REQUEST_NULO -> "error.GASTO_REQUEST_NULO";
            case ErrorCodesNegocio.CATEGORIA_GASTO_NULA -> "error.CATEGORIA_GASTO_NULA";
            case ErrorCodesNegocio.VALOR_GASTO_INVALIDO -> "error.VALOR_GASTO_INVALIDO";
            case ErrorCodesNegocio.PRODUCTO_REQUEST_NULO -> "error.PRODUCTO_REQUEST_NULO";
            case ErrorCodesNegocio.PRODUCTO_ID_INVALIDO -> "error.PRODUCTO_ID_INVALIDO";
            case ErrorCodesNegocio.PRODUCTO_NO_ENCONTRADO -> "error.PRODUCTO_NO_ENCONTRADO";
            case ErrorCodesNegocio.PRODUCTO_INACTIVO -> "error.PRODUCTO_INACTIVO";
            case ErrorCodesNegocio.NOMBRE_PRODUCTO_VACIO -> "error.NOMBRE_PRODUCTO_VACIO";
            case ErrorCodesNegocio.PRECIO_VENTA_INVALIDO -> "error.PRECIO_VENTA_INVALIDO";
            case ErrorCodesNegocio.COSTO_INVALIDO -> "error.COSTO_INVALIDO";
            case ErrorCodesNegocio.STOCK_INICIAL_INVALIDO -> "error.STOCK_INICIAL_INVALIDO";
            case ErrorCodesNegocio.VENTA_REQUEST_NULO -> "error.VENTA_REQUEST_NULO";
            case ErrorCodesNegocio.TIPO_PAGO_NULO -> "error.TIPO_PAGO_NULO";
            case ErrorCodesNegocio.DETALLES_VENTA_VACIOS -> "error.DETALLES_VENTA_VACIOS";
            case ErrorCodesNegocio.DETALLE_PRODUCTO_ID_NULO -> "error.DETALLE_PRODUCTO_ID_NULO";
            case ErrorCodesNegocio.DETALLE_CANTIDAD_INVALIDA -> "error.DETALLE_CANTIDAD_INVALIDA";
            case ErrorCodesNegocio.STOCK_INSUFICIENTE -> "error.STOCK_INSUFICIENTE";
            case ErrorCodesNegocio.FECHA_CONSULTA_NULA -> "error.FECHA_CONSULTA_NULA";
            default -> "error.UNKNOWN_ERROR";
        };
    }
}
