package com.prog.negocio.entity;

import com.prog.negocio.enumAtributte.TipoMovimientoContable;
import com.prog.negocio.enumAtributte.TipoPago;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "movimientos_contables")
public class MovimientoContable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private TipoMovimientoContable tipo;

    @Enumerated(EnumType.STRING)
    private TipoPago tipoPago;

    @Column(precision = 15, scale = 2)
    private BigDecimal monto;

    private String descripcion;
}
