package com.sgv.controller;

import com.sgv.model.Cliente;
import com.sgv.model.OrdenReparacion;
import com.sgv.model.Vehiculo;
import com.sgv.service.ClienteService;
import com.sgv.service.OrdenReparacionService;
import com.sgv.service.VehiculoService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mecanico/ordenes")
public class OrdenReparacionController {

    @Autowired
    private OrdenReparacionService ordenService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VehiculoService vehiculoService;

    @GetMapping
    public String listarOrdenes(Model model) {
        model.addAttribute("ordenes", ordenService.obtenerTodas());
        return "ordenes";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        OrdenReparacion orden = new OrdenReparacion();
        orden.setCliente(new Cliente());
        orden.setVehiculo(new Vehiculo());
        model.addAttribute("orden", orden);
        model.addAttribute("clientes", clienteService.obtenerTodos());
        model.addAttribute("vehiculos", vehiculoService.obtenerTodos());
        return "form_orden";
    }

    @PostMapping("/guardar")
    public String guardarOrden(@Valid @ModelAttribute("orden") OrdenReparacion orden,
                               BindingResult result, Model model) {

        // Validar selección de cliente
        if (orden.getCliente() == null || orden.getCliente().getId() == null || orden.getCliente().getId() <= 0) {
            result.rejectValue("cliente", null, "Debe seleccionar un cliente válido.");
        }

        // Validar selección de vehículo
        if (orden.getVehiculo() == null || orden.getVehiculo().getId() == null || orden.getVehiculo().getId() <= 0) {
            result.rejectValue("vehiculo", null, "Debe seleccionar un vehículo válido.");
        } else {
            Optional<Vehiculo> vehiculoOpt = vehiculoService.obtenerPorId(orden.getVehiculo().getId());
            if (vehiculoOpt.isPresent()) {
                orden.setVehiculo(vehiculoOpt.get());
            } else {
                result.rejectValue("vehiculo", null, "El vehículo seleccionado no existe.");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.obtenerTodos());
            model.addAttribute("vehiculos", vehiculoService.obtenerTodos());
            return "form_orden";
        }

        ordenService.guardar(orden);
        return "redirect:/mecanico/ordenes";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        var orden = ordenService.obtenerPorId(id);
        if (orden.isPresent()) {
            model.addAttribute("orden", orden.get());
            model.addAttribute("clientes", clienteService.obtenerTodos());
            model.addAttribute("vehiculos", vehiculoService.obtenerTodos());
            return "form_orden";
        }
        return "redirect:/mecanico/ordenes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarOrden(@PathVariable Long id) {
        ordenService.eliminar(id);
        return "redirect:/mecanico/ordenes";
    }
}
