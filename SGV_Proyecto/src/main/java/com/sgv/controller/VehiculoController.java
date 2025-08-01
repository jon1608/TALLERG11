/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sgv.controller;

import com.sgv.model.Cliente;
import com.sgv.model.Vehiculo;
import com.sgv.service.ClienteService;
import com.sgv.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;


import java.util.Optional;

@Controller
@RequestMapping("/admin/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private ClienteService clienteService;

    // Mostrar los vehículos de un cliente específico
    @GetMapping("/cliente/{clienteId}")
    public String listarVehiculosCliente(@PathVariable Long clienteId, Model model) {
    try {
        Optional<Cliente> cliente = clienteService.obtenerPorId(clienteId);
        if (cliente.isPresent()) {
            model.addAttribute("cliente", cliente.get());
            model.addAttribute("vehiculos", vehiculoService.obtenerPorClienteId(clienteId));
            model.addAttribute("vehiculo", new Vehiculo());
            return "vehiculos_cliente";
        } else {
            System.out.println("Cliente no encontrado con ID: " + clienteId);
        }
    } catch (Exception e) {
        System.out.println("Error al listar vehículos del cliente:");
        e.printStackTrace(); // Imprime el error detallado en la consola
    }
        return "redirect:/admin/clientes";
}


    // Guardar vehículo nuevo
   @PostMapping("/guardar")
    public String guardarVehiculo(@Valid @ModelAttribute Vehiculo vehiculo, 
                              BindingResult result,
                              @RequestParam Long clienteId,
                              Model model) {
    if (result.hasErrors()) {
        Optional<Cliente> cliente = clienteService.obtenerPorId(clienteId);
        cliente.ifPresent(c -> model.addAttribute("cliente", c));
        model.addAttribute("vehiculos", vehiculoService.obtenerPorClienteId(clienteId));
        return "vehiculos_cliente";
        }

    Optional<Cliente> cliente = clienteService.obtenerPorId(clienteId);
    cliente.ifPresent(vehiculo::setCliente);
    vehiculoService.guardar(vehiculo);
    return "redirect:/admin/vehiculos/cliente/" + clienteId;
    }

}
