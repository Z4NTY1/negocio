package com.prog.negocio.service;

import com.prog.negocio.dto.ResumenGeneralDTO;
import com.prog.negocio.dto.VentaRequestDTO;
import com.prog.negocio.dto.VentaResponseDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio de ventas: registro, listado por fecha y resúmenes diario y quincenal.
 */
public interface VentaService {

    /**
     * Registra una venta con sus detalles, actualiza stock y genera el movimiento contable.
     *
     * @param request datos de la venta (tipo de pago y detalle de productos/cantidades)
     * @return datos de la venta registrada (id, total, tipo pago, fecha)
     * @throws RuntimeException si no hay stock suficiente para algún producto
     */
    VentaResponseDTO registrarVenta(VentaRequestDTO request);

    /**
     * Lista las ventas realizadas en una fecha dada.
     *
     * @param fecha día a consultar
     * @return lista de ventas del día
     */
    List<VentaResponseDTO> listarPorFecha(LocalDate fecha);

    /**
     * Obtiene el resumen de ventas del día (totales por tipo de pago, cantidad y monto).
     *
     * @param fecha día a consultar
     * @return resumen con detalle por tipo de pago y totales
     */
    ResumenGeneralDTO resumenDiario(LocalDate fecha);

    /**
     * Obtiene el resumen de ventas de la quincena que contiene la fecha dada.
     *
     * @param fechaReferencia cualquier fecha dentro de la quincena
     * @return resumen con detalle por tipo de pago y totales
     */
    ResumenGeneralDTO resumenQuincenal(LocalDate fechaReferencia);
}
