package com.prog.negocio.entity;

import com.prog.negocio.enums.TipoMovimientoContable;
import com.prog.negocio.enums.TipoPago;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "movimientos_contables")
public class MovimientoContableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "tipo_movimiento")
    @Enumerated(EnumType.STRING)
    private TipoMovimientoContable tipo;

    @Column(name = "tipo_pago")
    @Enumerated(EnumType.STRING)
    private TipoPago tipoPago;

    @Column(name = "monto",precision = 15, scale = 2)
    private BigDecimal monto;

    @Column(name = "descripcion")
    private String descripcion;
}
