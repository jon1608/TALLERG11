package com.sgv.service;

import com.sgv.model.OrdenReparacion;
import com.sgv.repository.OrdenReparacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdenReparacionService {

    @Autowired
    private OrdenReparacionRepository ordenRepo;

    public List<OrdenReparacion> obtenerTodas() {
        return ordenRepo.findAll();
    }

    public Optional<OrdenReparacion> obtenerPorId(Long id) {
        return ordenRepo.findById(id);
    }

    public void guardar(OrdenReparacion orden) {
        ordenRepo.save(orden);
    }

    public void eliminar(Long id) {
        ordenRepo.deleteById(id);
    }
}
