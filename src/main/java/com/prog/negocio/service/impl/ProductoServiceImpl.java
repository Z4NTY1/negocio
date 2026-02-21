package com.prog.negocio.service.impl;

import com.prog.negocio.business.ErrorCodesNegocio;
import com.prog.negocio.dto.ActualizarProductoDTO;
import com.prog.negocio.dto.ProductoRequestDTO;
import com.prog.negocio.dto.ProductoResponseDTO;
import com.prog.negocio.entity.ProductoEntity;
import com.prog.negocio.exceptions.BcExceptionFactory;
import com.prog.negocio.mapper.ProductoMapper;
import com.prog.negocio.repository.ProductoRepository;
import com.prog.negocio.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;

    /**
     * Crea un nuevo producto con los datos indicados.
     *
     * @param request nombre, precio de venta, costo y stock inicial
     * @return DTO del producto creado
     */
    @Override
    @Transactional
    public ProductoResponseDTO crear(ProductoRequestDTO request) {

        validarRequestCrear(request);

        ProductoEntity producto = ProductoMapper.toEntity(request);
        productoRepository.save(producto);
        log.info("Producto creado: id={}, nombre={}", producto.getId(), producto.getNombre());

        return ProductoMapper.toDTO(producto);
    }

    /**
     * Actualiza nombre, precio de venta y costo de un producto existente.
     *
     * @param id      identificador del producto
     * @param request datos a actualizar
     * @return DTO del producto actualizado
     */
    @Override
    @Transactional
    public ProductoResponseDTO actualizar(Long id, ActualizarProductoDTO request) {

        validarIdProducto(id);
        validarRequestActualizar(request);

        ProductoEntity producto = productoRepository.findById(id)
                .orElseThrow(() -> BcExceptionFactory.create(ErrorCodesNegocio.PRODUCTO_NO_ENCONTRADO));

        if (!producto.getActivo()) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.PRODUCTO_INACTIVO);
        }

        producto.setNombre(request.getNombre().trim());
        producto.setPrecioVenta(request.getPrecioVenta());
        producto.setCosto(request.getCosto());

        productoRepository.save(producto);
        log.info("Producto actualizado: id={}", id);

        return obtenerPorId(id);
    }

    /**
     * Desactiva un producto (borrado lógico).
     *
     * @param id identificador del producto
     */
    @Override
    @Transactional
    public void eliminar(Long id) {

        validarIdProducto(id);

        ProductoEntity producto = productoRepository.findById(id)
                .orElseThrow(() -> BcExceptionFactory.create(ErrorCodesNegocio.PRODUCTO_NO_ENCONTRADO));

        producto.setActivo(false);

        productoRepository.save(producto);
        log.info("Producto desactivado (eliminado lógico): id={}", id);
    }

    /**
     * Lista todos los productos activos.
     *
     * @return lista de productos con id, nombre, precio, costo y stock
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listar() {
        return productoRepository.findByActivoTrue()
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
    }

    /**
     * Obtiene un producto por su identificador.
     *
     * @param id identificador del producto
     * @return DTO del producto
     */
    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerPorId(Long id) {

        validarIdProducto(id);

        ProductoEntity producto = productoRepository.findById(id)
                .orElseThrow(() -> BcExceptionFactory.create(ErrorCodesNegocio.PRODUCTO_NO_ENCONTRADO));

        return ProductoMapper.toDTO(producto);
    }

    private void validarIdProducto(Long id) {
        if (id == null || id <= 0) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.PRODUCTO_ID_INVALIDO);
        }
    }

    private void validarRequestCrear(ProductoRequestDTO request) {
        if (request == null) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.PRODUCTO_REQUEST_NULO);
        }
        if (isBlank(request.getNombre())) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.NOMBRE_PRODUCTO_VACIO);
        }
        if (request.getPrecioVenta() == null || request.getPrecioVenta().compareTo(BigDecimal.ZERO) < 0) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.PRECIO_VENTA_INVALIDO);
        }
        if (request.getCosto() == null || request.getCosto().compareTo(BigDecimal.ZERO) < 0) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.COSTO_INVALIDO);
        }
        if (request.getStockInicial() == null || request.getStockInicial() < 0) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.STOCK_INICIAL_INVALIDO);
        }
    }

    private void validarRequestActualizar(ActualizarProductoDTO request) {
        if (request == null) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.PRODUCTO_REQUEST_NULO);
        }
        if (isBlank(request.getNombre())) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.NOMBRE_PRODUCTO_VACIO);
        }
        if (request.getPrecioVenta() == null || request.getPrecioVenta().compareTo(BigDecimal.ZERO) < 0) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.PRECIO_VENTA_INVALIDO);
        }
        if (request.getCosto() == null || request.getCosto().compareTo(BigDecimal.ZERO) < 0) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.COSTO_INVALIDO);
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
