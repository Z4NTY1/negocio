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
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private TipoPago tipoPago;

    @Column(precision = 15, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<DetalleVenta> detalles;
}
