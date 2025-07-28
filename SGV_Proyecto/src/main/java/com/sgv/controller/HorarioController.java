package com.sgv.controller;

import com.sgv.model.HorarioDisponible;
import com.sgv.service.ClienteService;
import com.sgv.service.HorarioDisponibleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/cliente/horarios")
public class HorarioController {

    @Autowired
    private HorarioDisponibleService horarioService;

    @Autowired
    private ClienteService clienteService;

    // 👉 Mostrar lista de horarios
    @GetMapping
    public String mostrarHorarios(Model model) {
        model.addAttribute("horarios", horarioService.obtenerTodos());
        return "horarios";
    }

    // 👉 Mostrar formulario para nuevo horario
    @GetMapping("/nueva")
    public String nuevoHorario(Model model) {
        model.addAttribute("horario", new HorarioDisponible());
        model.addAttribute("clientes", clienteService.obtenerTodos());
        return "form_horario";
    }

    // 👉 Mostrar formulario para editar un horario
    @GetMapping("/editar/{id}")
    public String editarHorario(@PathVariable Long id, Model model) {
        Optional<HorarioDisponible> horario = horarioService.obtenerPorId(id);

        if (horario.isPresent()) {
            model.addAttribute("horario", horario.get());
            model.addAttribute("clientes", clienteService.obtenerTodos());
            return "form_horario";
        } else {
            return "redirect:/cliente/horarios";
        }
    }

    // 👉 Guardar o actualizar horario
    @PostMapping("/guardar")
    public String guardarHorario(@ModelAttribute("horario") HorarioDisponible horario, Model model) {

        // Validar duplicado solo si es nuevo
        if (horario.getId() == null && horarioService.existeHorario(horario.getFecha(), horario.getHora())) {
            model.addAttribute("error", "Ya existe un horario con esa fecha y hora");
            model.addAttribute("horario", horario);
            model.addAttribute("clientes", clienteService.obtenerTodos());
            return "form_horario";
        }

        horario.setEstado("Disponible");
        horarioService.guardar(horario);

        return "redirect:/cliente/horarios";
    }

    // 👉 Eliminar horario
    @GetMapping("/eliminar/{id}")
    public String eliminarHorario(@PathVariable Long id) {
        horarioService.eliminar(id);
        return "redirect:/cliente/horarios";
    }
}
