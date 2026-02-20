package com.prog.negocio.service;

import com.prog.negocio.dto.CierreResponseDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio de cierres quincenales: generación, listado, resumen y reporte Excel.
 */
public interface CierreService {

    /**
     * Genera y persiste un cierre para el rango de fechas (ventas, gastos, utilidad).
     *
     * @param inicio fecha inicial del rango (inclusive)
     * @param fin    fecha final del rango (inclusive)
     * @return DTO del cierre guardado con totales
     * @throws IllegalArgumentException si las fechas son nulas o inicio &gt; fin
     * @throws RuntimeException        si ya existe un cierre para ese rango
     */
    CierreResponseDTO generarCierre(LocalDate inicio, LocalDate fin);

    /**
     * Lista todos los cierres guardados.
     *
     * @return lista de cierres con sus totales
     */
    List<CierreResponseDTO> listarCierres();

    /**
     * Calcula el resumen (ventas, gastos, utilidad) para un rango sin guardar cierre.
     *
     * @param inicio fecha inicial del rango
     * @param fin    fecha final del rango
     * @return DTO con totales (id del cierre en null)
     */
    CierreResponseDTO calcularResumen(LocalDate inicio, LocalDate fin);

    /**
     * Genera un archivo Excel con el reporte de cierre para el rango indicado.
     *
     * @param inicio fecha inicial del rango
     * @param fin    fecha final del rango
     * @return bytes del archivo .xlsx
     */
    byte[] generarReportePorRango(LocalDate inicio, LocalDate fin);
}
