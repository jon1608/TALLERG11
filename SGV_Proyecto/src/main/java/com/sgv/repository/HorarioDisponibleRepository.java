package com.sgv.repository;

import com.sgv.model.HorarioDisponible;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface HorarioDisponibleRepository extends JpaRepository<HorarioDisponible, Long> {

    boolean existsByFechaAndHora(LocalDate fecha, LocalTime hora); // Para evitar duplicados

    List<HorarioDisponible> findByFecha(LocalDate fecha); // Para listar horarios por día
}
