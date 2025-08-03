package com.sgv.controller;

import com.sgv.model.Cliente;
import com.sgv.model.Factura;
import com.sgv.model.ItemFactura;
import com.sgv.service.ClienteService;
import com.sgv.service.FacturaService;
import com.sgv.service.VehiculoService; 

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VehiculoService vehiculoService; // <-- inyección del servicio de vehículos

    @GetMapping
    public String listarFacturas(Model model) {
        model.addAttribute("facturas", facturaService.obtenerTodas());
        return "facturas"; // busca templates/facturas.html
    }

    @GetMapping("/nueva")
    public String nuevaFactura(Model model) {
        Factura factura = new Factura();
        factura.getItems().add(new ItemFactura());

        model.addAttribute("factura", factura);
        model.addAttribute("clientes", clienteService.obtenerTodos());
        model.addAttribute("vehiculos", vehiculoService.obtenerTodos()); // <-- AGREGA ESTA LÍNEA

        return "form_factura";
    }


    @PostMapping("/guardar")
    public String guardarFactura(@ModelAttribute("factura") Factura factura) {

        if (factura.getItems() != null) {
            for (ItemFactura item : factura.getItems()) {
                item.setFactura(factura);
            }
        }

        double subtotal = 0;
        for (ItemFactura item : factura.getItems()) {
            subtotal += item.getCantidad() * item.getPrecioUnitario();
        }
        double iva = subtotal * 0.13;
        double total = subtotal + iva;

        factura.setSubtotal(subtotal);
        factura.setIva(iva);
        factura.setTotal(total);

        facturaService.guardar(factura);
        return "redirect:/admin/facturas";
    }

    @PostMapping("/preview")
    public String vistaPreviaFactura(@ModelAttribute("factura") Factura factura, Model model) {

        // Cliente
        if (factura.getCliente() != null && factura.getCliente().getId() != null) {
            Cliente clienteCompleto = clienteService.obtenerPorId(factura.getCliente().getId()).orElse(null);
            factura.setCliente(clienteCompleto);
        }

        // Vehículo
        if (factura.getVehiculo() != null && factura.getVehiculo().getId() != null) {
            factura.setVehiculo(
                vehiculoService.obtenerPorId(factura.getVehiculo().getId()).orElse(null)
            );
        }

        // Asignar factura a cada ítem
        if (factura.getItems() != null) {
            for (ItemFactura item : factura.getItems()) {
                item.setFactura(factura);
            }
        }

        // Calcular totales
        double subtotal = 0;
        for (ItemFactura item : factura.getItems()) {
            subtotal += item.getCantidad() * item.getPrecioUnitario();
        }
        double iva = subtotal * 0.13;
        double total = subtotal + iva;

        factura.setSubtotal(subtotal);
        factura.setIva(iva);
        factura.setTotal(total);

        model.addAttribute("factura", factura);
        return "detalle_Factura";
    }


    @GetMapping("/eliminar/{id}")
    public String eliminarFactura(@PathVariable Long id) {
        facturaService.eliminar(id);
        return "redirect:/admin/facturas";
    }
}
