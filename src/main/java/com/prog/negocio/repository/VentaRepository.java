package com.prog.negocio.repository;

import com.prog.negocio.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
    @Query("""
           SELECT COALESCE(SUM(v.total), 0)
           FROM Venta v
           WHERE v.fecha BETWEEN :inicio AND :fin
           """)
    BigDecimal sumTotalBetween(@Param("inicio") LocalDateTime inicio,
                               @Param("fin") LocalDateTime fin);
}
