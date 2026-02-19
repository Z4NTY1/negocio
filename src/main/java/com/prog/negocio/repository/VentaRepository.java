package com.prog.negocio.repository;

import com.prog.negocio.dto.ResumenPagoDTO;
import com.prog.negocio.entity.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<VentaEntity, Long> {

    List<VentaEntity> findByFechaBetween(LocalDateTime inicio,
                                         LocalDateTime fin);

    @Query("""
           SELECT COALESCE(SUM(v.total), 0)
           FROM VentaEntity v
           WHERE v.fecha BETWEEN :inicio AND :fin
           """)
    BigDecimal sumTotalBetween(@Param("inicio") LocalDateTime inicio,
                               @Param("fin") LocalDateTime fin);

    @Query("""
    SELECT new com.prog.negocio.dto.ResumenPagoDTO(
        v.tipoPago,
        COUNT(v),
        SUM(v.total)
    )
    FROM VentaEntity v
    WHERE v.fecha BETWEEN :inicio AND :fin
    GROUP BY v.tipoPago
""")
    List<ResumenPagoDTO> obtenerResumenPorRango(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );
}
