package com.sgv.service;

import com.sgv.model.HorarioDisponible;
import com.sgv.repository.HorarioDisponibleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorarioDisponibleService {

    @Autowired
    private HorarioDisponibleRepository horarioRepo;

    public List<HorarioDisponible> obtenerTodos() {
        return horarioRepo.findAll();
    }
}
