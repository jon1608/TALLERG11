package com.sgv.controller;

import com.sgv.model.Cliente;
import com.sgv.model.Factura;
import com.sgv.model.ItemFactura;
import com.sgv.service.ClienteService;
import com.sgv.service.FacturaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return "facturas"; // ✅ ahora busca templates/facturas.html
    }


    @GetMapping("/nueva")
    public String nuevaFactura(Model model) {
        Factura factura = new Factura();

        // Ya no lanza error porque items está inicializado
        ItemFactura itemInicial = new ItemFactura();
        factura.getItems().add(itemInicial);

        model.addAttribute("factura", factura);
        model.addAttribute("clientes", clienteService.obtenerTodos());
        return "form_factura"; // Asegúrate de tener esta vista
    }


    @PostMapping("/guardar")
    public String guardarFactura(@ModelAttribute("factura") Factura factura) {

        if (factura.getItems() != null) {
            for (ItemFactura item : factura.getItems()) {
                item.setFactura(factura);
            }
        }

        // Calcular totales ANTES de guardar
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

        if (factura.getItems() != null) {
            for (ItemFactura item : factura.getItems()) {
                item.setFactura(factura);
            }
        }

        // Calcular los totales antes de mostrar
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
        return "facturas/preview_factura";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarFactura(@PathVariable Long id) {
        facturaService.eliminar(id);
        return "redirect:/admin/facturas";
    }
}
