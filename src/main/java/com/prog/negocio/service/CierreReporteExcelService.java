package com.prog.negocio.service;

import com.prog.negocio.dto.ResumenPagoDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Genera el archivo Excel del reporte de cierre.
 */
public interface CierreReporteExcelService {

    /**
     * Construye el archivo .xlsx con el detalle de ventas por tipo de pago y el resumen financiero.
     *
     * @param inicio       fecha de inicio del rango
     * @param fin          fecha de fin del rango
     * @param detalle      resumen de ventas agrupado por tipo de pago
     * @param totalVentas  total de ventas del período
     * @param totalGastos  total de gastos del período
     * @param utilidadNeta utilidad neta del período
     * @return bytes del archivo .xlsx
     */
    byte[] generarExcel(
            LocalDate inicio,
            LocalDate fin,
            List<ResumenPagoDTO> detalle,
            BigDecimal totalVentas,
            BigDecimal totalGastos,
            BigDecimal utilidadNeta
    );
}
