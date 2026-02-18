package com.prog.negocio.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal precioVenta;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal costo;

    @Column(nullable = false)
    private Integer stockActual;

    @Column(nullable = false)
    private Boolean activo;
}
