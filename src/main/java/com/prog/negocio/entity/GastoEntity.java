package com.prog.negocio.entity;

import com.prog.negocio.enums.CategoriaGasto;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "gastos")
public class GastoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "categoria")
    @Enumerated(EnumType.STRING)
    private CategoriaGasto categoria;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "valor",precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "descripcion")
    private String descripcion;
}
