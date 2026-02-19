package com.prog.negocio.repository;

import com.prog.negocio.entity.CierreQuincenalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CierreRepository extends JpaRepository<CierreQuincenalEntity, Long> {
    Optional<CierreQuincenalEntity> findByFechaInicioAndFechaFin(
            LocalDate fechaInicio,
            LocalDate fechaFin
    );
}
