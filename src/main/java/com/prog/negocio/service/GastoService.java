package com.prog.negocio.service;

import com.prog.negocio.dto.GastoDTO;

/**
 * Servicio de gastos: registro de egresos y su movimiento contable.
 */
public interface GastoService {

    /**
     * Registra un gasto y el correspondiente movimiento contable de egreso.
     *
     * @param request categoría, valor, descripción y fecha implícita (ahora)
     */
    void registrar(GastoDTO request);
}
