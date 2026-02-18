package com.prog.negocio.repository;

import com.prog.negocio.entity.MovimientoContable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoContableRepository extends JpaRepository<MovimientoContable, Long> {
}
