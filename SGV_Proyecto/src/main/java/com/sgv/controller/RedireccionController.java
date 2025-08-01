/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sgv.controller;

/**
 *
 * @author Jonathan Umaña R
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedireccionController {

    // Este método captura /citas y redirige a /cliente/horarios
    @GetMapping("/citas")
    public String redirigirAHorarios() {
        return "redirect:/cliente/horarios";
    }
}
