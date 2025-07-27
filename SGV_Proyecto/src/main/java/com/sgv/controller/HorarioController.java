package com.sgv.controller;

import com.sgv.model.HorarioDisponible;
import com.sgv.service.HorarioDisponibleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

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

    // 👉 Mostrar formulario de nuevo horario
    @GetMapping("/nueva")
    public String nuevoHorario(Model model) {
        model.addAttribute("horario", new HorarioDisponible());
        return "form_horario";
    }

    // 👉 Guardar horario, validando que no exista duplicado
    @PostMapping("/guardar")
    public String guardarHorario(@ModelAttribute("horario") HorarioDisponible horario, Model model) {
        
       if (horarioService.existeHorario(horario.getFecha(), horario.getHora())) {
            model.addAttribute("error", "Ya existe un horario con esa fecha y hora");
            model.addAttribute("horario", horario);
            return "form_horario";
        }


        // Siempre se guarda como "Disponible"
        horario.setEstado("Disponible");
        horarioService.guardar(horario);

        return "redirect:/cliente/horarios";
    }
}
