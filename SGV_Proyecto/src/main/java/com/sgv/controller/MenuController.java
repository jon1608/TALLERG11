package com.sgv.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    @GetMapping("/menu")
    public String mostrarMenu() {
        return "dashboard"; // Nombre del HTML
    }

    @GetMapping("/facturar")
    public String redirigirAFacturas() {
        return "redirect:/admin/facturas";
    }
}
