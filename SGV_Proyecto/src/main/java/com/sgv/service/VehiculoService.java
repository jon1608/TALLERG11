package com.sgv.service;

import com.sgv.model.Vehiculo;
import com.sgv.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    public List<Vehiculo> obtenerTodos() {
        return vehiculoRepository.findAll();
    }

    public List<Vehiculo> obtenerPorClienteId(Long clienteId) {
        return vehiculoRepository.findByClienteId(clienteId);
    }

    public void guardar(Vehiculo vehiculo) {
        vehiculoRepository.save(vehiculo);
    }

    public Optional<Vehiculo> obtenerPorId(Long id) {
        return vehiculoRepository.findById(id);
    }

    public void eliminar(Long id) {
        vehiculoRepository.deleteById(id);
    }
}
