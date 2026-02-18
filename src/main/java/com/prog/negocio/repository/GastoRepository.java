package com.prog.negocio.repository;

import com.prog.negocio.entity.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long> {
    @Query("""
           SELECT COALESCE(SUM(g.valor), 0)
           FROM Gasto g
           WHERE g.fecha BETWEEN :inicio AND :fin
           """)
    BigDecimal sumBetween(@Param("inicio") LocalDate inicio,
                          @Param("fin") LocalDate fin);
}
