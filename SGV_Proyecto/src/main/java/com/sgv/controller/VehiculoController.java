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
        Optional<Cliente> cliente = clienteService.obtenerPorId(clienteId);
        if (cliente.isPresent()) {
            model.addAttribute("cliente", cliente.get());
            model.addAttribute("vehiculos", vehiculoService.obtenerPorClienteId(clienteId));
            model.addAttribute("vehiculo", new Vehiculo());
            return "vehiculos_cliente";
        }
        return "redirect:/admin/clientes";
    }

    // Guardar vehículo nuevo
    @PostMapping("/guardar")
    public String guardarVehiculo(@ModelAttribute Vehiculo vehiculo, @RequestParam Long clienteId) {
        Optional<Cliente> cliente = clienteService.obtenerPorId(clienteId);
        cliente.ifPresent(vehiculo::setCliente);
        vehiculoService.guardar(vehiculo);
        return "redirect:/admin/vehiculos/cliente/" + clienteId;
    }
}
