package com.prog.negocio.service;

import com.prog.negocio.dto.*;
import com.prog.negocio.entity.DetalleVentaEntity;
import com.prog.negocio.entity.MovimientoContableEntity;
import com.prog.negocio.entity.ProductoEntity;
import com.prog.negocio.entity.VentaEntity;
import com.prog.negocio.enumAtributte.TipoMovimientoContable;
import com.prog.negocio.repository.MovimientoContableRepository;
import com.prog.negocio.repository.ProductoRepository;
import com.prog.negocio.repository.VentaRepository;
import com.prog.negocio.service.iservice.VentaService;
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
    public VentaResponseDTO registrarVenta(VentaRequestDTO dto) {

        VentaEntity ventaEntity = new VentaEntity();
        ventaEntity.setFecha(LocalDateTime.now());
        ventaEntity.setTipoPago(dto.getTipoPago());

        BigDecimal total = BigDecimal.ZERO;
        List<DetalleVentaEntity> detalles = new ArrayList<>();

        for (DetalleVentaDTO d : dto.getDetalles()) {

            ProductoEntity productoEntity = productoRepository.findById(d.getProductoId())
                    .orElseThrow();

            if (productoEntity.getStockActual() < d.getCantidad()) {
                throw new RuntimeException("Stock insuficiente");
            }

            productoEntity.setStockActual(productoEntity.getStockActual() - d.getCantidad());

            BigDecimal subtotal = productoEntity.getPrecioVenta()
                    .multiply(BigDecimal.valueOf(d.getCantidad()));

            total = total.add(subtotal);

            DetalleVentaEntity detalle = new DetalleVentaEntity();
            detalle.setVentaEntity(ventaEntity);
            detalle.setProductoEntity(productoEntity);
            detalle.setCantidad(d.getCantidad());
            detalle.setPrecioUnitario(productoEntity.getPrecioVenta());
            detalle.setSubtotal(subtotal);

            detalles.add(detalle);
        }

        ventaEntity.setTotal(total);
        ventaEntity.setDetalles(detalles);

        ventaRepository.save(ventaEntity);

        // Registrar movimiento contable
        MovimientoContableEntity mov = new MovimientoContableEntity();
        mov.setFecha(LocalDateTime.now());
        mov.setTipo(TipoMovimientoContable.INGRESO);
        mov.setTipoPago(dto.getTipoPago());
        mov.setMonto(total);
        mov.setDescripcion("Venta ID: " + ventaEntity.getId());

        movimientoRepository.save(mov);

        return new VentaResponseDTO(
                ventaEntity.getId(),
                total,
                dto.getTipoPago(),
                ventaEntity.getFecha()
        );
    }

    @Override
    public List<VentaResponseDTO> listarPorFecha(LocalDate fecha) {

        return ventaRepository.findByFechaBetween(
                        fecha.atStartOfDay(),
                        fecha.atTime(23,59,59))
                .stream()
                .map(v -> new VentaResponseDTO(
                        v.getId(),
                        v.getTotal(),
                        v.getTipoPago(),
                        v.getFecha()))
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
    private ResumenGeneralDTO construirResumen(LocalDateTime inicio,
                                               LocalDateTime fin) {

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
