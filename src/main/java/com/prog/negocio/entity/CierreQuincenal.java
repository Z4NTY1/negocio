package com.prog.negocio.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "cierres_quincenales")
public class CierreQuincenal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalVentas;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalGastos;

    @Column(precision = 15, scale = 2)
    private BigDecimal utilidadNeta;
}
