package com.prog.negocio.repository;

import com.prog.negocio.entity.CierreQuincenal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CierreRepository extends JpaRepository<CierreQuincenal, Long> {
}
