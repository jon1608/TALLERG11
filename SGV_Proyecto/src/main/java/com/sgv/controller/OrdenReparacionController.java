package com.sgv.controller;

import com.sgv.model.Cliente;
import com.sgv.model.OrdenReparacion;
import com.sgv.service.ClienteService;
import com.sgv.service.OrdenReparacionService;
import jakarta.validation.Valid;
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

    @GetMapping
    public String listarOrdenes(Model model) {
        model.addAttribute("ordenes", ordenService.obtenerTodas());
        return "ordenes";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("orden", new OrdenReparacion());
        model.addAttribute("clientes", clienteService.obtenerTodos());
        return "form_orden";
    }

    @PostMapping("/guardar")
    public String guardarOrden(@Valid @ModelAttribute OrdenReparacion orden, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.obtenerTodos());
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
