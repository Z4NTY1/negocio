package com.prog.negocio.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "pedidos")
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "proveedor")
    private String proveedor;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalPedido;

    @Column(name = "pagado")
    private Boolean pagado;
}
