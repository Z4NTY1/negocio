package com.prog.negocio.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "cierres_quincenales")
public class CierreQuincenalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name ="total_ventas", precision = 15, scale = 2)
    private BigDecimal totalVentas;

    @Column(name = "total_gastos", precision = 15, scale = 2)
    private BigDecimal totalGastos;

    @Column(name = "utilidad_neta",precision = 15, scale = 2)
    private BigDecimal utilidadNeta;
}
