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

    public void guardar(HorarioDisponible horario) {
        horarioRepo.save(horario);
    }

    public boolean existeHorario(LocalDate fecha, LocalTime hora) {
        return horarioRepo.existsByFechaAndHora(fecha, hora);
    }

    public List<HorarioDisponible> buscarPorFecha(LocalDate fecha) {
        return horarioRepo.findByFecha(fecha);
    }

    public Optional<HorarioDisponible> obtenerPorId(Long id) {
        return horarioRepo.findById(id);
    }

    public void eliminar(Long id) {
        horarioRepo.deleteById(id);
    }
}
