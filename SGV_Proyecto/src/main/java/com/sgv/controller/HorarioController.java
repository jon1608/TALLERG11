package com.sgv.controller;

import com.sgv.service.HorarioDisponibleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
