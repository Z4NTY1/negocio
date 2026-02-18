package com.prog.negocio.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String proveedor;

    private LocalDate fecha;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalPedido;

    private Boolean pagado;
}
