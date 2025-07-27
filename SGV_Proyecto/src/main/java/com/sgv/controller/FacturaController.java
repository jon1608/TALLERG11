package com.sgv.controller;

import com.sgv.model.Factura;
import com.sgv.model.ItemFactura;
import com.sgv.model.Cliente;
import com.sgv.service.ClienteService;
import com.sgv.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/admin/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listarFacturas(Model model) {
        model.addAttribute("facturas", facturaService.obtenerTodas());
        return "facturas";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        Factura factura = new Factura();
        factura.setItems(new ArrayList<>());
        factura.getItems().add(new ItemFactura());
        factura.getItems().add(new ItemFactura());

        model.addAttribute("factura", factura);
        model.addAttribute("clientes", clienteService.obtenerTodos());
        return "form_factura";
    }

    @PostMapping("/guardar")
    public String guardarFactura(@ModelAttribute Factura factura, @RequestParam("cliente") Long clienteId) {
        Cliente cliente = clienteService.obtenerPorId(clienteId).orElse(null);
        factura.setCliente(cliente);

        double subtotal = factura.getItems().stream()
                .mapToDouble(item -> item.getCantidad() * item.getPrecioUnitario())
                .sum();
        double iva = subtotal * 0.13;
        double total = subtotal + iva;

        factura.setSubtotal(subtotal);
        factura.setIva(iva);
        factura.setTotal(total);

        facturaService.guardar(factura);
        return "redirect:/admin/facturas";
    }

    @PostMapping("/preview")
    public String vistaPreviaFactura(@ModelAttribute Factura factura, 
                                     @RequestParam("cliente") Long clienteId,
                                     Model model) {
        Cliente cliente = clienteService.obtenerPorId(clienteId).orElse(null);
        factura.setCliente(cliente);

        double subtotal = factura.getItems().stream()
                .mapToDouble(item -> item.getCantidad() * item.getPrecioUnitario())
                .sum();
        double iva = subtotal * 0.13;
        double total = subtotal + iva;

        factura.setSubtotal(subtotal);
        factura.setIva(iva);
        factura.setTotal(total);

        model.addAttribute("factura", factura);
        return "detalle_factura";
    }

    @GetMapping("/ver/{id}")
    public String verFactura(@PathVariable Long id, Model model) {
        Factura factura = facturaService.obtenerPorId(id).orElse(null);
        if (factura == null) {
            return "redirect:/admin/facturas";
        }
        model.addAttribute("factura", factura);
        return "detalle_factura";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarFactura(@PathVariable Long id) {
        facturaService.eliminar(id);
        return "redirect:/admin/facturas";
    }
}
