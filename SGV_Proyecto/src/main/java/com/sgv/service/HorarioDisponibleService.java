package com.sgv.service;

import com.sgv.model.HorarioDisponible;
import com.sgv.repository.HorarioDisponibleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class HorarioDisponibleService {

    @Autowired
    private HorarioDisponibleRepository horarioRepo;

    public List<HorarioDisponible> obtenerTodos() {
        return horarioRepo.findAll();
    }

    public List<HorarioDisponible> obtenerPorFecha(LocalDate fecha) {
        return horarioRepo.findByFecha(fecha);
    }

    public boolean existeHorario(LocalDate fecha, LocalTime hora) {
        return horarioRepo.existsByFechaAndHora(fecha, hora);
    }

    public void guardar(HorarioDisponible horario) {
        horarioRepo.save(horario);
    }

    public void eliminar(Long id) {
        horarioRepo.deleteById(id);
    }

    public Optional<HorarioDisponible> obtenerPorId(Long id) {
        return horarioRepo.findById(id);
    }

    public void actualizarEstado(Long id, String nuevoEstado) {
        Optional<HorarioDisponible> optional = horarioRepo.findById(id);
        if (optional.isPresent()) {
            HorarioDisponible horario = optional.get();
            horario.setEstado(nuevoEstado);
            horarioRepo.save(horario);
        }
    }
}
