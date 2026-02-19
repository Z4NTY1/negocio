package com.prog.negocio.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "detalle_venta")
public class DetalleVentaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private VentaEntity ventaEntity;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private ProductoEntity productoEntity;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(precision = 15, scale = 2)
    private BigDecimal precioUnitario;

    @Column(precision = 15, scale = 2)
    private BigDecimal subtotal;
}
