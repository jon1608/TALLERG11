package com.sgv.controller;

import com.sgv.model.Pago;
import com.sgv.model.Pago.EstadoPago;
import com.sgv.model.Pago.MetodoPago;
import com.sgv.service.PagoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    /** Registrar un pago (por defecto lo dejamos CONFIRMADO).
     *  Se llama desde detalle_factura.html.
     */
    @PostMapping("/registrar")
    public String registrarPago(@RequestParam(value = "facturaId", required = false) Long facturaId,
                                @RequestParam("monto") double monto,
                                @RequestParam("metodo") MetodoPago metodo,
                                @RequestParam(value = "referencia", required = false) String referencia,
                                @RequestParam(value = "observacion", required = false) String observacion,
                                RedirectAttributes ra) {
        try {
            if (facturaId == null) {
                ra.addFlashAttribute("error", "Primero guarde la factura para poder registrar pagos.");
                return "redirect:/admin/facturas"; // o a donde prefieras
            }

            Pago p = new Pago();
            p.setMonto(monto);
            p.setMetodo(metodo);
            p.setReferencia(referencia);
            p.setObservacion(observacion);
            p.setEstado(EstadoPago.CONFIRMADO);

            var facturaRef = new com.sgv.model.Factura();
            facturaRef.setId(facturaId);
            p.setFactura(facturaRef);

            pagoService.registrar(p);
            ra.addFlashAttribute("mensaje", "Pago registrado correctamente.");
            return "redirect:/admin/facturas/ver/" + facturaId;

        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/admin/facturas/ver/" + (facturaId != null ? facturaId : "");
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Ocurrió un error al registrar el pago.");
            return "redirect:/admin/facturas/ver/" + (facturaId != null ? facturaId : "");
        }
    }


    /** Anular un pago (cambia el estado a CANCELADO). */
    @PostMapping("/{pagoId}/anular")
    public String anularPago(@PathVariable Long pagoId,
                             @RequestParam("facturaId") Long facturaId,
                             RedirectAttributes ra) {
        try {
            pagoService.actualizarEstado(pagoId, EstadoPago.CANCELADO);
            ra.addFlashAttribute("mensaje", "Pago anulado.");
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "No se pudo anular el pago.");
        }
        return "redirect:/admin/facturas/ver/" + facturaId;
    }

    /** Marcar pago como CONFIRMADO (por si algún flujo lo crea como PENDIENTE). */
    @PostMapping("/{pagoId}/confirmar")
    public String confirmarPago(@PathVariable Long pagoId,
                                @RequestParam("facturaId") Long facturaId,
                                RedirectAttributes ra) {
        try {
            pagoService.actualizarEstado(pagoId, EstadoPago.CONFIRMADO);
            ra.addFlashAttribute("mensaje", "Pago confirmado.");
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "No se pudo confirmar el pago.");
        }
        return "redirect:/admin/facturas/ver/" + facturaId;
    }
}
