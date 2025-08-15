package com.sgv.controller;

import com.sgv.model.Cliente;
import com.sgv.model.Vehiculo;
import com.sgv.service.ClienteService;
import com.sgv.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private ClienteService clienteService;

    /** DTO mínimo para combos dependientes */
    public record VehiculoOption(Long id, String placa) {}

    /** Vista: lista/gestiona vehículos de un cliente */
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
                return "redirect:/admin/clientes";
            }
        } catch (Exception e) {
            System.out.println("Error al listar vehículos del cliente:");
            e.printStackTrace();
            return "redirect:/admin/clientes";
        }
    }

    /** Acción: guardar vehículo nuevo para un cliente */
    @PostMapping("/guardar")
    public String guardarVehiculo(@ModelAttribute Vehiculo vehiculo,
                                  BindingResult result,
                                  @RequestParam Long clienteId,
                                  Model model) {

        // Asignar cliente antes de validar
        Optional<Cliente> cliente = clienteService.obtenerPorId(clienteId);
        cliente.ifPresent(vehiculo::setCliente);

        // Validaciones simples
        if (vehiculo.getPlaca() == null || vehiculo.getPlaca().isBlank()) {
            result.rejectValue("placa", null, "La placa es obligatoria.");
        }
        if (vehiculo.getCliente() == null) {
            result.rejectValue("cliente", null, "Debe seleccionar un cliente válido.");
        }

        if (result.hasErrors()) {
            System.out.println(">>> Hay errores en el formulario:");
            result.getAllErrors().forEach(error -> System.out.println(" - " + error.getDefaultMessage()));
            cliente.ifPresent(c -> model.addAttribute("cliente", c));
            model.addAttribute("vehiculos", vehiculoService.obtenerPorClienteId(clienteId));
            model.addAttribute("vehiculo", vehiculo); // mantener datos del form
            return "vehiculos_cliente";
        }

        vehiculoService.guardar(vehiculo);
        return "redirect:/admin/vehiculos/cliente/" + clienteId;
    }

    /** API: devuelve SOLO id y placa (para el combo dependiente) */
    @GetMapping(value = "/cliente/{clienteId}/placas", produces = "application/json")
    @ResponseBody
    public List<VehiculoOption> obtenerVehiculosPorCliente(@PathVariable Long clienteId) {
        return vehiculoService.obtenerPorClienteId(clienteId)
                .stream()
                .map(v -> new VehiculoOption(v.getId(), v.getPlaca()))
                .toList();
    }
}
