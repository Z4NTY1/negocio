package com.prog.negocio.entity;

import com.prog.negocio.enumAtributte.TipoPago;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "ventas")
public class VentaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "tipo_pago")
    @Enumerated(EnumType.STRING)
    private TipoPago tipoPago;

    @Column(precision = 15, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "ventaEntity", cascade = CascadeType.ALL)
    private List<DetalleVentaEntity> detalles;
}
