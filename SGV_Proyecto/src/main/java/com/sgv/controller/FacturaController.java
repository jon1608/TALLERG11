package com.sgv.controller;

import com.sgv.model.Cliente;
import com.sgv.model.Factura;
import com.sgv.model.ItemFactura;
import com.sgv.model.Vehiculo;
import com.sgv.service.ClienteService;
import com.sgv.service.FacturaService;
import com.sgv.service.VehiculoService;
import com.sgv.service.PagoService; // ✅ importa PagoService

import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private PagoService pagoService; // ✅ inyección del servicio de pagos

    @GetMapping
    public String listarFacturas(Model model) {
        model.addAttribute("facturas", facturaService.obtenerTodas());
        return "facturas";
    }

    @GetMapping("/nueva")
    public String nuevaFactura(Model model) {
        Factura factura = new Factura();
        factura.getItems().add(new ItemFactura());
        model.addAttribute("factura", factura);
        model.addAttribute("clientes", clienteService.obtenerTodos());
        return "form_factura";
    }

    @PostMapping("/guardar")
    public String guardarFactura(@Valid @ModelAttribute("factura") Factura factura,
                                 BindingResult result,
                                 Model model) {

        // --- Validar cliente
        if (factura.getCliente() == null || factura.getCliente().getId() == null || factura.getCliente().getId() <= 0) {
            result.rejectValue("cliente", null, "Debe seleccionar un cliente válido.");
        }

        // --- Validar vehículo
        if (factura.getVehiculo() == null || factura.getVehiculo().getId() == null || factura.getVehiculo().getId() <= 0) {
            result.rejectValue("vehiculo", null, "Debe seleccionar un vehículo válido.");
        } else {
            Optional<Vehiculo> vehiculoOpt = vehiculoService.obtenerPorId(factura.getVehiculo().getId());
            if (vehiculoOpt.isEmpty()) {
                result.rejectValue("vehiculo", null, "El vehículo seleccionado no existe.");
            } else {
                Vehiculo v = vehiculoOpt.get();
                Long clienteIdElegido = (factura.getCliente() != null) ? factura.getCliente().getId() : null;
                Long clienteIdVehiculo = (v.getCliente() != null) ? v.getCliente().getId() : null;
                if (clienteIdElegido == null || !clienteIdElegido.equals(clienteIdVehiculo)) {
                    result.rejectValue("vehiculo", null, "El vehículo no pertenece al cliente seleccionado.");
                }
                factura.setVehiculo(v);
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.obtenerTodos());
            return "form_factura";
        }

        // --- Relación item -> factura y limpieza de filas vacías
        if (factura.getItems() != null) {
            factura.getItems().removeIf(i ->
                (i.getDescripcion() == null || i.getDescripcion().isBlank())
                && i.getCantidad() <= 0
                && i.getPrecioUnitario() <= 0
            );
            for (ItemFactura item : factura.getItems()) {
                item.setFactura(factura);
            }
        }

        // --- Cálculo de totales
        double subtotal = 0.0;
        if (factura.getItems() != null) {
            for (ItemFactura item : factura.getItems()) {
                double cant   = item.getCantidad();
                double precio = item.getPrecioUnitario();
                subtotal += cant * precio;
            }
        }
        factura.setSubtotal(subtotal);
        factura.setIva(subtotal * 0.13);
        factura.setTotal(factura.getSubtotal() + factura.getIva());

        facturaService.guardar(factura);
        return "redirect:/admin/facturas";
    }

    @PostMapping("/preview")
    public String vistaPreviaFactura(@ModelAttribute("factura") Factura factura, Model model) {
        if (factura.getCliente() != null && factura.getCliente().getId() != null) {
            factura.setCliente(clienteService.obtenerPorId(factura.getCliente().getId()).orElse(null));
        }
        if (factura.getVehiculo() != null && factura.getVehiculo().getId() != null) {
            factura.setVehiculo(vehiculoService.obtenerPorId(factura.getVehiculo().getId()).orElse(null));
        }

        if (factura.getItems() != null) {
            for (ItemFactura item : factura.getItems()) item.setFactura(factura);
        }

        double subtotal = 0.0;
        if (factura.getItems() != null) {
            for (ItemFactura item : factura.getItems()) {
                double cant   = item.getCantidad();
                double precio = item.getPrecioUnitario();
                subtotal += cant * precio;
            }
        }
        factura.setSubtotal(subtotal);
        factura.setIva(subtotal * 0.13);
        factura.setTotal(factura.getSubtotal() + factura.getIva());

        model.addAttribute("factura", factura);
        return "detalle_factura";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarFactura(@PathVariable Long id) {
        facturaService.eliminar(id);
        return "redirect:/admin/facturas";
    }

    @GetMapping("/ver/{id}")
    public String verFactura(@PathVariable Long id, Model model) {
        Factura factura = facturaService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        model.addAttribute("factura", factura);

        // ✅ pagos para la tabla
        model.addAttribute("pagos", pagoService.listarPorFactura(id));

        // ✅ si tu Factura no tiene @Transient totalPagado/saldo, calcula aquí:
        double totalPagado = pagoService.totalPagadoConfirmado(id);
        double saldo = Math.max(0d, factura.getTotal() - totalPagado);
        model.addAttribute("totalPagado", totalPagado);
        model.addAttribute("saldo", saldo);

        return "detalle_factura";
    }
}
