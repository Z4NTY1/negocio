package com.prog.negocio.service.impl;

import com.prog.negocio.dto.*;
import com.prog.negocio.entity.DetalleVentaEntity;
import com.prog.negocio.entity.MovimientoContableEntity;
import com.prog.negocio.entity.ProductoEntity;
import com.prog.negocio.entity.VentaEntity;
import com.prog.negocio.enums.TipoMovimientoContable;
import com.prog.negocio.repository.MovimientoContableRepository;
import com.prog.negocio.repository.ProductoRepository;
import com.prog.negocio.repository.VentaRepository;
import com.prog.negocio.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoContableRepository movimientoRepository;

    @Override
    public VentaResponseDTO registrarVenta(VentaRequestDTO request) {

        VentaEntity venta = new VentaEntity();
        venta.setFecha(LocalDateTime.now());
        venta.setTipoPago(request.getTipoPago());

        BigDecimal total = BigDecimal.ZERO;
        List<DetalleVentaEntity> detalles = new ArrayList<>();

        for (DetalleVentaDTO detalleDto : request.getDetalles()) {

            ProductoEntity producto = productoRepository.findById(detalleDto.getProductoId())
                    .orElseThrow();

            if (producto.getStockActual() < detalleDto.getCantidad()) {
                throw new RuntimeException("Stock insuficiente");
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

        MovimientoContableEntity movimiento = new MovimientoContableEntity();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipo(TipoMovimientoContable.INGRESO);
        movimiento.setTipoPago(request.getTipoPago());
        movimiento.setMonto(total);
        movimiento.setDescripcion("Venta ID: " + venta.getId());

        movimientoRepository.save(movimiento);

        return new VentaResponseDTO(
                venta.getId(),
                total,
                request.getTipoPago(),
                venta.getFecha()
        );
    }

    @Override
    public List<VentaResponseDTO> listarPorFecha(LocalDate fecha) {

        return ventaRepository.findByFechaBetween(
                        fecha.atStartOfDay(),
                        fecha.atTime(23, 59, 59))
                .stream()
                .map(venta -> new VentaResponseDTO(
                        venta.getId(),
                        venta.getTotal(),
                        venta.getTipoPago(),
                        venta.getFecha()))
                .toList();
    }

    @Override
    public ResumenGeneralDTO resumenDiario(LocalDate fecha) {

        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(23, 59, 59);

        return construirResumen(inicio, fin);
    }

    @Override
    public ResumenGeneralDTO resumenQuincenal(LocalDate fechaReferencia) {

        LocalDate inicio;
        LocalDate fin;

        if (fechaReferencia.getDayOfMonth() <= 15) {
            inicio = fechaReferencia.withDayOfMonth(1);
            fin = fechaReferencia.withDayOfMonth(15);
        } else {
            inicio = fechaReferencia.withDayOfMonth(16);
            fin = fechaReferencia.withDayOfMonth(fechaReferencia.lengthOfMonth());
        }

        return construirResumen(
                inicio.atStartOfDay(),
                fin.atTime(23, 59, 59)
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
}
