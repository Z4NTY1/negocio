package com.prog.negocio.repository;

import com.prog.negocio.entity.MovimientoContableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoContableRepository extends JpaRepository<MovimientoContableEntity, Long> {
}
