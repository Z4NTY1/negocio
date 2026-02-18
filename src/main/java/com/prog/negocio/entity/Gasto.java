package com.prog.negocio.entity;

import com.prog.negocio.enumAtributte.CategoriaGasto;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "gastos")
public class Gasto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoriaGasto categoria;

    private LocalDate fecha;

    @Column(precision = 15, scale = 2)
    private BigDecimal valor;

    private String descripcion;
}
