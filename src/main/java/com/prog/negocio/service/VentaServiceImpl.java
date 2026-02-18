package com.prog.negocio.service;

import com.prog.negocio.dto.DetalleVentaDTO;
import com.prog.negocio.dto.VentaRequestDTO;
import com.prog.negocio.dto.VentaResponseDTO;
import com.prog.negocio.entity.DetalleVenta;
import com.prog.negocio.entity.MovimientoContable;
import com.prog.negocio.entity.Producto;
import com.prog.negocio.entity.Venta;
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

        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setTipoPago(dto.getTipoPago());

        BigDecimal total = BigDecimal.ZERO;
        List<DetalleVenta> detalles = new ArrayList<>();

        for (DetalleVentaDTO d : dto.getDetalles()) {

            Producto producto = productoRepository.findById(d.getProductoId())
                    .orElseThrow();

            if (producto.getStockActual() < d.getCantidad()) {
                throw new RuntimeException("Stock insuficiente");
            }

            producto.setStockActual(producto.getStockActual() - d.getCantidad());

            BigDecimal subtotal = producto.getPrecioVenta()
                    .multiply(BigDecimal.valueOf(d.getCantidad()));

            total = total.add(subtotal);

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(d.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecioVenta());
            detalle.setSubtotal(subtotal);

            detalles.add(detalle);
        }

        venta.setTotal(total);
        venta.setDetalles(detalles);

        ventaRepository.save(venta);

        // Registrar movimiento contable
        MovimientoContable mov = new MovimientoContable();
        mov.setFecha(LocalDateTime.now());
        mov.setTipo(TipoMovimientoContable.INGRESO);
        mov.setTipoPago(dto.getTipoPago());
        mov.setMonto(total);
        mov.setDescripcion("Venta ID: " + venta.getId());

        movimientoRepository.save(mov);

        return new VentaResponseDTO(
                venta.getId(),
                total,
                dto.getTipoPago(),
                venta.getFecha()
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
}
