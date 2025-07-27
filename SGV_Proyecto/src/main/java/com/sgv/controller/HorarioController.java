package com.sgv.controller;

import com.sgv.model.HorarioDisponible;
import com.sgv.service.HorarioDisponibleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/cliente/horarios")
public class HorarioController {

    @Autowired
    private HorarioDisponibleService horarioService;

    @GetMapping
    public String mostrarHorarios(Model model) {
        model.addAttribute("horarios", horarioService.obtenerTodos());
        return "horarios";
    }

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        model.addAttribute("horario", new HorarioDisponible());
        return "form_horario";
    }

    @PostMapping("/guardar")
    public String guardarHorario(@ModelAttribute("horario") HorarioDisponible horario, Model model) {
        boolean existe = horarioService.existeHorario(horario.getFecha(), horario.getHora());

        if (existe) {
            model.addAttribute("error", "Ya existe un horario registrado para esa fecha y hora.");
            model.addAttribute("horario", horario);
            return "form_horario";
        }

        horario.setEstado("Disponible");
        horarioService.guardar(horario);
        return "redirect:/cliente/horarios";
    }

    @GetMapping("/filtrar")
    public String filtrarPorFecha(@RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha, Model model) {
        List<HorarioDisponible> horarios = horarioService.obtenerPorFecha(fecha);
        model.addAttribute("horarios", horarios);
        return "horarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        horarioService.eliminar(id);
        return "redirect:/cliente/horarios";
    }
}
