package com.prog.negocio.service;

import com.prog.negocio.dto.ActualizarProductoDTO;
import com.prog.negocio.dto.ProductoRequestDTO;
import com.prog.negocio.dto.ProductoResponseDTO;

import java.util.List;

/**
 * Servicio de productos: alta, actualización, listado y baja lógica.
 */
public interface ProductoService {

    /**
     * Crea un nuevo producto con stock inicial.
     *
     * @param request nombre, precio de venta, costo y stock inicial
     * @return producto creado con su id
     */
    ProductoResponseDTO crear(ProductoRequestDTO request);

    /**
     * Actualiza nombre, precio de venta y costo de un producto existente.
     *
     * @param id      id del producto
     * @param request nuevos datos (nombre, precio, costo)
     * @return producto actualizado
     */
    ProductoResponseDTO actualizar(Long id, ActualizarProductoDTO request);

    /**
     * Baja lógica del producto (lo marca como inactivo, no se borra).
     *
     * @param id id del producto
     */
    void eliminar(Long id);

    /**
     * Lista todos los productos activos.
     *
     * @return lista de productos activos
     */
    List<ProductoResponseDTO> listar();

    /**
     * Obtiene un producto por id.
     *
     * @param id id del producto
     * @return datos del producto
     */
    ProductoResponseDTO obtenerPorId(Long id);
}
