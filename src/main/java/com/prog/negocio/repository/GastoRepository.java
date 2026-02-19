package com.prog.negocio.repository;

import com.prog.negocio.entity.GastoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface GastoRepository extends JpaRepository<GastoEntity, Long> {
    @Query("""
           SELECT COALESCE(SUM(g.valor), 0)
           FROM GastoEntity g
           WHERE g.fecha BETWEEN :inicio AND :fin
           """)
    BigDecimal sumBetween(@Param("inicio") LocalDateTime inicio,
                          @Param("fin") LocalDateTime fin);
}
