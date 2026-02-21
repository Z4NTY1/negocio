package com.prog.negocio.business;

/**
 * Códigos de Error del sistema de negocio
 * Cada código se mapea a una clave en messages.properties
 */
public class ErrorCodesNegocio {

    public static final int FIRST_USER_ERROR_CODE = 10000;

    // Errores de CierreService
    public static final int RANGO_CIERRE_EXISTE = FIRST_USER_ERROR_CODE + 1;
    public static final int FECHAS_NULAS = FIRST_USER_ERROR_CODE + 2;
    public static final int FECHA_INICIO_MAYOR = FIRST_USER_ERROR_CODE + 3;
    public static final int ERROR_GENERANDO_REPORTE = FIRST_USER_ERROR_CODE + 4;

    // Errores de GastoService
    public static final int GASTO_REQUEST_NULO = FIRST_USER_ERROR_CODE + 10;
    public static final int CATEGORIA_GASTO_NULA = FIRST_USER_ERROR_CODE + 11;
    public static final int VALOR_GASTO_INVALIDO = FIRST_USER_ERROR_CODE + 12;

    // Errores de ProductoService
    public static final int PRODUCTO_REQUEST_NULO = FIRST_USER_ERROR_CODE + 20;
    public static final int PRODUCTO_ID_INVALIDO = FIRST_USER_ERROR_CODE + 21;
    public static final int PRODUCTO_NO_ENCONTRADO = FIRST_USER_ERROR_CODE + 22;
    public static final int PRODUCTO_INACTIVO = FIRST_USER_ERROR_CODE + 23;
    public static final int NOMBRE_PRODUCTO_VACIO = FIRST_USER_ERROR_CODE + 24;
    public static final int PRECIO_VENTA_INVALIDO = FIRST_USER_ERROR_CODE + 25;
    public static final int COSTO_INVALIDO = FIRST_USER_ERROR_CODE + 26;
    public static final int STOCK_INICIAL_INVALIDO = FIRST_USER_ERROR_CODE + 27;

    // Errores de VentaService
    public static final int VENTA_REQUEST_NULO = FIRST_USER_ERROR_CODE + 100;
    public static final int TIPO_PAGO_NULO = FIRST_USER_ERROR_CODE + 101;
    public static final int DETALLES_VENTA_VACIOS = FIRST_USER_ERROR_CODE + 102;
    public static final int DETALLE_PRODUCTO_ID_NULO = FIRST_USER_ERROR_CODE + 103;
    public static final int DETALLE_CANTIDAD_INVALIDA = FIRST_USER_ERROR_CODE + 104;
    public static final int STOCK_INSUFICIENTE = FIRST_USER_ERROR_CODE + 105;
    public static final int FECHA_CONSULTA_NULA = FIRST_USER_ERROR_CODE + 106;

    protected ErrorCodesNegocio() {
    }
}
