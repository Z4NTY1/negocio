package com.prog.negocio.service.impl;

import com.prog.negocio.business.ErrorCodesNegocio;
import com.prog.negocio.dto.*;
import com.prog.negocio.entity.DetalleVentaEntity;
import com.prog.negocio.entity.MovimientoContableEntity;
import com.prog.negocio.entity.ProductoEntity;
import com.prog.negocio.entity.VentaEntity;
import com.prog.negocio.enums.TipoMovimientoContable;
import com.prog.negocio.exceptions.BcExceptionFactory;
import com.prog.negocio.repository.MovimientoContableRepository;
import com.prog.negocio.repository.ProductoRepository;
import com.prog.negocio.repository.VentaRepository;
import com.prog.negocio.mapper.VentaMapper;
import com.prog.negocio.service.VentaService;
import com.prog.negocio.util.FechaUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private static final Logger log = LoggerFactory.getLogger(VentaServiceImpl.class);

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoContableRepository movimientoRepository;

    /**
     * Registra una venta con sus detalles, actualiza stock y genera el movimiento contable (ingreso).
     *
     * @param request tipo de pago y lista de productos con cantidad
     * @return DTO de la venta registrada (id, total, tipo de pago, fecha)
     */
    @Override
    @Transactional
    public VentaResponseDTO registrarVenta(VentaRequestDTO request) {

        validarVentaRequest(request);

        VentaEntity venta = new VentaEntity();
        venta.setFecha(LocalDateTime.now());
        venta.setTipoPago(request.getTipoPago());

        BigDecimal total = BigDecimal.ZERO;
        List<DetalleVentaEntity> detalles = new ArrayList<>();

        for (DetalleVentaDTO detalleDto : request.getDetalles()) {

            validarDetalleVenta(detalleDto);

            ProductoEntity producto = productoRepository.findById(detalleDto.getProductoId())
                    .orElseThrow(() -> BcExceptionFactory.create(ErrorCodesNegocio.PRODUCTO_NO_ENCONTRADO));

            if (!producto.getActivo()) {
                throw BcExceptionFactory.create(ErrorCodesNegocio.PRODUCTO_INACTIVO);
            }

            if (producto.getStockActual() < detalleDto.getCantidad()) {
                throw BcExceptionFactory.create(ErrorCodesNegocio.STOCK_INSUFICIENTE);
            }

            producto.setStockActual(producto.getStockActual() - detalleDto.getCantidad());

            BigDecimal subtotal = producto.getPrecioVenta()
                    .multiply(BigDecimal.valueOf(detalleDto.getCantidad()));

            total = total.add(subtotal);

            DetalleVentaEntity detalle = new DetalleVentaEntity();
            detalle.setVentaEntity(venta);
            detalle.setProductoEntity(producto);
            detalle.setCantidad(detalleDto.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecioVenta());
            detalle.setSubtotal(subtotal);

            detalles.add(detalle);
        }

        venta.setTotal(total);
        venta.setDetalles(detalles);

        ventaRepository.save(venta);
        log.info("Venta registrada: id={}, total={}, tipoPago={}", venta.getId(), total, request.getTipoPago());

        MovimientoContableEntity movimiento = new MovimientoContableEntity();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipo(TipoMovimientoContable.INGRESO);
        movimiento.setTipoPago(request.getTipoPago());
        movimiento.setMonto(total);
        movimiento.setDescripcion("Venta ID: " + venta.getId());

        movimientoRepository.save(movimiento);

        return VentaMapper.toDTO(venta);
    }

    /**
     * Lista todas las ventas realizadas en una fecha dada.
     *
     * @param fecha fecha a consultar
     * @return lista de ventas del día
     */
    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> listarPorFecha(LocalDate fecha) {

        if (fecha == null) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.FECHA_CONSULTA_NULA);
        }

        return ventaRepository.findByFechaBetween(
                        FechaUtil.inicioDelDia(fecha),
                        FechaUtil.finDelDia(fecha))
                .stream()
                .map(VentaMapper::toDTO)
                .toList();
    }

    /**
     * Obtiene el resumen de ventas de un día (totales por tipo de pago).
     *
     * @param fecha fecha a consultar
     * @return resumen con detalle por tipo de pago y totales
     */
    @Override
    @Transactional(readOnly = true)
    public ResumenGeneralDTO resumenDiario(LocalDate fecha) {

        if (fecha == null) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.FECHA_CONSULTA_NULA);
        }

        return construirResumen(
                FechaUtil.inicioDelDia(fecha),
                FechaUtil.finDelDia(fecha)
        );
    }

    /**
     * Obtiene el resumen de ventas de la quincena que contiene la fecha dada.
     * Quincena 1: días 1-15; Quincena 2: días 16-fin de mes.
     *
     * @param fechaReferencia fecha dentro de la quincena a consultar
     * @return resumen con detalle por tipo de pago y totales
     */
    @Override
    @Transactional(readOnly = true)
    public ResumenGeneralDTO resumenQuincenal(LocalDate fechaReferencia) {

        if (fechaReferencia == null) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.FECHA_CONSULTA_NULA);
        }

        FechaUtil.RangoQuincena rango = FechaUtil.rangoQuincena(fechaReferencia);

        return construirResumen(
                FechaUtil.inicioDelDia(rango.inicio()),
                FechaUtil.finDelDia(rango.fin())
        );
    }

    private ResumenGeneralDTO construirResumen(LocalDateTime inicio, LocalDateTime fin) {

        List<ResumenPagoDTO> detalle =
                ventaRepository.obtenerResumenPorRango(inicio, fin);

        Long totalVentas = detalle.stream()
                .mapToLong(ResumenPagoDTO::getCantidadVentas)
                .sum();

        BigDecimal totalDinero = detalle.stream()
                .map(ResumenPagoDTO::getTotalDinero)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ResumenGeneralDTO(detalle, totalVentas, totalDinero);
    }

    private void validarVentaRequest(VentaRequestDTO request) {
        if (request == null) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.VENTA_REQUEST_NULO);
        }
        if (request.getTipoPago() == null) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.TIPO_PAGO_NULO);
        }
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.DETALLES_VENTA_VACIOS);
        }
    }

    private void validarDetalleVenta(DetalleVentaDTO detalle) {
        if (detalle.getProductoId() == null) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.DETALLE_PRODUCTO_ID_NULO);
        }
        if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.DETALLE_CANTIDAD_INVALIDA);
        }
    }
}
